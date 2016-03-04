package com.example.cjj.mynews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cjj.mynews.R;

import java.util.List;

/**
 * Created by CJJ on 2016/3/2.
 */
public class Numadapter extends RecyclerView.Adapter<NumVH> {
    private Context mContext;
    private List<String> lists;

    public Numadapter(Context context,List<String> dataList){
        mContext=context;
        lists=dataList;
    }

    @Override
    public NumVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_item, parent, false);
        NumVH holder= new NumVH(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(NumVH holder, int position) {

        final String valueStr=lists.get(position);
        holder.tvTitle.setText(valueStr);
        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, valueStr, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}


class NumVH extends RecyclerView.ViewHolder{
    public TextView tvTitle;
    public NumVH(View itemView) {
        super(itemView);
        tvTitle= (TextView) itemView.findViewById(R.id.tv_title_rvItem);
    }
}

