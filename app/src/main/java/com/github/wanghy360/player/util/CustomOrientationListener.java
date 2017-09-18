package com.github.wanghy360.player.util;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.OrientationEventListener;
import android.view.Surface;

/**
 * Created by Wanghy on 2017/5/23
 */
public abstract class CustomOrientationListener extends OrientationEventListener {
    private int physicsOrientation = ORIENTATION_UNKNOWN;//物理旋转角度,from 0 to 359
    private State physicsState;//物理状态
    private State realState = State.NOT_LAND;//实际状态

    public CustomOrientationListener(Context context) {
        super(context);
        switch (DensityUtils.getDisplayRotation(context)) {
            case Surface.ROTATION_0:
                physicsOrientation = 0;
                break;
            case Surface.ROTATION_90:
                physicsOrientation = 90;
                break;
            case Surface.ROTATION_180:
                physicsOrientation = 180;
                break;
            case Surface.ROTATION_270:
                physicsOrientation = 270;
                break;
            default:
                physicsOrientation = 0;
        }
        physicsState = getStateFromRotation(physicsOrientation);
    }

    @Override
    public void onOrientationChanged(int orientation) {
        physicsOrientation = orientation;
        State state = getStateFromRotation(orientation);
        if (physicsState != state) {
            physicsState = state;
            if (physicsState != State.NOT_LAND && realState != State.NOT_LAND && physicsState != realState) {//物理状态与实际状态不为竖屏且互不相同，才回调onStateChanged
                onLandStateChanged(physicsState.getScreenOrientation());
                realState = physicsState;
            }
        }
    }

    private State getStateFromRotation(int rotation) {
        if (rotation > 225 && rotation < 315) {
            return State.LAND;
        } else if (rotation > 45 && rotation < 135) {
            return State.REVERSE_LAND;
        } else {
            return State.NOT_LAND;
        }
    }

    public void setRealState(State realState) {
        this.realState = realState;
    }

    public abstract void onLandStateChanged(int screenOrientation);

    public enum State {
        LAND(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE),
        REVERSE_LAND(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE),
        NOT_LAND(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        private int screenOrientation;

        State(int screenOrientation) {
            this.screenOrientation = screenOrientation;
        }

        public int getScreenOrientation() {
            return screenOrientation;
        }
    }

    /**
     * 判断当前屏幕方向接近于<b>横屏<b/>，还是<b>横屏倒转<b/>
     */
    public boolean isTendLand() {
        return physicsOrientation == 0 || physicsOrientation >= 180 && physicsOrientation < 360;
    }
}
