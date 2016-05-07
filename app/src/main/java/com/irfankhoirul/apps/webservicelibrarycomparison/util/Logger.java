package com.irfankhoirul.apps.webservicelibrarycomparison.util;

import android.util.Log;

/**
 * Created by Sevima on 13/01/2016.
 */
public class Logger {

    public static final int VERBOSE = 4;
    public static final int DEBUG = 3;
    public static final int INFO = 2;
    public static final int WARM = 1;
    public static final int ERROR = 0;

    public static void v(String tag, String msg) {
        if (msg.length() > 4000) {
            Log.v("LOGGER", "Message length = " + msg.length()
                    + ", Split into the following parts: ");
            int chunkCount = msg.length() / 4000;
            for (int i = 0; i <= chunkCount; i++) {
                int max = 4000 * (i + 1);
                if (max >= msg.length()) {
                    Log.v("LOGGER", "part " + i + " of " + chunkCount + "::"
                            + msg.substring(4000 * i));
                } else {
                    Log.v("LOGGER", "part " + i + " of " + chunkCount + "::"
                            + msg.substring(4000 * i, max));
                }
            }
        } else {
            Log.v("LOGGER" + "::" + tag.toUpperCase(), msg);
        }
    }
}
