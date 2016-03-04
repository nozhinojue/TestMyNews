package com.example.cjj.mynews.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cjj.mynews.R;
import com.example.cjj.mynews.service.MyMusicService;


public class MusicFragment extends Fragment implements View.OnClickListener {
    private ImageView ivPlay;
    private ImageView ivPre;
    private ImageView ivNext;

    public MusicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_music, container, false);
        ivPlay= (ImageView) view.findViewById(R.id.iv_play_musicFg);
        ivPre= (ImageView) view.findViewById(R.id.iv_pre_musicFg);
        ivNext= (ImageView) view.findViewById(R.id.iv_next_musicFg);

        ivPlay.setOnClickListener(this);
        ivPre.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_play_musicFg:
                Intent intent = new Intent(getContext(), MyMusicService.class);
                intent.putExtra("playing",true);
                getActivity().startService(intent);
                Toast.makeText(getContext(), "bofang", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_pre_musicFg:
                Intent intent0 = new Intent(getContext(), MyMusicService.class);
                getActivity().stopService(intent0);
                Toast.makeText(getContext(), "tingzhi", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_next_musicFg:
                Intent intent1 = new Intent(getContext(), MyMusicService.class);
                intent1.putExtra("playing",false);
                getActivity().startService(intent1);
                Toast.makeText(getContext(), "zanting", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
