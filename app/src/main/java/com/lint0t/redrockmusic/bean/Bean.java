package com.lint0t.redrockmusic.bean;

import android.graphics.Bitmap;

public class Bean {
    public static final String baseURL = "http://47.99.165.194";
    private String picUrl, name;
    private int albumSize;
    private long id;
    @Override
    public String toString() {
        return "Bean{" +
                "picUrl='" + picUrl + '\'' +
                ", name='" + name + '\'' +
                ", albumSize=" + albumSize +
                ", id=" + id +
                '}';
    }

    public Bean(long id, String picUrl, String name) {
        this.picUrl = picUrl;
        this.name = name;
        this.id = id;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAlbumSize() {
        return albumSize;
    }

    public void setAlbumSize(int albumSize) {
        this.albumSize = albumSize;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
