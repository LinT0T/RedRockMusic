package com.lint0t.redrockmusic.bean;

import java.util.List;

public class MusicCache {
    public static String name, author, url,songUrl;
    public static long dt, id;
    public static int fee, num,nowPos = -1;

    public static void load(PlayListBean bean){
        name = bean.getName();
        author = bean.getAuthor();
        url = bean.getUrl();
        dt = bean.getDt();
        id = bean.getId();
        fee = bean.getFee();
        num = bean.getNum();
    }

    public static int getNowPos() {
        return nowPos;
    }

    public static void setNowPos(int nowPos) {
        MusicCache.nowPos = nowPos;
    }

    public static String getSongUrl() {
        return songUrl;
    }

    public static void setSongUrl(String songUrl) {
        MusicCache.songUrl = songUrl;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        MusicCache.name = name;
    }

    public static String getAuthor() {
        return author;
    }

    public static void setAuthor(String author) {
        MusicCache.author = author;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        MusicCache.url = url;
    }

    public static long getDt() {
        return dt;
    }

    public static void setDt(long dt) {
        MusicCache.dt = dt;
    }

    public static long getId() {
        return id;
    }

    public static void setId(long id) {
        MusicCache.id = id;
    }

    public static int getFee() {
        return fee;
    }

    public static void setFee(int fee) {
        MusicCache.fee = fee;
    }

    public static int getNum() {
        return num;
    }

    public static void setNum(int num) {
        MusicCache.num = num;
    }


}
