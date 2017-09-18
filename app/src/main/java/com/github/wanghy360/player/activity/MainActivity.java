package com.github.wanghy360.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.wanghy360.player.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //金山直播
    public void startKsy(View view) {
        startActivity(new Intent(this,KsyPlayerActivity.class));
    }
    //Bilibili直播
    public void startIjk(View view) {
        startActivity(new Intent(this,IjkPlayerActivity.class));
    }

    public void startDetail(View view) {
        startActivity(new Intent(this,PlayerDetailActivity.class));
    }
}
