package com.example.cjj.mynews.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cjj.mynews.R;
import com.example.cjj.mynews.imageloader.ImageLoaderSetting;
import com.example.cjj.mynews.utils.FileUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;

public class ImageDetailActivity extends Activity implements View.OnClickListener {
    private ImageView imageVie;
    private ProgressBar progressBar;

    private String imgUrl;
    private String imgName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        imgUrl = getIntent().getStringExtra("imgurl");
        imgName = getIntent().getStringExtra("imgname");

        imageVie= (ImageView) findViewById(R.id.iv_imageDetailA);
        progressBar= (ProgressBar) findViewById(R.id.pb_loading_imageDetailA);
        Button btnDownload= (Button) findViewById(R.id.bt_download);
        btnDownload.setOnClickListener(this);

        showImage();
    }


    private void showImage() {
        ImageLoader.getInstance().displayImage(imgUrl, imageVie, ImageLoaderSetting.defaultOptions, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                String message = null;
                switch (failReason.getType()) {
                    case IO_ERROR:
                        message = "下载错误";
                        break;
                    case DECODING_ERROR:
                        message = "图片无法显示";
                        break;
                    case NETWORK_DENIED:
                        message = "网络有问题，无法下载";
                        break;
                    case OUT_OF_MEMORY:
                        message = "图片太大无法显示";
                        break;
                    case UNKNOWN:
                        message = "未知的错误";
                        break;
                }
                Toast.makeText(ImageDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.bt_download:

                File file = ImageLoader.getInstance().getDiskCache().get(imgUrl);
                File file1 = new File(createSavePath(), imgName + ".jpg");
                FileUtil.saveFile(this, file, file1);

                //Toast.makeText(ImageDetailActivity.this, "下载完成", Toast.LENGTH_LONG).show();
                break;
        }
    }

    /**
     * 创建存储缓存的文件夹路径
     *
     * @return
     */
    private File createSavePath() {
        String path;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory().getPath() + "/MynewsDownload/";
        } else {
            path = "/MynewsDownload/";
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
}
