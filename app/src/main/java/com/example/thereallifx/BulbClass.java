package com.example.thereallifx;

import android.content.SharedPreferences;
import net.emirac.lifx.*;
import net.emirac.lifx.BulbScanner.BulbScannerCallback;
import java.util.ArrayList;

public class BulbClass {
    private BulbScanner bulbScanner = BulbScanner.getInstance();
    public int bulbCount = 0;
    private int numBulbs = 0;

    public BulbClass(int bulbs){
        numBulbs = bulbs;
    }

    public int bulbStuff(final Object thread, final SharedPreferences preferences) throws InterruptedException {
        bulbScanner.addCallback(new BulbScannerCallback() {
            @Override
            public void onError(String arg0) {
                synchronized (thread) {
                    numBulbs = 0;
                    thread.notify();
                }
            }

            @Override
            public void onBulbGone(net.emirac.lifx.Bulb arg0) {
            }

            @Override
            public void onNewBulb(net.emirac.lifx.Bulb arg0) {
                bulbCount++;
                try {
                    if (preferences.contains(arg0.getLabel())){
                        if (numBulbs == bulbCount) {
                            synchronized (thread) {
                                numBulbs = 0;
                                thread.notify();
                            }
                        }
                        return;
                    }
                    boolean power = arg0.getPower();
                        if (power == true) {
                            arg0.setPower(false, 0);}
                         else{
                                arg0.setPower(true, 0);
                            }
                    if (numBulbs == bulbCount) {
                        synchronized (thread) {
                            numBulbs = 0;
                            thread.notify();
                        }
                    }

                } catch (Exception e) {
                    synchronized (thread) {
                        numBulbs = 0;
                        thread.notify();
                    }
                }
            }
        });
        synchronized (thread) {
            bulbScanner.start();
            thread.wait(5000);
            if (numBulbs != bulbCount) {
                bulbScanner.finish();
                numBulbs = 0;
                return 1;
            }
            bulbScanner.finish();
            numBulbs = 0;
            return 0;
        }
    }
    public void getNames(final Object thread) throws InterruptedException {
        final ArrayList<String> names = new ArrayList<String>();
        bulbScanner.addCallback(new BulbScannerCallback() {
            @Override
            public void onNewBulb(Bulb bulb) {
                try {
                    names.add(bulb.getLabel());
                    if (numBulbs == bulbCount) {
                        synchronized (thread) {
                            numBulbs = 0;
                            thread.notify();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    synchronized (thread) {
                        numBulbs = 0;
                        thread.notify();
                    }
                }
            }

            @Override
            public void onBulbGone(Bulb bulb) {

            }

            @Override
            public void onError(String s) {
                synchronized (thread) {
                    thread.notify();
                }

            }
        });
        synchronized (thread){
            bulbScanner.start();
            thread.wait(5000);
            if (numBulbs != bulbCount){
                bulbScanner.finish();
                numBulbs = 0;
                BulbNames.bulbNames =  names;
            }
            bulbScanner.finish();
            numBulbs = 0;
            BulbNames.bulbNames =  names;
        }
    }
}