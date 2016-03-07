package com.example.cjj.mynews.okhttp;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by CJJ on 2016/2/22.
 */
public class MyOkHttp {
    public static String TIYU_URL="http://apis.baidu.com/txapi/tiyu/tiyu";      //体育新闻
    public static String KEJI_URL="http://apis.baidu.com/txapi/keji/keji";      //科技新闻
    public static String WORLD_URL="http://apis.baidu.com/txapi/world/world";   //国际新闻

    private static String myAPIKey="68f081859a99d672e259e2f4b1d4cae9";
    // private static String httpUrl = "http://apis.baidu.com/txapi/keji/keji";   //科技新闻
    //http://apis.baidu.com/txapi/world/world  国际新闻
   // private static String httpUrl = "http://apis.baidu.com/txapi/tiyu/tiyu";   //体育新闻

//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .header("apikey",myAPIKey)
//                .url(urlStr)
//                .build();
//        Call call=client.newCall(request);
//        resultStr =call.execute().body().string();

    //获取新闻数据。
    public void getNewsData(String url,int pageNum, final Context context, final MyOkHttpCallBack myOkHttpCallBack){
        String urlStr=url+ "?"+"num=10&page="+pageNum;

        Request request=new Request.Builder()
                .header("apikey",myAPIKey)
                .url(urlStr)
                .build();
        OkHttpUtil.getInstance().getJsonFromServer(request, new OkHttpUtil.MyCallBack() {
            @Override
            public void onFailure(Call call, IOException e) {
                myOkHttpCallBack.Failure(call,e);
            }

            @Override
            public void onResponse(String json) {
                myOkHttpCallBack.Successed(json);
            }
        });
    }

    public void getPicData(final MyOkHttpCallBack myOkHttpCallBack){
        String urlStr="http://service.picasso.adesk.com/v1/wallpaper/category";

        Request request = new Request.Builder()
                .url(urlStr)
                .build();
        OkHttpUtil.getInstance().getJsonFromServer(request, new OkHttpUtil.MyCallBack() {
            @Override
            public void onFailure(Call call, IOException e) {
                myOkHttpCallBack.Failure(call,e);
            }

            @Override
            public void onResponse(String json) {
                myOkHttpCallBack.Successed(json);
            }
        });
    }

    //获取图片详细信息。
    public void getPicDetailData(String id,int skipNum, final MyOkHttpCallBack myOkHttpCallBack){
        String urlStr="http://service.picasso.adesk.com/v1/wallpaper/category/"+id+"/wallpaper?order=new&adult=false&limit=30&first=0&skip="+skipNum;
        Request request = new Request.Builder()
                .url(urlStr)
                .build();
        OkHttpUtil.getInstance().getJsonFromServer(request, new OkHttpUtil.MyCallBack() {
            @Override
            public void onFailure(Call call, IOException e) {
                myOkHttpCallBack.Failure(call,e);
            }

            @Override
            public void onResponse(String json) {
                myOkHttpCallBack.Successed(json);
            }
        });
    }

    /**
     * 获取音乐数据
     * @param type  不同的类型,1、2、3....
     */
    public void getMusicData(int type,final MyOkHttpCallBack myOkHttpCallBack){
        //天天动听的APi
        String Muisc_Recommend="http://api.dongting.com/" +
                "favorite/song/plaza?from=android&api_version=1.0&agent=none&user_id=0&language=zh&random="+type;
        Request request = new Request.Builder()
                .url(Muisc_Recommend)
                .build();
        OkHttpUtil.getInstance().getJsonFromServer(request, new OkHttpUtil.MyCallBack() {
            @Override
            public void onFailure(Call call, IOException e) {
                myOkHttpCallBack.Failure(call,e);
            }

            @Override
            public void onResponse(String json) {
                myOkHttpCallBack.Successed(json);
            }
        });
    }



    //接口
    public interface MyOkHttpCallBack{
        void Successed(String jsonResult);
        void Failure(Call call,IOException e);
    }

}
