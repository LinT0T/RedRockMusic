package com.lint0t.redrockmusic.bean;

public class RecommsBean {
    private String name,coverImgUrl,nickName;
    private long id;

    public RecommsBean(String name, String coverImgUrl, String nickName, long id) {
        this.name = name;
        this.coverImgUrl = coverImgUrl;
        this.nickName = nickName;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RecommsBean{" +
                "name='" + name + '\'' +
                ", coverImgUrl='" + coverImgUrl + '\'' +
                ", nickName='" + nickName + '\'' +
                ", id=" + id +
                '}';
    }
}
