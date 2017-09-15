package com.github.wanghy360.hyvideolibrary.util;

import android.util.Log;

import com.github.wanghy360.hyvideolibrary.BuildConfig;

/**
 * @author Wanghy
 *Log类，可在build.gradle文件里开闭log,方便测试。
 */
public final class LogPrint {
    private LogPrint() {
    }

    private static final boolean SHOW_LOG = BuildConfig.ENV_CLIENT_TEST;

    public static void v(String tag, String msg) {
        if (SHOW_LOG) Log.v(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (SHOW_LOG) Log.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (SHOW_LOG) Log.i(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (SHOW_LOG) Log.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (SHOW_LOG) Log.e(tag, msg);
    }
}