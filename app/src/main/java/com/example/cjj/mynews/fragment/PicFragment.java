package com.example.cjj.mynews.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.cjj.mynews.R;
import com.example.cjj.mynews.activity.PicDetailActivity;
import com.example.cjj.mynews.activity.WebViewActivity;
import com.example.cjj.mynews.adapter.PicFgRVAdapter;
import com.example.cjj.mynews.model.PicTypeData;
import com.example.cjj.mynews.okhttp.MyOkHttp;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class PicFragment extends Fragment implements PicFgRVAdapter.ItemOnClickListener {
    private RecyclerView mRecyclerView;
    private List<PicTypeData.ResEntity.CategoryEntity> lists;
    private PicFgRVAdapter picFgRVAdapter;
    public PicFragment() {
        lists=new ArrayList<PicTypeData.ResEntity.CategoryEntity>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pic, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_picFg);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        mRecyclerView.setLayoutManager(gridLayoutManager);  //设置网格布局，每行显示2个。


        getData();
        return view;
    }

    //获取数据
    private void getData() {
        new MyOkHttp().getPicData(new MyOkHttp.MyOkHttpCallBack() {
            @Override
            public void Successed(String jsonResult) {
                Gson gson = new Gson();
                PicTypeData picTypeData = gson.fromJson(jsonResult, PicTypeData.class);

                setData(picTypeData);
            }

            @Override
            public void Failure(Call call, IOException e) {
                Toast.makeText(getActivity(), "请求网络失败，请查看！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData(PicTypeData data){
        lists.addAll(data.getRes().getCategory());
        picFgRVAdapter = new PicFgRVAdapter(getContext(),lists);
        picFgRVAdapter.setmItemOnclickListener(this);
        mRecyclerView.setAdapter(picFgRVAdapter);

    }

    //item的点击事件
    @Override
    public void onClick(int position, View view) {
        PicTypeData.ResEntity.CategoryEntity caterEntity= lists.get(position);
        String id =caterEntity.getId();
        Intent intent = new Intent(getActivity(), PicDetailActivity.class);
        intent.putExtra("typeId", id);
        intent.putExtra("typeName", caterEntity.getName());
        startActivity(intent);
        //Toast.makeText(getActivity(), "进入详细页面。id="+caterEntity.getId(), Toast.LENGTH_SHORT).show();
    }
}
