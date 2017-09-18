package com.github.wanghy360.player.ui;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by Wanghy on 2017/6/14
 */
public class BlockTouchRelativeLayout extends RelativeLayout {
    public BlockTouchRelativeLayout(Context context) {
        super(context);
    }

    public BlockTouchRelativeLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BlockTouchRelativeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BlockTouchRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        return true;
    }
}
