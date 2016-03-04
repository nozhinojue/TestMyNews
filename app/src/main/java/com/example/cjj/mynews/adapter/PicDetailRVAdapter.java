package com.example.cjj.mynews.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.cjj.mynews.R;
import com.example.cjj.mynews.imageloader.ImageLoaderSetting;
import com.example.cjj.mynews.model.PicTypeData;
import com.example.cjj.mynews.model.PicTypeDetailData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

/**
 * Created by CJJ on 2016/2/26.
 */
public class PicDetailRVAdapter extends RecyclerView.Adapter<PicDetailViewHolder> {

    private Context mContext;
    private List<PicTypeDetailData.ResEntity.WallpaperEntity> mLists;
    private ItemOnClickListener mItemOnclickListener;

    public PicDetailRVAdapter(Context context,List<PicTypeDetailData.ResEntity.WallpaperEntity> lists) {
        mContext=context;
        mLists=lists;
    }

    @Override
    public PicDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PicDetailViewHolder holder = new PicDetailViewHolder(LayoutInflater.from(mContext).inflate(R.layout.picdetial_rv_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final PicDetailViewHolder holder, int position) {
        holder.setData(mLists.get(position));

        // 如果设置了回调，则设置点击事件
        if(mItemOnclickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=holder.getLayoutPosition(); //当前item的位置。
                    mItemOnclickListener.onClick(pos,holder.itemView);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    //item点击事件接口
    public interface ItemOnClickListener{
        void onClick(int position, View view);
    }

    public void setmItemOnclickListener(ItemOnClickListener mItemOnclickListener) {
        this.mItemOnclickListener = mItemOnclickListener;
    }

}


class PicDetailViewHolder extends RecyclerView.ViewHolder{
    ImageView ivImg;
    public PicDetailViewHolder(View itemView) {
        super(itemView);
        ivImg= (ImageView) itemView.findViewById(R.id.iv_img_picDetailA_item);
    }

    public void setData(PicTypeDetailData.ResEntity.WallpaperEntity data) {
        ImageLoader.getInstance().displayImage(data.getPreview(), ivImg, ImageLoaderSetting.defaultOptions);
    }

}
