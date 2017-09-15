package com.github.wanghy360.hyvideolibrary.player;

/**
 * Created by Wanghy on 2017/9/11
 * 视频播放器抽象类，增加了{@link Player}的一些行为方法，
 * 故使用装饰模式
 */
public abstract class VideoPlayer<T> extends Player<T> {
    private Player<T> player;

    /**
     * @param player 被装饰的类
     */
    public VideoPlayer(Player<T> player) {
        this.player = player;
    }

    @Override
    public void init(T realPlayer) {
        player.init(realPlayer);
    }

    @Override
    public T getRealPlayer() {
        return player.getRealPlayer();
    }

    @Override
    public void startVideo(String liveUrl) {
        player.startVideo(liveUrl);
    }

    @Override
    public void stopPlay() {
        player.stopPlay();
    }

    @Override
    public void runInBackground() {
        player.runInBackground();
    }

    @Override
    public void runInForeground() {
        player.runInForeground();
    }

    @Override
    public void release() {
        player.release();
    }

    @Override
    public void reset() {
        player.reset();
    }

    @Override
    public void setCallBack(Callback callback) {
        player.setCallBack(callback);
    }

    // *******************************分割线，下面是VideoPlayer自有的方法*********************************

    public abstract void seekTo (long pos);

    public abstract void pause();

    public abstract void start();

    public abstract long getDuration();

    public abstract long getCurrentPosition();

    public abstract boolean isPlaying();
}