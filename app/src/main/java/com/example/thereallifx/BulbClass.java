package com.example.thereallifx;

import net.emirac.lifx.*;
import net.emirac.lifx.BulbScanner.BulbScannerCallback;

public class BulbClass {
    private BulbScanner bulbScanner = BulbScanner.getInstance();
    public int bulbCount = 0;
    private int numBulbs = 3;

    public int bulbStuff(final Object thread) throws InterruptedException {
        bulbScanner.addCallback(new BulbScannerCallback() {
            @Override
            public void onError(String arg0) {
                synchronized (thread) {
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
                    boolean power = arg0.getPower();
                        if (power == true) {
                            arg0.setPower(false, 0);}
                         else{
                                arg0.setPower(true, 0);
                            }
                    if (numBulbs == bulbCount) {
                        synchronized (thread) {
                            thread.notify();
                        }
                    }

                } catch (Exception e) {
                    synchronized (thread) {
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
                return 1;
            }
            bulbScanner.finish();
            return 0;
        }

    }

}