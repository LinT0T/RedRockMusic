package com.lint0t.redrockmusic.bean;

import android.graphics.Bitmap;

public class MusicBean {
    private String name,singer,time,path,id;
    private Bitmap bitmap;
    private static Bitmap cover;
    private static String sname;

    public static String getSname() {
        return sname;
    }

    @Override
    public String toString() {
        return "MusicBean{" +
                "name='" + name + '\'' +
                ", singer='" + singer + '\'' +
                ", time='" + time + '\'' +
                ", path='" + path + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public MusicBean() {
    }

    public static Bitmap getCover() {
        return cover;
    }


    public MusicBean(String name, String singer, String time, String path, String id, Bitmap bitmap) {
        this.name = name;
        this.singer = singer;
        this.time = time;
        this.path = path;
        this.id = id;
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


}
