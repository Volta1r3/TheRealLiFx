package com.example.thereallifx;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Intent serviceIntent;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceIntent = new Intent(this, MainService.class);
        serviceIntent.putExtra("sensitivity", 1500);
        startForegroundService(serviceIntent);
        SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                return;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                return;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                TextView textView = findViewById(R.id.textView);
                textView.setText(String.valueOf(seekBar.getProgress()));
                change(seekBar.getProgress());
            }
        });
        try {
            createButtons();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onDestroy() {
        stopService(serviceIntent);
        super.onDestroy();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void change (int sense){
        if (serviceIntent != null){
            stopService(new Intent(this, MainService.class));
        }
        serviceIntent = new Intent(this, MainService.class);
        serviceIntent.putExtra("sensitivity", sense);
        startForegroundService(serviceIntent);
    }
    protected void createButtons() throws InterruptedException {

        BulbClass bulbs= new BulbClass(3);
        bulbs.getNames(MainActivity.class);


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        final LinearLayout buttonArray = findViewById(R.id.buttonLayout);

        final ArrayList <String> names =  BulbNames.bulbNames;

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
                Button button=new Button(this);

                button.setWidth(20);
                button.setId(buttonIdNumber);
                button.setLayoutParams(params);
                button.setText(names.get(i*2+j));
                final int finalI = i;
                final int finalJ = j;
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        SharedPreferences preferences = getSharedPreferences("prefName", MODE_PRIVATE);
                        SharedPreferences.Editor edit = preferences.edit();
                        if (preferences.contains(names.get(finalI *2+ finalJ))){
                            edit.remove(names.get(finalI *2+ finalJ));
                            edit.commit();
                            Log.d("removed", "removed");
                        }
                        else {
                            edit.putString(names.get(finalI *2+ finalJ), "don't touch");
                            Log.d("don't touch", "don't touch");
                            edit.commit();
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