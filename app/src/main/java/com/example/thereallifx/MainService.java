package com.example.thereallifx;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.concurrent.TimeoutException;


public class MainService extends Service {
    Accelerometer accelerometer;
    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    @Nullable
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accelerometer.unregister();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        final int sensitivity = intent.getIntExtra("sensitivity", 1500);
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        final Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText(String.valueOf(sensitivity))
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentIntent(pendingIntent)
                .build();


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                startForeground(1, notification);
                accelerometer = new Accelerometer(getBaseContext());
                accelerometer.register();
                accelerometer.setListener(new Accelerometer.Listener() {
                                              @Override
                                              public void onTranslation(float tx, float ty, float tz) throws TimeoutException, InterruptedException {
                                                  if (ty*ty+tx*tx+tz*tz>sensitivity){
                                                      Object lock = this;
                                                      accelerometer.unregister();
                                                      try {
                                                          BulbClass bulb = new BulbClass(3);
                                                          int y = bulb.bulbStuff(lock);
                                                      } catch (Exception e) {
                                                          e.printStackTrace();
                                                      }
                                                      accelerometer.register();
                                                  }
                                              }
                                          }
                );
            }
        });
        thread.start();
        return START_STICKY;
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
