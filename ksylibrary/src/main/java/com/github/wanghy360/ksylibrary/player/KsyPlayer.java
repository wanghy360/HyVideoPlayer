package com.github.wanghy360.ksylibrary.player;


import com.github.wanghy360.hyvideolibrary.player.Player;
import com.github.wanghy360.hyvideolibrary.util.LogPrint;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.ksyun.media.player.KSYTextureView;

import java.io.IOException;

/**
 * Created by Wanghy on 2017/9/11
 * 金山播放器，{@link Player}的实现类。
 */
public class KsyPlayer extends Player<KSYTextureView> implements IMediaPlayer.OnCompletionListener,
        IMediaPlayer.OnPreparedListener,
        IMediaPlayer.OnInfoListener,
        IMediaPlayer.OnVideoSizeChangedListener,
        IMediaPlayer.OnErrorListener,
        IMediaPlayer.OnMessageListener {
    private static final String TAG = "KsyPlayer";
    private KSYTextureView mVideoView;
    private int mVideoWidth = 0;
    private int mVideoHeight = 0;

    @Override
    public void init(KSYTextureView realPlayer) {
        this.mVideoView = realPlayer;
        mVideoView.setKeepScreenOn(true);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnInfoListener(this);
        mVideoView.setOnVideoSizeChangedListener(this);
        mVideoView.setOnErrorListener(this);
        mVideoView.setOnMessageListener(this);
        mVideoView.setScreenOnWhilePlaying(true);
        mVideoView.setTimeout(5, 30);
        mVideoView.setBufferTimeMax(2);//2s
        mVideoView.setBufferSize(15);//15M
    }

    @Override
    public KSYTextureView getRealPlayer() {
        return mVideoView;
    }

    @Override
    public void startVideo(String liveUrl) {
        if (mVideoView != null) {
            try {
                LogPrint.d(TAG, "startVideo liveUrl:" + liveUrl);
                mVideoView.setDataSource(liveUrl);
                mVideoView.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stopPlay() {
        if (mVideoView != null) {
            LogPrint.d(TAG, "stopPlayback");
            mVideoView.reset();
            mVideoView.stop();
        }
    }

    @Override
    public void runInBackground() {
        if (mVideoView != null) {
            mVideoView.runInBackground(true);
        }
    }

    @Override
    public void runInForeground() {
        if (mVideoView != null) {
            mVideoView.runInForeground();
        }
    }

    @Override
    public void release() {
        if (mVideoView != null) {
            mVideoView.stop();
            mVideoView.release();
            mVideoView.setOnPreparedListener(null);
            mVideoView.setOnCompletionListener(null);
            mVideoView.setOnInfoListener(null);
            mVideoView.setOnVideoSizeChangedListener(null);
            mVideoView.setOnErrorListener(null);
            mVideoView.setOnMessageListener(null);
            mVideoView = null;
        }
    }

    @Override
    public void reset() {
        if (mVideoView != null) {
            mVideoView.reset();
        }
    }

    @Override
    public void onPrepared(com.ksyun.media.player.IMediaPlayer mp) {//setOnPreparedListener
        LogPrint.d(TAG, "OnPrepared");
        callback.onPrepared();

        mVideoWidth = mVideoView.getVideoWidth();
        mVideoHeight = mVideoView.getVideoHeight();

        // Set Video Scaling Mode
        mVideoView.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);

        //start player
        mVideoView.start();
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {//setOnInfoListener
        switch (i) {
            case KSYMediaPlayer.MEDIA_INFO_BUFFERING_START:
                LogPrint.d(TAG, "Buffering Start.");
                callback.onBufferingStart();
                break;
            case KSYMediaPlayer.MEDIA_INFO_BUFFERING_END:
                LogPrint.d(TAG, "Buffering End.");
                callback.onBufferingEnd();
                break;
            case KSYMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                LogPrint.d(TAG, "Audio Rendering Start");
                break;
            case KSYMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                LogPrint.d(TAG, "Video Rendering Start");
                callback.onRenderingStart();
                break;
            case KSYMediaPlayer.MEDIA_INFO_RELOADED:
                LogPrint.d(TAG, "Succeed to reload video.");
                break;
            case KSYMediaPlayer.MEDIA_INFO_SUGGEST_RELOAD://重连
                LogPrint.d(TAG, "suggest reload");
                break;
        }
        return false;
    }

    @Override
    public void onMessage(IMediaPlayer iMediaPlayer, String name, String info, double number) {//setOnMessageListener
        LogPrint.d(TAG, "name:" + name + ",info:" + info + ",number:" + number);
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sarNum, int sarDen) {
        if (mVideoWidth > 0 && mVideoHeight > 0) {
            if (width != mVideoWidth || height != mVideoHeight) {
                mVideoWidth = mp.getVideoWidth();
                mVideoHeight = mp.getVideoHeight();

                if (mVideoView != null)
                    mVideoView.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            }
        }
    }

    @Override
    public void onCompletion(IMediaPlayer mp) {//setOnCompletionListener
        LogPrint.e(TAG, "onCompletion");
        callback.onCompletion();
    }

    @Override
    public boolean onError(IMediaPlayer mp, int what, int extra) {
        LogPrint.e(TAG, "OnErrorListener, Error:" + what + ",extra:" + extra);
        callback.onError();
        return false;
    }
}
