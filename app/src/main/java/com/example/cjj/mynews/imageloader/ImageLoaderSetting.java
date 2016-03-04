package com.example.cjj.mynews.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.example.cjj.mynews.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by CJJ on 2016/2/29.
 */
public class ImageLoaderSetting {

    public static DisplayImageOptions defaultOptions;

    public void initImageLoader(Context context) {

        // 设置缓存 路径
        File cacheDir = StorageUtils.getOwnCacheDirectory(context,
                "TestNews/imageloader/Cache");

        //默认
        defaultOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)//设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.ic_launcher)//设置图片Uri为null或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.ic_launcher)//设置图片加载/解码过程中错误时显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)//是否考虑JPEG图像的旋转,翻转
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置和复位
                .displayer(new SimpleBitmapDisplayer())//不设置的时候是默认的
                        //.displayer(new RoundedBitmapDisplayer(20))//是否为圆角,弧度是多少
                        //displayer()还可以设置渐入动画
                .build();


        /**
         * 配置ImageLoader基本属性,最好放在Application中(只能配置一次,如多次配置,则会默认第一次的配置参数)
         */
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)//设置线程优先级
                .threadPoolSize(4)//线程池内加载的数量,推荐范围1-5内。
                .denyCacheImageMultipleSizesInMemory()//当同一个Uri获取不同大小的图片缓存到内存中时只缓存一个。不设置的话默认会缓存多个不同大小的图片
                .memoryCacheExtraOptions(480, 800)//内存缓存文件的最大长度
                .memoryCache(new LruMemoryCache(10 * 1024 * 1024))//内存缓存方式,这里可以换成自己的内存缓存实现。(推荐LruMemoryCache,道理自己懂的)
                .memoryCacheSize(10 * 1024 * 1024)//内存缓存的最大值
                .diskCache(new UnlimitedDiskCache(cacheDir))//可以自定义缓存路径
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//对保存的URL进行加密保存
                .diskCacheSize(50 * 1024 * 1024)     //50 Mb sd卡(本地)缓存的最大值
                .diskCacheFileCount(100)             // sd卡(本地)可以缓存的文件数量
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000))//设置连接时间5s,超时时间30s
                .writeDebugLogs()   // 打印debug log
                .build();
        ImageLoader.getInstance().init(config);
    }

//    /**
//     * 创建存储缓存的文件夹路径
//     *
//     * @return
//     */
//    private File createSavePath() {
//        String path;
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            path = Environment.getExternalStorageDirectory().getPath() + "/TestCash/";
//        } else {
//            path = "/TestCash/";
//        }
//        File file = new File(path);
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        return file;
//    }

}
