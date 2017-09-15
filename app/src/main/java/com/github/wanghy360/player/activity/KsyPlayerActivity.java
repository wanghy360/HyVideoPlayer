package com.github.wanghy360.player.activity;

import android.os.Bundle;

import com.github.wanghy360.ksylibrary.player.KsyPlayer;
import com.github.wanghy360.player.R;
import com.ksyun.media.player.KSYTextureView;

public class KsyPlayerActivity extends PlayerActivity<KSYTextureView> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ksy_player);
        initView();
        init(new KsyPlayer());
    }

    @Override
    protected void initView() {
        videoView = (KSYTextureView) findViewById(R.id.id_ksy_player);
    }
}
