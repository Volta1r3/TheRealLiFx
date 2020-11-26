package com.example.thereallifx;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;



public class MainActivity extends AppCompatActivity {
    private Intent serviceIntent;
    @SuppressLint("ApplySharedPref")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("pref_name", MODE_PRIVATE);
        if (!preferences.getBoolean("started_boolean", false)){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("started_boolean", true);
            editor.commit();

            DialogFragment newFragment = new GetBulbNumber();
            newFragment.setCancelable(false);
            newFragment.show(getSupportFragmentManager(), "showing");
        }





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


        final Context mainContext = this;
        final int sense = preferences.getInt("has_started_before", 1000);

        ImageButton powerButton = findViewById(R.id.powerButton);
        powerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!MainService.isRunning){
                    serviceIntent = new Intent(mainContext, MainService.class);
                    serviceIntent.putExtra("sensitivity", sense);
                    startForegroundService(serviceIntent);
                }
                else{
                    stopService(serviceIntent);
                }
            }
        });


        ImageButton settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainService.isRunning){
                    stopService(serviceIntent);
                }
                Context context = getApplicationContext();
                Intent intent = new Intent(context, SettingsActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onDestroy() {
        if(MainService.isRunning) {
            stopService(serviceIntent);
        }
        super.onDestroy();
    }

}