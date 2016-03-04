package com.example.cjj.mynews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cjj.mynews.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJJ on 2016/3/1.
 */
public class NumberRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_VIEW_TYPE_HEADER = 1; //头部标识
    private static final int ITEM_VIEW_TYPE_FOOTER = 2;  //尾部标识
    private static final int ITEM_VIEW_TYPE_ITEM = 3;   //item标识

    private View mHeaderView;
    private View mFooterView;

    private Context mContext;
    private List<String> lists;

    public NumberRVAdapter(Context context,View headerView,View footerView,List<String> dataList){
        mHeaderView=headerView;
        mFooterView=footerView;

        mContext=context;
        lists=dataList;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==ITEM_VIEW_TYPE_HEADER){
            StaticViewHolder headholder= new StaticViewHolder(mHeaderView);
            return headholder;
        }else if(viewType==ITEM_VIEW_TYPE_FOOTER){
            StaticViewHolder footholder= new StaticViewHolder(mFooterView);
            return footholder;
        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_item, parent, false);
        NumViewHolder holder= new NumViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(isHeader(position) || isFooter(position)){
            return;
        }

        final String valueStr=lists.get(position - 1);//第一个位置为header,从第二个开始是item数据。
        ((NumViewHolder)holder).tvTitle.setText(valueStr);
        ((NumViewHolder)holder).tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, valueStr, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size()+2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_VIEW_TYPE_HEADER;
        } else if(position==lists.size()+1){
            return ITEM_VIEW_TYPE_FOOTER;
        }else {
            return ITEM_VIEW_TYPE_ITEM;
        }
    }

    //判断是否为头部.
    public boolean isHeader(int position){
        return position==0;     //位置为0时是header
    }

    //判断是否为尾部
    public boolean isFooter(int position){
        return position==lists.size()+1;
    }
}

//item的viewholder
class NumViewHolder extends RecyclerView.ViewHolder{
    public TextView tvTitle;
    public NumViewHolder(View itemView) {
        super(itemView);
        tvTitle= (TextView) itemView.findViewById(R.id.tv_title_rvItem);
    }
}

//head,foot的viewholder
class StaticViewHolder extends RecyclerView.ViewHolder{
    public StaticViewHolder(View itemView) {
        super(itemView);
    }
}

