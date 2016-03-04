package com.example.cjj.mynews.model;

/**
 * Created by CJJ on 2016/2/20.
 */
public class NewsListModel {
    private String title;       //标题
    private String description; //描述
    private String picUrl;      //图片路径
    private String url;         //详细内容路径

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
