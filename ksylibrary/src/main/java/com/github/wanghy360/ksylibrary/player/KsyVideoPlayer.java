package com.github.wanghy360.ksylibrary.player;

import com.github.wanghy360.hyvideolibrary.player.Player;
import com.github.wanghy360.hyvideolibrary.player.VideoPlayer;
import com.ksyun.media.player.KSYTextureView;

/**
 * Created by Wanghy on 2017/9/11 0011
 * {@link VideoPlayer}实现类
 */
public class KsyVideoPlayer extends VideoPlayer<KSYTextureView> {

    public KsyVideoPlayer(Player<KSYTextureView> player) {
        super(player);
    }

    @Override
    public void seekTo(long pos) {
        if (getRealPlayer() != null)
            getRealPlayer().seekTo(pos);
    }

    @Override
    public void pause() {
        if (getRealPlayer() != null)
            getRealPlayer().pause();
    }

    @Override
    public void start() {
        if (getRealPlayer() != null)
            getRealPlayer().start();
    }

    @Override
    public long getDuration() {
        if (getRealPlayer() != null)
            return getRealPlayer().getDuration();
        else return 0;
    }

    @Override
    public long getCurrentPosition() {
        if (getRealPlayer() != null)
            return getRealPlayer().getCurrentPosition();
        else return 0;
    }

    @Override
    public boolean isPlaying() {
        if (getRealPlayer() != null)
            return getRealPlayer().isPlaying();
        else
            return false;
    }
}