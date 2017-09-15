package com.github.wanghy360.hyvideolibrary.player;

/**
 * Created by Wanghy on 2017/9/11
 * 播放器抽象类，定义了播放器的行为方法
 *
 * @param <T> 播放器View
 */
public abstract class Player<T> {
    protected Callback callback;

    /**
     * 初始化
     *
     * @param realPlayer 播放器View
     */
    public abstract void init(T realPlayer);

    /**
     * @return 播放器View
     */
    public abstract T getRealPlayer();

    /**
     * 开始播放
     *
     * @param liveUrl 视频Url
     */
    public abstract void startVideo(String liveUrl);

    /**
     * 结束播放
     */
    public abstract void stopPlay();

    /**
     * 后台播放
     */
    public abstract void runInBackground();

    /**
     * 前台播放
     */
    public abstract void runInForeground();

    /**
     * 释放资源
     */
    public abstract void release();

    /**
     * reset播放
     */
    public abstract void reset();

    /**
     * 此方法务必调用
     */
    public void setCallBack(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        /**
         * 播放准备
         */
        void onPrepared();

        /**
         * 缓冲开始
         */
        void onBufferingStart();

        /**
         * 缓冲结束
         */
        void onBufferingEnd();

        /**
         * 视频展现开始
         */
        void onRenderingStart();

        /**
         * 播放结束
         */
        void onCompletion();

        /**
         * 播放出错
         */
        void onError();
    }
}
