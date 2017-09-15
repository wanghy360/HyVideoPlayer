package com.github.wanghy360.player.activity;

import android.os.Bundle;

import com.github.wanghy360.ijklibrary.media.IjkVideoView;
import com.github.wanghy360.ijklibrary.player.IjkPlayer;
import com.github.wanghy360.player.R;

public class IjkPlayerActivity extends PlayerActivity<IjkVideoView> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ijk_player);
        initView();
        init(new IjkPlayer());
    }

    @Override
    protected void initView() {
        videoView = (IjkVideoView) findViewById(R.id.id_ijk_player);
    }
}
