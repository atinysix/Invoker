package com.daiwj.invoker.runtime;

import android.util.Log;

import com.daiwj.invoker.BuildConfig;
import com.daiwj.invoker.Invoker;

/**
 * author: daiwj on 1/9/21 16:02
 */
public class Logger {

    private static String TAG = Invoker.class.getSimpleName();

    private static boolean sDebug = BuildConfig.DEBUG;

    public static void setDebug(boolean debug) {
        Logger.sDebug = debug;
    }

    public static void d(String tag, String message) {
        if (sDebug) {
            Log.d(TAG + "_" + tag, message);
        }
    }

    public static void w(String tag, String message) {
        if (sDebug) {
            Log.w(TAG + "_" + tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (sDebug) {
            Log.e(TAG + "_" + tag, message);
        }
    }

}
