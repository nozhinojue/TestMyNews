package com.example.cjj.mynews.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cjj.mynews.R;
import com.example.cjj.mynews.model.PicTypeData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

/**
 * Created by CJJ on 2016/2/26.
 */
public class PicFgRVAdapter extends RecyclerView.Adapter<PicFgViewHolder>  {
    private Context mContext;
    private List<PicTypeData.ResEntity.CategoryEntity> mLists;
    private ItemOnClickListener mItemOnclickListener;

    public PicFgRVAdapter(Context context,List<PicTypeData.ResEntity.CategoryEntity> lists) {
        mContext=context;
        mLists=lists;
    }

    @Override
    public PicFgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PicFgViewHolder holder = new PicFgViewHolder(LayoutInflater.from(mContext).inflate(R.layout.picfg_rv_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final PicFgViewHolder holder, int position) {
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


class PicFgViewHolder extends RecyclerView.ViewHolder{
    ImageView ivImg;
    TextView tvName;

    public PicFgViewHolder(View itemView) {
        super(itemView);
        ivImg= (ImageView) itemView.findViewById(R.id.iv_img_picFg_item);
        tvName= (TextView) itemView.findViewById(R.id.tv_name_picFg_item);
    }

    public void setData(PicTypeData.ResEntity.CategoryEntity data) {
        tvName.setText(data.getName());
        ImageLoader.getInstance().displayImage(data.getCover(), ivImg, getImageOptionsConfig());
    }

    /**
     * 配置图片加载时候的配置,在实际开发中可以对这些参数进行一次封装。
     */
    private DisplayImageOptions getImageOptionsConfig(){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)//设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.ic_launcher)//设置图片Uri为null或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.ic_launcher)//设置图片加载/解码过程中错误时显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)//是否考虑JPEG图像的旋转,翻转
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.ARGB_8888)//设置图片的解码类型
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置和复位
                .displayer(new SimpleBitmapDisplayer())//不设置的时候是默认的
                        //.displayer(new RoundedBitmapDisplayer(20))//是否为圆角,弧度是多少
                        //displayer()还可以设置渐入动画
                .build();
        return options;
    }

}