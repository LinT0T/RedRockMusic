package com.lint0t.redrockmusic.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyImageLoader {
    private Context context;
    private LruCache<String, Bitmap> mMemoryCache;
    private MyThreadPool myThreadPool = new MyThreadPool();
    private String src = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1583565012840&di=4a8f6fe834c593f4f571cad8e9c78d75&imgtype=0&src=http%3A%2F%2Fpic.rmb.bdstatic.com%2F81add38219a545a71f9b0cb60993bc6a.jpeg";
    private Handler handler2;
    private int taskNumber = 1, maxThread = 1;
    private File cacheDir;
    private boolean use_cache = false;

    public MyImageLoader() {
    }

    public MyImageLoader setTaskNumber(int i) {
        this.taskNumber = i;
        return this;
    }

    public MyImageLoader setMaxThread(int i) {
        this.maxThread = i;
        return this;
    }

    public MyImageLoader setUseCache(boolean b) {
        this.use_cache = b;
        return this;
    }

    public MyImageLoader setURL(String s) {
        this.src = s;
        return this;
    }


    public MyImageLoader setHandler(Handler handler) {
        this.handler2 = handler;
        return this;
    }

    public MyImageLoader(Context context) {
        memoryCache();

        this.context = context;
        cacheDir = context.getCacheDir();
    }

    public void load() {
        try {
            Bitmap bit = mMemoryCache.get(src);
            if (bit != null && use_cache) {
                Message message = new Message();
                message.what = 400;
                message.obj = bit;
                handler2.sendMessage(message);
            } else {
                bit = getBitmapFromFile(pathName(src));
                if (bit != null && use_cache) {
                    Message message = new Message();
                    message.what = 400;
                    message.obj = bit;
                    handler2.sendMessage(message);
                } else {
                    myThreadPool.setMode(MyThreadPool.USE_FIXEDTHREADPOOL).setMaxThread(maxThread).setTaskNumber(taskNumber).setRunnable(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                URL url = new URL(src);
                                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                                InputStream inputStream = httpURLConnection.getInputStream();
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                Message message = new Message();
                                message.what = 400;
                                message.obj = bitmap;
                                mMemoryCache.put(src, bitmap);
                                String path = pathName(src);
                                writeToLoce(path, bitmap);
                                handler2.sendMessage(message);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).go();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void loads(String s) {

        final String st = s;
        Bitmap bit = mMemoryCache.get(st);
        System.out.println(bit);
        if (bit != null) {
            Message message = new Message();
            message.what = 200;
            message.obj = bit;
            handler2.sendMessage(message);
        } else {
            bit = getBitmapFromFile(pathName(st));
            if (bit != null) {
                Message message = new Message();
                message.what = 200;
                message.obj = bit;
                handler2.sendMessage(message);
            } else {
                myThreadPool.setMode(MyThreadPool.USE_FIXEDTHREADPOOL).setTaskNumber(1).setMaxThread(maxThread).setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(st);
                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                            InputStream inputStream = httpURLConnection.getInputStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            Message message = new Message();
                            message.what = 200;
                            message.obj = bitmap;
                            mMemoryCache.put(st, bitmap);
                            String path = pathName(st);
                            System.out.println("GG  " + path);
                            writeToLoce(path, bitmap);
                            handler2.sendMessage(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).go();
            }
        }
    }


    private void memoryCache() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // 使用最大可用内存值的1/8作为缓存的大小。
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // 重写此方法来衡量每张图片的大小，默认返回图片数量。
                return bitmap.getByteCount() / 1024;
            }

        };
    }


    private void writeToLoce(String imageUrl, Bitmap bitmap) {
        try {
            String bitmapefilename = imageUrl;
            Log.i("bitmapefilename", bitmapefilename);
            File file = new File(cacheDir, bitmapefilename);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            //写入文件的操作(1图片类型2图片质量当为100时表示不压缩3文件流)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private Bitmap getBitmapFromFile(String url) {
        try {
            String bitmapefilename = url;
            /**
             * 在cache文件夹下面找到指定文件
             * dir cache文件存储路径
             * name 文件名称
             */
            File file = new File(cacheDir, bitmapefilename);
            ;
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());

            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public String pathName(String s) {
        String result = "";
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) != '/') {
                result += s.charAt(i);
            } else return result;
        }
        return null;
    }
}

