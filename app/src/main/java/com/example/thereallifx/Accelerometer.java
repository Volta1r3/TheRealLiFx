package com.example.thereallifx;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.concurrent.TimeoutException;

public class Accelerometer {
    public interface Listener {
        void onTranslation(float tx, float ty, float tz) throws TimeoutException, InterruptedException;
    }
    private Listener listener;

    public void setListener(Listener l){
        listener = l;
    }

    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;

    public boolean isRegistered;


    Accelerometer(Context context){
        sensorManager = (SensorManager)  context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorEventListener = new SensorEventListener(){
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (listener!=null){
                    try {
                        listener.onTranslation(event.values[0], event.values[1], event.values[2]);
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) { }
        };

    }

    public void register(){ sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_FASTEST); isRegistered = true; }

    public void unregister(){
        sensorManager.unregisterListener(sensorEventListener); isRegistered = false;
    }

}
