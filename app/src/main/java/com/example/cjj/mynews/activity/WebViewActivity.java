package com.example.cjj.mynews.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cjj.mynews.R;

public class WebViewActivity extends Activity {

    private WebView webView;
    private String urlStr;
    private FrameLayout flLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        urlStr=getIntent().getStringExtra("URL");   //获取启动当前intent中的URL值。

        ImageView ivActionbar= (ImageView) findViewById(R.id.iv_actionbar);
        ivActionbar.setImageResource(R.mipmap.back_icon);
        ivActionbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebViewActivity.this.finish();
            }
        });
        TextView tvActionbar= (TextView) findViewById(R.id.tv_actionbar);
        tvActionbar.setText("内容");
        flLoading= (FrameLayout) findViewById(R.id.fl_loading); //framelayout进度条
//        Button btnBack= (Button) findViewById(R.id.btn_back);
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                WebViewActivity.this.finish();
//            }
//        });

        webView = (WebView) findViewById(R.id.wv_webview);
        webView.getSettings().setJavaScriptEnabled(true);//设置使用够执行JS脚本
       // webView.getSettings().setBuiltInZoomControls(true);//设置使支持缩放
        webView.loadUrl(urlStr);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //开始加载页面
                super.onPageStarted(view, url, favicon);
                flLoading.setVisibility(View.VISIBLE); //显示进度条。
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //页面加载结束
                super.onPageFinished(view, url);
                flLoading.setVisibility(View.GONE); //隐藏进度条。
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                //网页加载失败
                super.onReceivedError(view, request, error);
                flLoading.setVisibility(View.GONE); //隐藏进度条。
                Toast.makeText(WebViewActivity.this, "页面加载失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);// 使用当前WebView处理跳转
                return true;//true表示此事件在此处被处理，不需要再广播
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    //加载完成了
                    flLoading.setVisibility(View.GONE);//隐藏进度条。
                    //Toast.makeText(WebViewActivity.this, "加载完成", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override   //默认点回退键，会退出Activity，需监听按键操作，使回退在WebView内发生
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.destroy();
        }
    }
}
