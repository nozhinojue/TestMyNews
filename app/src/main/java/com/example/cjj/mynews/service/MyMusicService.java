package com.example.cjj.mynews.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by CJJ on 2016/3/4.
 */
public class MyMusicService  extends Service {
    private MyMediaPlayer myMediaPlayer;
    @Override
    public void onCreate() {
        super.onCreate();
        myMediaPlayer=new MyMediaPlayer(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myMediaPlayer.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
