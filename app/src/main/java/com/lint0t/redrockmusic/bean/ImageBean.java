package com.lint0t.redrockmusic.bean;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class ImageBean {
    List<Bitmap> datas = new ArrayList<>();

    public ImageBean(List<Bitmap> datas) {
        this.datas = datas;
    }

    public List<Bitmap> getDatas() {
        return datas;
    }

    public void setDatas(List<Bitmap> datas) {
        this.datas = datas;
    }
}
