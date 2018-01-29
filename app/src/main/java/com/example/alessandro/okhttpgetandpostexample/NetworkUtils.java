package com.example.alessandro.okhttpgetandpostexample;

/**
 * Created by alessandro on 26/01/18.
 */

public class NetworkUtils {

    // This method encapsulates a generic runnable method inside a new Thread
    public static Thread performOnBackgroundThread(final Runnable runnable) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        thread.start();
        return thread;
    }
}
