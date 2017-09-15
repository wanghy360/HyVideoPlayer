package com.github.wanghy360.player.activity;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Wanghy on 2017/9/15
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected String TAG = getClass().getSimpleName();
    protected  void initView(){}
    protected  void initData(){}
}
