package com.example.thereallifx;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;**/


public class MainActivity extends AppCompatActivity {
    private Intent serviceIntent;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("pref_name", MODE_PRIVATE);
        int sense = preferences.getInt("has_started_before", 1500);

        TextView textView = findViewById(R.id.textView);
        textView.setText(String.valueOf(sense));

        serviceIntent = new Intent(this, MainService.class);
        serviceIntent.putExtra("sensitivity", sense);
        startForegroundService(serviceIntent);

        /**MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });**/
        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(serviceIntent);
                Context context = getApplicationContext();
                Intent intent = new Intent(context, SettingsActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onDestroy() {
        stopService(serviceIntent);
        super.onDestroy();
    }
}