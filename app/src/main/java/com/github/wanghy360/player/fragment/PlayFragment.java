package com.github.wanghy360.player.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.wanghy360.ksylibrary.player.KsyPlayer;
import com.github.wanghy360.player.R;
import com.github.wanghy360.player.activity.BaseActivity;
import com.github.wanghy360.player.bean.PlayerBean;
import com.github.wanghy360.player.presenter.MyPlayerPresenter;
import com.github.wanghy360.player.view.MyPlayView;
import com.ksyun.media.player.KSYTextureView;


/**
 * @author Wanghy
 */
public class PlayFragment extends BaseFragment implements MyPlayView<BaseActivity> {
    KSYTextureView mXYVideoView;
    private PlayerBean mPlayerBean;
    private MyPlayerPresenter<KSYTextureView> playerPresenter;

    public PlayFragment() {
    }

    public static PlayFragment newInstance(PlayerBean playerBean) {
        PlayFragment fragment = new PlayFragment();
        fragment.updateData(playerBean);
        return fragment;
    }

    public void updateData(PlayerBean playerBean) {
        this.mPlayerBean = playerBean;
        if (isResumed()){
            startVideo();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play, container, false);
        mXYVideoView = (KSYTextureView) view.findViewById(R.id.id_detail_player);
        playerPresenter = new MyPlayerPresenter<>(this);
        playerPresenter.subscribe(mXYVideoView, new KsyPlayer());
        if (mPlayerBean != null) {
            startVideo();
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void startVideo() {
        playerPresenter.startVideo(mPlayerBean.getUrl());
    }

    public void stopPlay() {
        playerPresenter.stopPlay();
    }

    @Override
    public BaseActivity getViewActivity() {
        return (BaseActivity) getActivity();
    }


    @Override
    public void onPause() {
        super.onPause();
        playerPresenter.runInBackground();
    }

    @Override
    public void onResume() {
        super.onResume();
        playerPresenter.runInForeground();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        playerPresenter.unSubscribe();
    }
}