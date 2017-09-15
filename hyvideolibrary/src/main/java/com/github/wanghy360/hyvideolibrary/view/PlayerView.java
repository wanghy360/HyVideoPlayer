package com.github.wanghy360.hyvideolibrary.view;

import android.app.Activity;

/**
 * Created by Wanghy on 2017/9/8
 */
public interface PlayerView<T extends Activity> extends BaseView{
    T getViewActivity();
}
