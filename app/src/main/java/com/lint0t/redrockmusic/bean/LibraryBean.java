package com.lint0t.redrockmusic.bean;

public class LibraryBean {
    private String name,author,url;
    private long id;
    private int tracks;

    @Override
    public String toString() {
        return "LibraryBean{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", url='" + url + '\'' +
                ", tracks='" + tracks + '\'' +
                ", id=" + id +
                '}';
    }

    public LibraryBean(String name, String author, String url, long id, int tracks) {
        this.name = name;
        this.author = author;
        this.url = url;
        this.id = id;
        this.tracks = tracks;
    }

    public int getTracks() {
        return tracks;
    }

    public void setTracks(int tracks) {
        this.tracks = tracks;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
