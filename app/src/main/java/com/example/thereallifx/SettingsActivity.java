package com.example.thereallifx;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        final TextView textView = findViewById(R.id.textView);
        final SharedPreferences preferences = getSharedPreferences("pref_name", MODE_PRIVATE);
        final int startProgress = preferences.getInt("has_started_before", 1000);
        textView.setText(String.valueOf(startProgress));

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView bottomView = findViewById(R.id.adViewBottom);
        AdRequest adRequestBottom = new AdRequest.Builder().build();
        bottomView.loadAd(adRequestBottom);

        AdView topView = findViewById(R.id.adViewTop);
        AdRequest adRequestTop = new AdRequest.Builder().build();
        topView.loadAd(adRequestTop);

        ImageButton upButton = findViewById(R.id.upButton);
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("pref_name", MODE_PRIVATE);
                int sense = sharedPreferences.getInt("has_started_before", 1000);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("has_started_before", sense+100);
                editor.commit();
                textView.setText(String.valueOf(sense+100));
                Log.d("This is the sensitivty", String.valueOf(sense+100));
            }
        });

        ImageButton downButton = findViewById(R.id.downButton);
        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("pref_name", MODE_PRIVATE);
                int sense = sharedPreferences.getInt("has_started_before", 1000);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("has_started_before", sense-100);
                editor.commit();
                textView.setText(String.valueOf(sense-100));
                Log.d("This is the sensitivty", String.valueOf(sense-100));
            }
        });



        ImageButton button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });
        createButtons();

        Button changeBulbNumber = findViewById(R.id.changeBulbNumber);
        changeBulbNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new GetBulbNumber();
                newFragment.setCancelable(false);
                newFragment.show(getSupportFragmentManager(), "showing");
            }
        });






    }
    protected void createButtons(){
        SharedPreferences preferences = getSharedPreferences("pref_name", MODE_PRIVATE);
        BulbClass bulbs= new BulbClass(preferences.getInt("number_bulbs", 3));
        try {
            bulbs.getNames(SettingsActivity.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        final LinearLayout buttonArray = findViewById(R.id.buttonLayout);
        final ArrayList<String> names =  BulbNames.bulbNames;
        int numberOfButtons = preferences.getInt("number_bulbs", 3);
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

                try {
                    button.setText(names.get(i * 2 + j));
                }
                catch (IndexOutOfBoundsException e){
                }



                final int finalI = i;
                final int finalJ = j;
                try {
                if (preferences.contains(names.get(finalI *2+ finalJ))){
                    button.setBackgroundResource(R.drawable.color_true);
                }
                else {
                    button.setBackgroundResource(R.drawable.color_null);
                }}
                catch (IndexOutOfBoundsException e){
                }

                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        SharedPreferences preferences = getSharedPreferences("pref_name", MODE_PRIVATE);
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
