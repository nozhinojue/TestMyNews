package com.example.cjj.mynews.model;

import java.util.List;

/**
 * Created by CJJ on 2016/2/20.
 * {
     "code": 200,
     "msg": "ok",
     "newslist": [
                     {
                     "ctime": "2016-02-20 08:08",
                     "title": "四大洲花样滑冰锦标赛 美国兄妹组合获冰舞冠军",
                     "description": "四大洲花样滑冰锦标赛 美国兄妹组合获冰舞冠军...",
                     "picUrl": "",
                     "url": "http://news.sohu.com/20160220/n437943101.shtml"
                     },
                     {
                     "ctime": "2016-02-20 09:30",
                     "title": "美总统参选人初选“拼杀” 互相攻击广告数量过万",
                     "description": "美总统参选人初选“拼杀” 互相攻击广告数量过万...",
                     "picUrl": "",
                     "url": "http://news.sohu.com/20160220/n437946920.shtml"
                     }
                ]
    }
 */
public class NewItemData {
    private String code;
    private String msg;
    private List<NewsListModel> newslist;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<NewsListModel> getNewslist() {
        return newslist;
    }

    public void setNewslist(List<NewsListModel> newslist) {
        this.newslist = newslist;
    }
}
