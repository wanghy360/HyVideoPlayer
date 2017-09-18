package com.github.wanghy360.player.util;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class InputUtils {

    private static final String TAG = "XYInputUtils";

    public static void hideSoftInputWindow(Context context, View view) {
        if (view != null) {
            final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showSoftInputWindow(Context context, View view) {
        if (view != null) {
            final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, 0);
        }
    }

    public static boolean isInputWindowActive(Context context, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            boolean isOpen = imm.isActive(view);
            Log.d(TAG, "isInputWindowActive:" + isOpen);
            return isOpen;
        }
        return false;
    }
}
