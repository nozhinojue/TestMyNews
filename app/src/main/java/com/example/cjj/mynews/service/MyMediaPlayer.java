package com.example.cjj.mynews.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.cjj.mynews.common.AppRunCache;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by CJJ on 2016/3/5.
 */
public class MyMediaPlayer implements MediaPlayer.OnPreparedListener {
    public static final int State_Play = 0;     //代表播放状态
    public static final int State_Pasue = 1;    //代表暂停状态
    public static final int State_Stop = 2;     //代表停止状态

    private MediaPlayer mediaPlayer;
    private List<String> musicList;//音乐播放列表
    private String musicPath;//音乐路径
    private int play_index=0; //播放的顺序数
    private int Music_State=State_Stop; //当前音乐状态,默认是停止状态。

    private LocalBroadcastManager localBroadcastManager;
    private Intent processUpdateIntent;//进度条更新Intent

    public MyMediaPlayer(Context context){
        mediaPlayer=new MediaPlayer();
        //获取缓存了的music播放列表
        musicList= AppRunCache.musicList;

        localBroadcastManager=LocalBroadcastManager.getInstance(context);
        IntentFilter intentFilter= new IntentFilter();
        intentFilter.addAction(IntentFilterUtils.Music_Play_Pause_Onlcik);
        intentFilter.addAction(IntentFilterUtils.Music_Next);
        intentFilter.addAction(IntentFilterUtils.Music_Pause);
        intentFilter.addAction(IntentFilterUtils.Music_Previous);
        intentFilter.addAction(IntentFilterUtils.Progress_Change);
        localBroadcastManager.registerReceiver(new MediaBroadReciver(), intentFilter);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //一首歌播放完成了
                next(); //播放下一首。
            }
        });

        preparePlay();
    }


    //准备播放
    private void preparePlay(){
        if(musicList==null||musicList.size()==0){
            return;
        }
        musicPath= musicList.get(play_index);
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(musicPath);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //播放
    private void play(){
        switch (Music_State){
            case State_Stop:
                //当前是停止状态，就播放
                mediaPlayer.start();
                Music_State=State_Play;
                sendProcessUpdate();
                break;
            case State_Play:
               //当前播放状态，就暂停。
                pause();
                break;
            case State_Pasue:
                //当前是暂停状态，就回复播放。
                resume();
                break;
        }

    }

    //暂停
    private void pause(){
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            Music_State = State_Pasue;
        }
    }

    //恢复播放
    private void resume(){
        if (Music_State == State_Pasue) {
            mediaPlayer.start();
            Music_State = State_Play;
        }
    }

    //上一首
    private void previous(){
        if(play_index==0){
            //发送Music_Previous_NO广播，说明没有上一首。
            Intent intent = new Intent(IntentFilterUtils.Music_Previous_NO);
            localBroadcastManager.sendBroadcast(intent);
        }else{
            play_index--;
//            Music_State=State_Play;
            preparePlay();
        }
    }

    //下一首
    private void next(){
//        Music_State=State_Play;
        if (play_index == musicList.size() - 1) {
            play_index = 0;
        } else {
            play_index++;
        }

        preparePlay();
    }

    //快进快退到。
    private void seekTo(int seekValue) {
        mediaPlayer.seekTo(seekValue);
    }

    // mediaPlayer缓冲好后调用。
    @Override
    public void onPrepared(MediaPlayer mp) {

        Intent intent = new Intent(IntentFilterUtils.Music_Prepared_OK);
        intent.putExtra("current_playlist_index",play_index);
        intent.putExtra("music_size", mediaPlayer.getDuration());
        localBroadcastManager.sendBroadcast(intent);

        if(Music_State==State_Play)
        {
            mediaPlayer.start();
        }

    }


    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    //广播receiver
    private class MediaBroadReciver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case IntentFilterUtils.Music_Play_Pause_Onlcik:
                    //接受到了播放按钮广播。
                    play();

                    break;
                case IntentFilterUtils.Music_Previous:
                    //接收到上一首按钮广播。
                    previous();
                    break;
                case IntentFilterUtils.Music_Next:
                    //接受到了下一首按钮广播。
                    next();
                    break;
                case IntentFilterUtils.Progress_Change:
                    //接收到了进度改变广播。
                    int seekValue= intent.getIntExtra("seekvalue",0);
                    seekTo(seekValue);
                    break;
            }
        }
    }



    //开启一个线程，发送进度条更新广播.
    private void sendProcessUpdate(){
        //进度条更新的intent
        processUpdateIntent=new Intent(IntentFilterUtils.Progress_Update);
        Timer mTimer = new Timer();
        TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if(Music_State==State_Play) {
                    //当前状态是播放状态的时候，发送更新进度条。
                    processUpdateIntent.putExtra("processVaule", mediaPlayer.getCurrentPosition());
                    localBroadcastManager.sendBroadcast(processUpdateIntent);
                }
            }
        };
        mTimer.schedule(mTimerTask, 0, 10);
    }

}
