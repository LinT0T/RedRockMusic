package com.lint0t.redrockmusic.bean;

public class PlayListBean {
    private String name,author,url;
    private long dt,id;
    private int fee,num,tracks;





    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "PlayListBean{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", url='" + url + '\'' +
                ", dt=" + dt +
                ", id=" + id +
                ", fee=" + fee +
                ", num=" + num +
                ", tracks=" + tracks +
                '}';
    }

    public PlayListBean(String name, String author, String url, long dt, long id, int fee, int num, int tracks) {
        this.name = name;
        this.author = author;
        this.url = url;
        this.dt = dt;
        this.id = id;
        this.fee = fee;
        this.num = num;
        this.tracks = tracks;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTracks() {
        return tracks;
    }

    public void setTracks(int tracks) {
        this.tracks = tracks;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }
}
