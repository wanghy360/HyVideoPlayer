package com.github.wanghy360.ijklibrary.player;

import com.github.wanghy360.hyvideolibrary.player.Player;
import com.github.wanghy360.hyvideolibrary.player.VideoPlayer;
import com.github.wanghy360.ijklibrary.media.IjkVideoView;

/**
 * Created by Wanghy on 2017/9/15
 */
public class IjkVideoPlayer extends VideoPlayer<IjkVideoView> {

    public IjkVideoPlayer(Player<IjkVideoView> player) {
        super(player);
    }
    @Override
    public void seekTo(long pos) {
        if (getRealPlayer() != null)
            getRealPlayer().seekTo((int) pos);
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
