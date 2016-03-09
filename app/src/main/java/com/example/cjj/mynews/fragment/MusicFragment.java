package com.example.cjj.mynews.fragment;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cjj.mynews.R;
import com.example.cjj.mynews.common.AppRunCache;
import com.example.cjj.mynews.imageloader.ImageLoaderSetting;
import com.example.cjj.mynews.model.MusicMainData;
import com.example.cjj.mynews.okhttp.MyOkHttp;
import com.example.cjj.mynews.service.IntentFilterUtils;
import com.example.cjj.mynews.service.MyMusicService;
import com.example.cjj.mynews.utils.FastBlur;
import com.example.cjj.mynews.utils.TimeUtils;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


public class MusicFragment extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
   private RelativeLayout rlBg;
    private ImageView ivPlay;
    private ImageView ivPre;
    private ImageView ivNext;
    private ImageView ivIcon;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private TextView tvSongName;
    private TextView tvSonger;
    private SeekBar sbProcess;

    private int musicType=1;//音乐类型
    private List<MusicMainData.DataEntity.SongsEntity> lists=new ArrayList<>();
    private Intent serviceIntent;   //服务intent
    private LocalBroadcastManager mLocalBroadcastManager; //广播mananger
    private int current_index_playing;//当前播放音乐在list中的index
    private int musicDuration;  //歌曲时长
    private boolean isPlayState=false;//是否播放状态

    private ObjectAnimator ivIconAnimator;//ivIcon动画对象
    private long animatorCurrentTime;//动画当前时间
    private ObjectAnimator rlBgAnimator; //rlBg动画对象

    private boolean sbIsChange=false; //seekBar是否改变标识


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_music, container, false);

        rlBg= (RelativeLayout) view.findViewById(R.id.rl_bg_musicFg);//背景
        //透明度动画
        rlBgAnimator = ObjectAnimator.ofFloat(rlBg, "alpha", 0.4f, 0.9f);
        rlBgAnimator.setDuration(3000);
        rlBgAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        tvStartTime= (TextView) view.findViewById(R.id.tv_start_musicFg);   //歌曲当前时间
        tvEndTime= (TextView) view.findViewById(R.id.tv_end_musicFg);       //歌曲结束时间
        tvSongName= (TextView) view.findViewById(R.id.tv_songname_MusicFg); //歌名
        tvSonger= (TextView) view.findViewById(R.id.tv_songername_MusicFg); //歌手名
        sbProcess= (SeekBar) view.findViewById(R.id.sb_musicFg);   //进度条
        sbProcess.setOnSeekBarChangeListener(this);

        ivIcon= (ImageView) view.findViewById(R.id.iv_icon_musicFg);    //圆的图片
        ivIconAnimator= ObjectAnimator.ofFloat(ivIcon, "rotation", 0, 360); //旋转360°动画
        ivIconAnimator.setDuration(15000);    //动画持续时间
        ivIconAnimator.setInterpolator(new LinearInterpolator());//动画以匀速的速率改变
        ivIconAnimator.setRepeatCount(ValueAnimator.INFINITE);//设置无限重复

        ivPlay= (ImageView) view.findViewById(R.id.iv_play_musicFg);    //play按钮
        ivPre= (ImageView) view.findViewById(R.id.iv_pre_musicFg);      //上一首按钮
        ivNext= (ImageView) view.findViewById(R.id.iv_next_musicFg);    //下一首按钮

        ivPlay.setOnClickListener(this);
        ivPre.setOnClickListener(this);
        ivNext.setOnClickListener(this);

        initData();

        return view;
    }


    /**
     * 获取播放列表数据，保存起来，供service使用。
     */
    private void initData() {
        //初始化服务intent
        serviceIntent = new Intent(getContext(), MyMusicService.class);

        //注册一个广播，来监听server发来的消息。
        mLocalBroadcastManager=LocalBroadcastManager.getInstance(getContext());
        IntentFilter intentFilter=new IntentFilter(); //广播IntentFilter
        intentFilter.addAction(IntentFilterUtils.Music_Prepared_OK);
        intentFilter.addAction(IntentFilterUtils.Music_Pause);
        intentFilter.addAction(IntentFilterUtils.Music_Resume);
        intentFilter.addAction(IntentFilterUtils.Progress_Update);
        intentFilter.addAction(IntentFilterUtils.Position);
        intentFilter.addAction(IntentFilterUtils.Music_Previous_NO);

        mLocalBroadcastManager.registerReceiver(new MyMusicBroadReceiver(), intentFilter);

        getData();

    }

    private void getData(){
        new MyOkHttp().getMusicData(musicType, new MyOkHttp.MyOkHttpCallBack() {
            @Override
            public void Successed(String jsonResult) {
                Gson gson = new Gson();
                MusicMainData musicMainData = gson.fromJson(jsonResult, MusicMainData.class);
                if (musicMainData.getData() != null && musicMainData.getData().getSongs() != null && musicMainData.getData().getSongs().size() > 0) {
                    AppRunCache.musicList.clear();
                    for (int i = 0; i < musicMainData.getData().getSongs().size(); i++) {
                        if (musicMainData.getData().getSongs().get(i).getUrlList() != null && musicMainData.getData().getSongs().get(i).getUrlList().size() > 0) {
                            lists.add(musicMainData.getData().getSongs().get(i));
                            AppRunCache.musicList.add(lists.get(i).getUrlList().get(0).getUrl());
                        }
                    }
                    //启动服务，播放AppRunCache.musicList中的音乐
                    getActivity().startService(serviceIntent);
                    //setSongMsg(lists.get(current_index_playing));

                }
            }

            @Override
            public void Failure(Call call, IOException e) {
                Toast.makeText(getActivity(), "获取音乐数据失败，请查看！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //设置界面上的歌曲名和歌手名
    public void setSongMsg(MusicMainData.DataEntity.SongsEntity songsEntity){
        tvSongName.setText(songsEntity.getName());
        tvSonger.setText(songsEntity.getSingerName());
    }

    //设置界面上的icon图像
    public void setIconImage(String urlPath){
        ImageLoader.getInstance().displayImage(urlPath, ivIcon, ImageLoaderSetting.defaultOptions, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                super.onLoadingFailed(imageUri, view, failReason);
                String message = null;
                switch (failReason.getType()) {
                    case IO_ERROR:
                        message = "下载错误";
                        break;
                    case DECODING_ERROR:
                        message = "图片无法显示";
                        break;
                    case NETWORK_DENIED:
                        message = "网络有问题，无法下载";
                        break;
                    case OUT_OF_MEMORY:
                        message = "图片太大无法显示";
                        break;
                    case UNKNOWN:
                        message = "未知的错误";
                        break;
                }
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (loadedImage != null) {
                    Bitmap blurBitmap= FastBlur.fastblur(getContext(), loadedImage, 20);//模糊处理
                    BitmapDrawable bd=new BitmapDrawable(blurBitmap);
                    setRlBg(bd);//设置背景图片
                }
            }
        });

    }

    //控制iconImage的动画开启或关闭
    public void startIconAnimator(boolean flag){
        if(flag)
        {
            //开启动画
            ivIconAnimator.start();
            ivIconAnimator.setCurrentPlayTime(animatorCurrentTime);
        }else {
            //停止动画
            animatorCurrentTime=ivIconAnimator.getCurrentPlayTime();
            ivIconAnimator.cancel();
        }
    }

    //设置背景图片
    public void setRlBg(Drawable drawable){
        rlBg.setBackgroundDrawable(drawable);
        rlBgAnimator.start();
    }

    //设置播放按钮图片
    public void setIvPlayImg(){
        if(isPlayState){
            ivPlay.setImageResource(R.mipmap.btn_playback_pause);
        }else {
            ivPlay.setImageResource(R.mipmap.btn_playback_play);
        }
    }

    public void setTVStartTime(int size){
        tvStartTime.setText(TimeUtils.transformationMS(size));
    }
    //设置歌曲持续时间在界面中显示。
    public void setTVEndTime(int size){
        tvEndTime.setText(TimeUtils.transformationMS(size));
    }

    //设置进度条的值
    public void setSbProgress(int progress) {
        if (progress < 0) return;
        sbProcess.setProgress(progress);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_play_musicFg:
                //发送播放广播。
                mLocalBroadcastManager.sendBroadcast(new Intent(IntentFilterUtils.Music_Play_Pause_Onlcik));

                if(!isPlayState){
                    isPlayState=true;
                }else {
                    isPlayState=false;
                }
                setIvPlayImg(); //设置IconImg的图片
                startIconAnimator(isPlayState);//开启IconImg动画
                break;
            case R.id.iv_pre_musicFg:
                //发送上一首广播。
                mLocalBroadcastManager.sendBroadcast(new Intent(IntentFilterUtils.Music_Previous));
                break;
            case R.id.iv_next_musicFg:
                //发送下一首广播。
                mLocalBroadcastManager.sendBroadcast(new Intent(IntentFilterUtils.Music_Next));

                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        //seekBar的值改变调用
        if(sbIsChange){
            //改变起始时间
            setTVStartTime(seekBar.getProgress() * musicDuration / 100);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //开始拖seekBar
        sbIsChange=true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //停止拖seekBar
        if(sbIsChange){
            //发送进度改变广播，让server去处理
            Intent intent= new Intent(IntentFilterUtils.Progress_Change);
            intent.putExtra("seekvalue",seekBar.getProgress() * musicDuration / 100);
            mLocalBroadcastManager.sendBroadcast(intent);

            sbIsChange=false;
        }
    }


    //定义一个继承了BroadcastReceiver的类，来处理监听到的消息。
    private class MyMusicBroadReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case IntentFilterUtils.Music_Prepared_OK:
                    //缓冲完成了
                    int playlistindex=intent.getIntExtra("current_playlist_index",-1);//当前播放歌曲在list中的index
                    musicDuration=  intent.getIntExtra("music_size",0);//歌曲持续时间。
                    setTVEndTime(musicDuration);//显示到界面上。
                    if(playlistindex!=-1){
                        current_index_playing=playlistindex;
                        MusicMainData.DataEntity.SongsEntity songEntity=lists.get(current_index_playing);
                        setSongMsg(songEntity);
                        setIconImage(songEntity.getPicUrl());
                    }
                    break;
                case IntentFilterUtils.Progress_Update:
                    if(!sbIsChange) {
                        //seekBar没有拖动时，更新进度条
                        int progress = intent.getIntExtra("processVaule", -1);
                        if (progress == -1) {
                            return;
                        }
                        setTVStartTime(progress);   //设置起始时间文本
                        setSbProgress(progress * 100 / musicDuration);  //设置seekbar
                    }
                    break;
                case IntentFilterUtils.Music_Previous_NO:
                    Toast.makeText(getContext(), "这是第一首歌，没有上一首！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

}
