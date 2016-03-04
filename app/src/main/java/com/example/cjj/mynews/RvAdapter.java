package com.example.cjj.mynews;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cjj.mynews.model.NewsListModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

/**
 * Created by CJJ on 2016/2/20.
 */
public class RvAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context mContext;
    private List<NewsListModel> mDatas;
    private ItemOnClickListener mItemOnclickListener;

    public RvAdapter(Context context,List<NewsListModel> data){
        mContext=context;
        mDatas=data;
    }

    //item点击事件接口
    public interface ItemOnClickListener{
        void onClick(int position, View view);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        //holder.tv.setText(mDatas.get(position));
        holder.setData(mDatas.get(position));

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
        return mDatas.size();
    }

    public ItemOnClickListener getmItemOnclickListener() {
        return mItemOnclickListener;
    }

    public void setmItemOnclickListener(ItemOnClickListener mItemOnclickListener) {
        this.mItemOnclickListener = mItemOnclickListener;
    }

}

class MyViewHolder extends RecyclerView.ViewHolder
{

    private TextView tv_title;
    private TextView tv_abstract;
    private ImageView iv_img;

    public MyViewHolder(View view)
    {
        super(view);
        tv_title = (TextView) view.findViewById(R.id.tv_title_rvItem);
        tv_abstract = (TextView) view.findViewById(R.id.tv_abstract_rvItem);
        iv_img= (ImageView) view.findViewById(R.id.iv_img_rvItem);
    }

    //设置数据
    public void setData(NewsListModel newItemData){
        tv_title.setText(newItemData.getTitle());
        tv_abstract.setText(newItemData.getDescription());

        ImageLoader.getInstance().displayImage(newItemData.getPicUrl(),iv_img,getImageOptionsConfig());
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
