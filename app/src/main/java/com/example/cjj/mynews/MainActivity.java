package com.example.cjj.mynews;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.example.cjj.mynews.drecyclerviewadatper.DRecyclerViewScrollListener;
import com.example.cjj.mynews.adapter.Numadapter;
import com.example.cjj.mynews.adapter.NumberRVAdapter;
import com.example.cjj.mynews.drecyclerviewadatper.DRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private List<String> dataList;
    private NumberRVAdapter numberRVAdapter;
    private DRecyclerViewAdapter dRecyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataList=new ArrayList<String>();
        for (int i=1;i<=20;i++)
        {
            dataList.add(""+i);
        }

        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.rv_MainA);

        Numadapter numadapter = new Numadapter(this,dataList);
        dRecyclerViewAdapter = new DRecyclerViewAdapter(numadapter);
        View footView = LayoutInflater.from(this).inflate(R.layout.item_foot,null); //底部foot的view
        dRecyclerViewAdapter.addFooterView(footView);   //添加foot到adapter中
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        // recyclerView.setLayoutManager(linearLayoutManager);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new DSpanSizeLookup(dRecyclerViewAdapter, 2));//让grid的头部和尾部，占用2个空间。
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(dRecyclerViewAdapter);

        recyclerView.addOnScrollListener(new DRecyclerViewScrollListener() {
            @Override
            public void onLoadNextPage(RecyclerView view) {
                //加载更多数据。
                loadMoreData();
            }
        });

    }


    private void loadMoreData(){
        int old=dataList.size();

        List<String> newData=new ArrayList<String>();
        for (int j=1;j<=5;j++)
        {
            newData.add("new "+j);
        }
        dataList.addAll(newData);
        dRecyclerViewAdapter.notifyItemRangeInserted(old,dataList.size()-old);
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
