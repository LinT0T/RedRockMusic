package com.lint0t.redrockmusic.model;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lint0t.redrockmusic.bean.Bean;
import com.lint0t.redrockmusic.bean.LibraryBean;
import com.lint0t.redrockmusic.bean.MusicBean;
import com.lint0t.redrockmusic.bean.MusicCache;
import com.lint0t.redrockmusic.bean.MusicCacheList;
import com.lint0t.redrockmusic.bean.MyBean;
import com.lint0t.redrockmusic.bean.PlayListBean;
import com.lint0t.redrockmusic.bean.RecommsBean;
import com.lint0t.redrockmusic.utils.MyThreadPool;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class InitDatas {
    private MyThreadPool myThreadPool = new MyThreadPool();
    private String path = null;
    private MyBean myBean;
    private List<Bean> datas = new ArrayList<>();
    private List<RecommsBean> recommsBeans = new ArrayList<>();
    private List<LibraryBean> libraryBeans = new ArrayList<>();
    private List<PlayListBean> playListBeans = new ArrayList<>();
    private List<MusicBean> musicBeans = new ArrayList<>();

    public void initBannerData(String url, final Handler handler) {
        myThreadPool.setMode(MyThreadPool.USE_CACHEDTHREADPOOL).setRunnable(new Runnable() {
            @Override
            public void run() {
                try {
                    String s = requestByGet(url);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("albums");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = new JSONObject(String.valueOf(jsonArray.get(i)));
                        Bean bean = new Bean(jsonObject1.getLong("id"), jsonObject1.getString("picUrl"), jsonObject1.getString("name"));
                        datas.add(bean);
                    }
                    Message message = new Message();
                    message.what = 1;
                    message.obj = datas;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).go();
    }

    public void initRecomms(String url, Handler handler) {
        myThreadPool.setMode(MyThreadPool.USE_CACHEDTHREADPOOL).setRunnable(new Runnable() {
            @Override
            public void run() {
                try {
                    String s = requestByGet(url);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("playlists");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("creator");
                        RecommsBean recommsBean = new RecommsBean(jsonObject1.getString("name"), jsonObject1.getString("coverImgUrl")
                                , jsonObject2.getString("nickname"), jsonObject1.getLong("id"));
                        recommsBeans.add(recommsBean);
                    }
                    Message message = new Message();
                    message.what = 2;
                    message.obj = recommsBeans;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).go();

    }


    /*
     *
     * 我的歌单
     * 加载数据
     *
     * */
    public void initLibrary(String url, Handler handler) {
        myThreadPool.setMode(MyThreadPool.USE_CACHEDTHREADPOOL).setRunnable(new Runnable() {
            @Override
            public void run() {
                try {
                    String s = requestByGet(url);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("playlist");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("creator");
                        LibraryBean libraryBean = new LibraryBean(jsonObject1.getString("name"), jsonObject2.getString("nickname"),
                                jsonObject1.getString("coverImgUrl"), jsonObject1.getLong("id"), jsonObject1.getInt("trackCount"));
                        libraryBeans.add(libraryBean);
                    }
                    Message message = new Message();
                    message.what = 3;
                    message.obj = libraryBeans;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).go();
    }

    public void initProfile(String url, Handler handler) {
        myThreadPool.setMode(MyThreadPool.USE_CACHEDTHREADPOOL).setRunnable(new Runnable() {
            @Override
            public void run() {
                try {
                    String s = requestByGet(url);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("profile");
                    String name = jsonObject1.getString("nickname");
                    String city = jsonObject1.getString("city");
                    List<String> datas = new ArrayList<>();
                    datas.add(name);
                    datas.add(city);
                    Message message = new Message();
                    message.what = 4;
                    message.obj = datas;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).go();
    }

    public void initPlayList(String url, Handler handler) {
        myThreadPool.setMode(MyThreadPool.USE_CACHEDTHREADPOOL).setRunnable(new Runnable() {
            @Override
            public void run() {
                try {
                    String s = requestByGet(url);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject object = jsonObject.getJSONObject("playlist");
                    JSONArray jsonArray = object.getJSONArray("tracks");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        JSONArray jsonArray1 = jsonObject1.getJSONArray("ar");
                        JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
                        JSONObject jsonObject3 = jsonObject1.getJSONObject("al");
                        PlayListBean bean = new PlayListBean(jsonObject1.getString("name"), jsonObject2.getString("name"), jsonObject3.getString("picUrl"), jsonObject1.getLong("dt")
                                , jsonObject1.getLong("id"), jsonObject1.getInt("fee"), i + 1, jsonArray.length() + 1);
                        String url1 = MyBean.baseURL + "/song/url?id=" + bean.getId();
                        String s1 = requestByGet(url1);
                        JSONObject jsonObjectMusic = new JSONObject(s1);

                        JSONArray jsonArrayMusic = jsonObjectMusic.getJSONArray("data");
                        JSONObject jsonObject1Music = jsonArrayMusic.getJSONObject(0);
                        MusicCache.setSongUrl(jsonObject1Music.getString("url"));
                        MusicBean musicBean;
                        if (bean.getFee() > 0){
                            musicBean = new MusicBean(bean.getName(),bean.getAuthor(),String.valueOf(bean.getDt()),null,String.valueOf(bean.getId()),null);
                        } else {
                         musicBean = new MusicBean(bean.getName(),bean.getAuthor(),String.valueOf(bean.getDt()),MusicCache.getSongUrl(),String.valueOf(bean.getId()),null);
                        }
                        playListBeans.add(bean);
                        musicBeans.add(musicBean);
                    }
                    MusicCacheList.setMusicBeans(musicBeans);
                    Message message = new Message();
                    message.what = 5;
                    message.obj = playListBeans;
                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).go();
    }


    private String requestByGet(String path) throws Exception {

        // 新建一个URL对象
        if (path == null) {
            Log.i("Init", "requestByGet: path is null");
            return null;
        }
        URL url = new URL(path);

        URLConnection connection = url.openConnection();
//使用输入流
        InputStream is = connection.getInputStream();
        InputStreamReader isr = new InputStreamReader(is, "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String line;
        String result = null;
        StringBuffer sb = new StringBuffer();
        while ((line = br.readLine()) != null) {
            sb.append(line);
            result = sb.toString(); //即请求得到的数据
        }
        return result;
    }


}
