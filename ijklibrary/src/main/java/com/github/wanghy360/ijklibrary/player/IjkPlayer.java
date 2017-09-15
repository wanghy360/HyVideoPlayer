package com.github.wanghy360.ijklibrary.player;


import com.github.wanghy360.hyvideolibrary.player.Player;
import com.github.wanghy360.hyvideolibrary.util.LogPrint;
import com.github.wanghy360.ijklibrary.media.IjkVideoView;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by Wanghy on 2017/9/15
 * ijkplayer，{@link Player}的实现类。
 */
public class IjkPlayer extends Player<IjkVideoView> implements IMediaPlayer.OnCompletionListener,
        IMediaPlayer.OnPreparedListener,
        IMediaPlayer.OnErrorListener,
        IMediaPlayer.OnInfoListener {
    private static final String TAG = "IjkPlayer";
    private IjkVideoView mVideoView;

    @Override
    public void init(IjkVideoView realPlayer) {
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        this.mVideoView = realPlayer;
        mVideoView.setKeepScreenOn(true);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnInfoListener(this);
        mVideoView.setOnErrorListener(this);
    }

    @Override
    public IjkVideoView getRealPlayer() {
        return mVideoView;
    }

    @Override
    public void startVideo(String liveUrl) {
        if (mVideoView != null) {
            try {
                LogPrint.d(TAG, "startVideo liveUrl:" + liveUrl);
                mVideoView.setVideoPath(liveUrl);
                mVideoView.start();
            } catch (Exception e) {
                e.printStackTrace();
                LogPrint.e(TAG, e.getMessage());
            }
        }
    }

    @Override
    public void stopPlay() {
        if (mVideoView != null) {
            LogPrint.d(TAG, "stopPlayback");
            mVideoView.stopPlayback();
            mVideoView.release(true);
        }
    }

    @Override
    public void runInBackground() {
    }

    @Override
    public void runInForeground() {
    }

    @Override
    public void release() {
        if (mVideoView != null) {
            mVideoView.setVideoURI(null);
            mVideoView.stopPlayback();
            mVideoView.release(true);
            mVideoView.setMediaController(null);
            mVideoView.setOnErrorListener(null);
            mVideoView.setOnCompletionListener(null);
            mVideoView.setOnInfoListener(null);
            mVideoView.setOnPreparedListener(null);
        }
        IjkMediaPlayer.native_profileEnd();
    }

    @Override
    public void reset() {
        if (mVideoView != null) {
            mVideoView.release(true);
        }
    }

    @Override
    public void onPrepared(IMediaPlayer mp) {//setOnPreparedListener
        LogPrint.d(TAG, "OnPrepared");
        callback.onPrepared();
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {//setOnInfoListener
        switch (i) {
            case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                LogPrint.d(TAG, "Buffering Start.");
                callback.onBufferingStart();
                break;
            case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                LogPrint.d(TAG, "Buffering End.");
                callback.onBufferingEnd();
                break;
            case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
                break;
            case IMediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                break;
            case IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                break;
            case IMediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                break;
            case IMediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE:
                break;
            case IMediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT:
                break;
            case IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                break;
            case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                LogPrint.d(TAG, "Video Rendering Start");
                callback.onRenderingStart();
                break;
        }

        return false;
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
