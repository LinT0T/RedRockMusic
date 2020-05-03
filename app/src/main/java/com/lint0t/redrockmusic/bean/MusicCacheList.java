package com.lint0t.redrockmusic.bean;

import java.util.List;

public class MusicCacheList {
    public static List<MusicBean> musicBeans;

    public static List<MusicBean> getMusicBeans() {
        return musicBeans;
    }

    public static void setMusicBeans(List<MusicBean> musicBeans) {
        MusicCacheList.musicBeans = musicBeans;
    }
}
