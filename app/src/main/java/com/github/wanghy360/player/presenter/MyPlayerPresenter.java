package com.github.wanghy360.player.presenter;

import android.widget.Toast;

import com.github.wanghy360.hyvideolibrary.presenter.PlayerPresenter;
import com.github.wanghy360.player.view.MyPlayView;

/**
 * Created by Wanghy on 2017/9/8
 */

public class MyPlayerPresenter<V> extends PlayerPresenter<V, MyPlayView> {

    public MyPlayerPresenter(MyPlayView view) {
        super(view);
    }

    @Override
    protected boolean isBlockReLoad() {
        return false;
    }

    @Override
    protected void reLoadLiveFail() {
        Toast.makeText(view.getViewActivity(), "reLoadLiveFail", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRenderingStart() {
        super.onRenderingStart();
        Toast.makeText(view.getViewActivity(), "onRenderingStart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void reConnect() {
        super.reConnect();
        Toast.makeText(view.getViewActivity(), "reConnect", Toast.LENGTH_SHORT).show();
    }
}