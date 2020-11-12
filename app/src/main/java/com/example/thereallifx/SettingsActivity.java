package com.example.thereallifx;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        SeekBar seekBar = findViewById(R.id.seekBar);
        final TextView textView = findViewById(R.id.textView);
        final SharedPreferences preferences = getSharedPreferences("pref_name", MODE_PRIVATE);
        int startProgress = preferences.getInt("has_started_before", 1500);
        textView.setText(String.valueOf(startProgress));
        seekBar.setProgress(startProgress);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                return;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                return;
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView.setText(String.valueOf(seekBar.getProgress()));
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("has_started_before", seekBar.getProgress());
                editor.commit();
            }
        });
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });
        createButtons();
    }
    protected void createButtons(){
        BulbClass bulbs= new BulbClass(3);
        try {
            bulbs.getNames(SettingsActivity.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        final LinearLayout buttonArray = findViewById(R.id.buttonLayout);
        final ArrayList<String> names =  BulbNames.bulbNames;
        int numberOfButtons = names.size();
        int numberOfButtonsPerRow = 2;
        int numberOfRows = numberOfButtons/numberOfButtonsPerRow + 1;
        int buttonIdNumber = 0;
        for(int i=0;i<numberOfRows;i++){
            LinearLayout newLine = new LinearLayout(this);
            newLine.setLayoutParams(params);
            newLine.setOrientation(LinearLayout.HORIZONTAL);
            for(int j=0;j<numberOfButtonsPerRow;j++){
                if (i*2+j==numberOfButtons){
                    buttonArray.addView(newLine);
                    return;
                }
                final Button button=new Button(this);
                button.setWidth(20);
                button.setId(buttonIdNumber);
                button.setLayoutParams(params);
                button.setText(names.get(i*2+j));
                final int finalI = i;
                final int finalJ = j;
                SharedPreferences preferences = getSharedPreferences("prefName", MODE_PRIVATE);
                if (preferences.contains(names.get(finalI *2+ finalJ))){
                    button.setBackgroundResource(R.drawable.color_true);
                }
                else {
                    button.setBackgroundResource(R.drawable.color_null);
                }
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        SharedPreferences preferences = getSharedPreferences("prefName", MODE_PRIVATE);
                        SharedPreferences.Editor edit = preferences.edit();
                        if (preferences.contains(names.get(finalI *2+ finalJ))){
                            edit.remove(names.get(finalI *2+ finalJ));
                            edit.commit();
                            Log.d("removed", "removed");
                            button.setBackgroundResource(R.drawable.color_null);
                        }
                        else {
                            edit.putString(names.get(finalI *2+ finalJ), "don't touch");
                            Log.d("don't touch", "don't touch");
                            edit.commit();
                            button.setBackgroundResource(R.drawable.color_true);
                        }
                    }
                });
                newLine.addView(button);
                buttonIdNumber++;
            }
            buttonArray.addView(newLine);
        }
    }
}
