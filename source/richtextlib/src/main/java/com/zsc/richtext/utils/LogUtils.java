package com.zsc.richtext.utils;

import android.util.Log;


/**
 * 日志输出工具
 */
public class LogUtils {

    private static boolean enableDefaultLog = true;

    /**
     * 设置Log开关
     *
     * @param printLog
     */
    public static void setLogEnable(boolean printLog) {
        enableDefaultLog = printLog;
    }

    /**
     * Syetem.out 输出日志
     *
     * @param msg
     */
    public static void log(String msg) {
        if (msg == null) {
            msg = "";
        }
        if (enableDefaultLog) {
            System.out.println(msg);
        }
    }

    public static void e(String tag, String msg) {
        if (msg == null) {
            msg = "";
        }
        if (enableDefaultLog) {
            Log.e(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (msg == null) {
            msg = "";
        }
        if (enableDefaultLog) {
            Log.d(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (msg == null) {
            msg = "";
        }
        if (enableDefaultLog) {
            Log.v(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (msg == null) {
            msg = "";
        }
        if (enableDefaultLog) {
            Log.w(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (msg == null) {
            msg = "";
        }
        if (enableDefaultLog) {
            Log.i(tag, msg);
        }
    }
}