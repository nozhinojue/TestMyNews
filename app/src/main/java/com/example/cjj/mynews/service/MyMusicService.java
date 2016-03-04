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
    private MediaPlayer mediaPlayer;
    private String urlPath;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer =new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        urlPath="http://m5.file.xiami.com/207/121207/1504099860/1773296543_15312352_l.mp3?auth_key=007c789b985c93fb2065fcdd69948c7c-1457136000-0-null";
        try {
            mediaPlayer.setDataSource(urlPath);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        boolean playing = intent.getBooleanExtra("playing", false);
        if (playing) {
            mediaPlayer.start();
            Log.i("MyMusicService"," mediaPlayer.start();");
        } else {
            mediaPlayer.pause();
            Log.i("MyMusicService", " mediaPlayer.pause();");
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
