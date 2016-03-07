package com.example.cjj.mynews.utils;

/**
 *
 */
public class TimeUtils {

    /**
     * int 毫秒 转换为 分:秒
     * @param size
     * @return
     */
    public static String transformationMS(int size){
        size=size/1000;
        int minute=size/60;
        int second=size%60;

        StringBuilder sb=new StringBuilder();
        if(minute<10){
            sb.append("0"+minute+":");
        }else{
            sb.append(minute+":");
        }
        if(second<10){
            sb.append("0"+second);
        }else{
            sb.append(second);
        }
        return sb.toString();
    }
}
