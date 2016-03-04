package com.example.cjj.mynews.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cjj.mynews.R;
import com.example.cjj.mynews.adapter.PicDetailRVAdapter;
import com.example.cjj.mynews.drecyclerviewadatper.DRecyclerViewAdapter;
import com.example.cjj.mynews.drecyclerviewadatper.DRecyclerViewScrollListener;
import com.example.cjj.mynews.model.PicTypeData;
import com.example.cjj.mynews.model.PicTypeDetailData;
import com.example.cjj.mynews.okhttp.MyOkHttp;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class PicDetailActivity extends Activity {
    private RecyclerView recyclerView;
    private  DRecyclerViewAdapter dRecyclerViewAdapter;
    private List<PicTypeDetailData.ResEntity.WallpaperEntity> listData;
    private int skipNum;
    private String idStr;   //图片类别id
    private String typeName;//图片类别名称。
    private boolean isOver; //是否还有更多数据。
    PicDetailRVAdapter picDetailRVAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_detail);

        idStr=getIntent().getStringExtra("typeId");   //获取启动当前intent中的id值。
        typeName=getIntent().getStringExtra("typeName");

        ImageView imageView= (ImageView) findViewById(R.id.iv_actionbar);
        imageView.setImageResource(R.mipmap.back_icon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView textView= (TextView) findViewById(R.id.tv_actionbar);  //标题
        textView.setText(typeName);

        listData=new ArrayList<PicTypeDetailData.ResEntity.WallpaperEntity>();
        recyclerView = (RecyclerView) findViewById(R.id.rv_picDetailA);

        picDetailRVAdapter = new PicDetailRVAdapter(this,listData);
        picDetailRVAdapter.setmItemOnclickListener(new PicDetailRVAdapter.ItemOnClickListener() {
            @Override
            public void onClick(int position, View view) {
                Intent intent = new Intent(PicDetailActivity.this, ImageDetailActivity.class);
                intent.putExtra("imgurl", listData.get(position).getImg());
                intent.putExtra("imgname", listData.get(position).getId());
                startActivity(intent);
            }
        });
        dRecyclerViewAdapter = new DRecyclerViewAdapter(picDetailRVAdapter);
        View footView = LayoutInflater.from(this).inflate(R.layout.item_foot,null); //底部foot的view
        dRecyclerViewAdapter.addFooterView(footView);   //添加foot到adapter中

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);    //设置网格布局，每行显示2个。
        gridLayoutManager.setSpanSizeLookup(new DSpanSizeLookup(dRecyclerViewAdapter, 2));  //让grid的头部和尾部，占用2个单位空间。
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(dRecyclerViewAdapter);
        recyclerView.addOnScrollListener(new DRecyclerViewScrollListener() {
            @Override
            public void onLoadNextPage(RecyclerView view) {
                getMore();//加载更多
            }
        });

        InitData();
    }

    //初始化数据。
    private void InitData(){
        skipNum=30;
        isOver=false;
        listData.clear();
        dRecyclerViewAdapter.notifyDataSetChanged();
//        picDetailRVAdapter.notifyDataSetChanged();
        getData();
    }

    //加载更多数据。
    private void getMore(){
        if(isOver){
            return;//没有更多数据了。
        }
        skipNum +=30;
        getData();
    }

    //获取数据
    private void getData() {
        new MyOkHttp().getPicDetailData(idStr, skipNum, new MyOkHttp.MyOkHttpCallBack() {
            @Override
            public void Successed(String jsonResult) {
                Gson gson = new Gson();
                PicTypeDetailData picTypeDetailData = gson.fromJson(jsonResult, PicTypeDetailData.class);

                setData(picTypeDetailData);
            }

            @Override
            public void Failure(Call call, IOException e) {

            }
        });
    }


    private void setData(PicTypeDetailData data){
        if (data.getRes() != null && data.getRes().getWallpaper() != null && data.getRes().getWallpaper().size() != 0) {
            if(skipNum==30){
                //第一页
                listData.addAll(data.getRes().getWallpaper());
                dRecyclerViewAdapter.notifyDataSetChanged();
            }else {
                int old = listData.size();
                listData.addAll(data.getRes().getWallpaper());
                dRecyclerViewAdapter.notifyItemRangeInserted(old, listData.size() - old);
            }

        }else{
            Toast.makeText(PicDetailActivity.this, "没有更多数据了。", Toast.LENGTH_SHORT).show();
        }
    }

    //让grid的头部和尾部，占用2个单位空间。
    private class DSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
        private DRecyclerViewAdapter adapter;
        private int mSpanSize = 1;

        public DSpanSizeLookup(DRecyclerViewAdapter adapter, int spanSize) {
            this.adapter = adapter;
            this.mSpanSize = spanSize;
        }

        @Override
        public int getSpanSize(int position) {
            boolean isHeaderOrFooter = adapter.isHeader(position) || adapter.isFooter(position);
            return isHeaderOrFooter ? mSpanSize : 1;
        }
    }
}
