package com.github.wanghy360.hyvideolibrary.presenter;

import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.github.wanghy360.hyvideolibrary.player.Player;
import com.github.wanghy360.hyvideolibrary.util.LogPrint;
import com.github.wanghy360.hyvideolibrary.view.PlayerView;

/**
 * Created by Wanghy on 2017/9/8
 * 播放逻辑
 */
public abstract class PlayerPresenter<V, P extends PlayerView> extends BasePresenter<P> implements Player.Callback {
    protected String TAG = getClass().getSimpleName();
    private Player player;
    private String liveUrl;
    private Handler handler;
    private boolean isStopPlay = false;
    private int reConnectTryTimes = 0;
    private static final int RECONNECT_TIME_HOST_LEAVE = 10 * 1000;
    private Runnable runnableReconnect;
    private static final int DEFAULT_RETRY_TIMES = 8;
    private int maxRetryTimes = DEFAULT_RETRY_TIMES;

    public void setMaxRetryTimes(int maxRetryTimes) {
        this.maxRetryTimes = maxRetryTimes;
    }

    public PlayerPresenter(P view) {
        super(view);
        handler = new Handler(Looper.getMainLooper());
    }

    public void subscribe(V videoView, Player<V> player) {
        this.player = player;
        player.init(videoView);
        player.setCallBack(this);
        subscribe();
    }

    @Override
    public void subscribe() {
        view.getViewActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    public void startVideo(String url) {
        LogPrint.d(TAG, "startVideo liveUrl:" + liveUrl);
        if (!TextUtils.isEmpty(url)) {
            liveUrl = url;
            player.startVideo(url);
        }
    }

    public void rePlay() {
        startVideo(liveUrl);
        isStopPlay = false;
    }

    public void stopPlay() {
        if (runnableReconnect != null)
            handler.removeCallbacks(runnableReconnect);
        runnableReconnect = null;
        isStopPlay = true;
        player.stopPlay();
    }

    public void clear() {
        liveUrl = "";
        stopPlay();
    }

    protected void reLoadLive(int delay) {
        if (runnableReconnect == null)
            runnableReconnect = new Runnable() {
                @Override
                public void run() {
                    LogPrint.d(TAG, "runnableReconnect");
                    if (isBlockReLoad()) {
                        return;
                    }
                    reConnectTryTimes++;
                    if (reConnectTryTimes > maxRetryTimes) {
                        reLoadLiveFail();
                        return;
                    }
                    handler.postDelayed(runnableReconnect, RECONNECT_TIME_HOST_LEAVE);
                    player.reset();
                    startVideo(liveUrl);
                }
            };
        handler.removeCallbacks(runnableReconnect);
        handler.postDelayed(runnableReconnect, delay);
    }

    protected abstract boolean isBlockReLoad();

    protected abstract void reLoadLiveFail();

    public void runInBackground() {
        player.runInBackground();
    }

    public void runInForeground() {
        player.runInForeground();
    }

    @Override
    public void unSubscribe() {
        player.release();
        if (runnableReconnect != null) {
            handler.removeCallbacks(runnableReconnect);
            runnableReconnect = null;
        }
    }

    @Override
    public void onPrepared() {
        if (runnableReconnect != null)
            handler.removeCallbacks(runnableReconnect);
        reConnectTryTimes = 0;
    }

    @Override
    public void onBufferingStart() {
    }

    @Override
    public void onBufferingEnd() {
    }

    @Override
    public void onRenderingStart() {
    }

    @Override
    public void onCompletion() {
        reConnect();
    }

    @Override
    public void onError() {
        reConnect();
    }

    protected void reConnect() {
        //此时不需要重连
        if (isStopPlay) {
            return;
        }
        reConnectTryTimes++;
        reLoadLive(RECONNECT_TIME_HOST_LEAVE);
    }
}