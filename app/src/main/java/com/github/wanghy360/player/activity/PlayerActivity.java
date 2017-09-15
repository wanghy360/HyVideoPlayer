package com.github.wanghy360.player.activity;

import android.os.Bundle;

import com.github.wanghy360.hyvideolibrary.player.Player;
import com.github.wanghy360.player.presenter.MyPlayerPresenter;
import com.github.wanghy360.player.view.MyPlayView;

/**
 * Created by Wanghy on 2017/9/15
 */
public abstract class PlayerActivity<V> extends BaseActivity implements MyPlayView<PlayerActivity> {
    protected V videoView;
    private MyPlayerPresenter<V> myPlayerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void init(Player<V> player) {
        myPlayerPresenter = new MyPlayerPresenter<>(this);
        myPlayerPresenter.subscribe(videoView, player);
        myPlayerPresenter.startVideo("rtmp://live.hkstv.hk.lxdns.com/live/hks");
    }

    @Override
    protected void onPause() {
        super.onPause();
        myPlayerPresenter.runInBackground();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myPlayerPresenter.runInForeground();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myPlayerPresenter.unSubscribe();
    }

    @Override
    public PlayerActivity getViewActivity() {
        return this;
    }
}
