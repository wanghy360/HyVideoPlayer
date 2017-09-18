package com.github.wanghy360.player.util;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.telephony.TelephonyManager;

/**
 * Created by Wanghy on 2017/8/4
 * 来电关闭播放器声音
 */
public class RingListenerTool {
    private AudioManager mAudioManager;
    private int curVolume = 0;
    private PhoneStateChangedReceiver phoneStateChangedReceiver;

    public void registerReceiver(Context context) {
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        IntentFilter filter = new IntentFilter();
        filter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        phoneStateChangedReceiver = new PhoneStateChangedReceiver();
        context.registerReceiver(phoneStateChangedReceiver, filter);
    }

    public void unregisterReceiver(Context context) {
        if (phoneStateChangedReceiver != null) {
            context.unregisterReceiver(phoneStateChangedReceiver);
        }
        phoneStateChangedReceiver = null;
        mAudioManager = null;
    }

    public class PhoneStateChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TelephonyManager.ACTION_PHONE_STATE_CHANGED.equals(action)) {
                String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {
                    updateVolume(false);
                } else if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state)) {
                    updateVolume(true);
                } else if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
                    updateVolume(true);
                }
            } else if (Intent.ACTION_NEW_OUTGOING_CALL.equals(action)) {
                updateVolume(true);
            }
        }
    }

    private void updateVolume(boolean hideMusicStream) {
        if (hideMusicStream) {
            curVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        } else {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, curVolume, 0);
        }
    }
}