package com.lint0t.redrockmusic.activity;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lint0t.redrockmusic.R;
import com.lint0t.redrockmusic.adapter.MyViewPagerAdapter;

import com.lint0t.redrockmusic.bean.MusicBean;
import com.lint0t.redrockmusic.bean.MusicCache;
import com.lint0t.redrockmusic.bean.MusicCacheList;
import com.lint0t.redrockmusic.bean.MyBean;
import com.lint0t.redrockmusic.fragment.HomeFragment;
import com.lint0t.redrockmusic.fragment.LibraryFragment;
import com.lint0t.redrockmusic.fragment.MusicFragment;
import com.lint0t.redrockmusic.fragment.PlayListFragment;
import com.lint0t.redrockmusic.fragment.ProfileFragment;
import com.lint0t.redrockmusic.model.InitDatas;
import com.lint0t.redrockmusic.utils.MyImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ServiceReceiver receiver;
    private Fragment fragment;
    private TabLayout mtab_home;
    private List<Fragment> fragments = new ArrayList<>();
    private ViewPager mvp_home;
    private TextView mtv_title, mtv_name, mtv_author;
    private LinearLayout mll_title, mll_music;
    private ImageView mimg_search, mimg_back, mimg_stop, mimg_music_stop, mimg_music_next, mimg_music_last;
    private View mline1, mline2;
    private MediaPlayer mediaPlayer;
    private NotificationManager manager;
    private int nowPos = -1, playTime = 0;
    private MusicFragment musicFragment = new MusicFragment();
    private List<MusicBean> datas = new ArrayList<>();
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //上一首
                    lastMusic();
                    break;

                case 2:
                    //播放
                    if (mediaPlayer.getCurrentPosition() >= 300) {
                        stopMusicBtn();
                    } else {
                        playNewMusic(datas.get(nowPos));
                    }

                    break;

                case 3:
                    //下一首
                    nextMusic();
                    break;
                case 4:
                    //传递点击位置
                    nowPos = msg.arg1;
                    System.out.println("收到" + nowPos);
                    MusicBean bean = datas.get(nowPos);
                    mtv_title.setText(bean.getName());
                    mtv_name.setText(bean.getName());
                    mtv_author.setText(bean.getSinger());
                    break;
                case 5:
                   //传递数据
                    datas.clear();
                    datas = (MusicCacheList.getMusicBeans());
                    break;
                case 6:
                    //拖动进度条
                    int i = msg.arg2;
                    mediaPlayer.seekTo(i);
                    break;
                case 7:
                    //进度条刷新

                    break;
            }
        }
    };

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (PlayListFragment.inPlayList) {
                removePlayListFragment("playList");
                return true;
            } else {
                if (MusicFragment.isMusic) {
                    removePlayListFragment("music");
                    return true;
                } else {
                    return super.onKeyDown(keyCode, event);
                }
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    private void removePlayListFragment(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (fragment != null) {
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        }
        mimg_back.setVisibility(View.INVISIBLE);
        mimg_search.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        hideBottomUIMenu();
        init();
        showCustomView();
    }

    private void init() {
        mtab_home = findViewById(R.id.tab_home);
        mvp_home = findViewById(R.id.vp_home);

        mtv_title = findViewById(R.id.tv_title);
        mll_music = findViewById(R.id.ll_music);
        mll_title = findViewById(R.id.ll_title);
        mimg_search = findViewById(R.id.img_search);
        mimg_back = findViewById(R.id.img_back);
        mimg_back.setVisibility(View.INVISIBLE);
        mline1 = findViewById(R.id.view_line1);
        mline2 = findViewById(R.id.view_line2);
        mimg_stop = findViewById(R.id.img_stop);
        mimg_stop.setOnClickListener(this);
        mtv_name = findViewById(R.id.tv_main_name);
        mtv_author = findViewById(R.id.tv_main_author);
        mediaPlayer = new MediaPlayer();
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        receiver = new ServiceReceiver();//----注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ServiceReceiver.NOTIFICATION_ITEM_BUTTON_LAST);
        intentFilter.addAction(ServiceReceiver.NOTIFICATION_ITEM_BUTTON_PLAY);
        intentFilter.addAction(ServiceReceiver.NOTIFICATION_ITEM_BUTTON_NEXT);

        registerReceiver(receiver, intentFilter);
        HomeFragment.setSelectListener(new HomeFragment.SelectListener() {
            @Override
            public void returnWhich(int inWhich) {

                mimg_back.setVisibility(View.VISIBLE);
                mimg_search.setVisibility(View.INVISIBLE);
            }
        });
        mimg_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePlayListFragment("playList");
            }
        });
        mll_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                MusicFragment musicFragment = new MusicFragment();
                fragmentManager.beginTransaction().add(musicFragment, "music");
                fragmentManager.beginTransaction().replace(R.id.frm_home, musicFragment, "music").commitAllowingStateLoss();
                mimg_back.setVisibility(View.INVISIBLE);
                mimg_search.setVisibility(View.INVISIBLE);
                mtv_title.setText(MusicCache.getName());
            }
        });
        fragments.add(new HomeFragment());
        fragments.add(new LibraryFragment());
        fragments.add(new ProfileFragment());
        MyViewPagerAdapter viewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragments);
        mvp_home.setAdapter(viewPagerAdapter);
        mvp_home.setOffscreenPageLimit(3);
        mtab_home.setupWithViewPager(mvp_home);
        mtab_home.setTabMode(TabLayout.MODE_FIXED);
        mtab_home.getTabAt(0).setIcon(R.mipmap.flame);
        mtab_home.getTabAt(1).setIcon(R.mipmap.library);
        mtab_home.getTabAt(2).setIcon(R.mipmap.account);
        mvp_home.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mtab_home.getTabAt(0).setIcon(R.mipmap.flameblue);
                        mtab_home.getTabAt(1).setIcon(R.mipmap.library);
                        mtab_home.getTabAt(2).setIcon(R.mipmap.account);
                        mtv_title.setText("Home");
                        mimg_search.setVisibility(View.VISIBLE);
                        mll_title.setVisibility(View.VISIBLE);
                        mll_music.setVisibility(View.VISIBLE);
                        mline1.setVisibility(View.VISIBLE);
                        mline2.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        mtab_home.getTabAt(0).setIcon(R.mipmap.flame);
                        mtab_home.getTabAt(2).setIcon(R.mipmap.account);
                        mtab_home.getTabAt(1).setIcon(R.mipmap.libraryblue);
                        mtv_title.setText("Library");
                        mimg_search.setVisibility(View.INVISIBLE);
                        mll_title.setVisibility(View.VISIBLE);
                        mll_music.setVisibility(View.VISIBLE);
                        mline1.setVisibility(View.VISIBLE);
                        mline2.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        mtab_home.getTabAt(2).setIcon(R.mipmap.accountblue);
                        mtab_home.getTabAt(0).setIcon(R.mipmap.flame);
                        mtab_home.getTabAt(1).setIcon(R.mipmap.library);
                        mtv_title.setText("Profile");
                        mimg_search.setVisibility(View.INVISIBLE);
                        mll_title.setVisibility(View.GONE);
                        mll_music.setVisibility(View.GONE);
                        mline1.setVisibility(View.GONE);
                        mline2.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_stop:
                // nowPos = MusicCache.getNowPos();
                if (nowPos != -1) {

                    if (datas.get(nowPos).getPath() != null) {
                        playNewMusic(datas.get(nowPos));
                    }
                }
                break;
        }
    }

    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        hideBottomUIMenu();
    }

    private void playNewMusic(MusicBean bean) {
        mtv_title.setText(bean.getName());
        mtv_name.setText(bean.getName());
        mtv_author.setText(bean.getSinger());
        stopMusic();
        mediaPlayer.reset();
        try {
            if (bean.getPath() == null) {
                Toast.makeText(HomeActivity.this, "付费歌曲无法播放哦", Toast.LENGTH_SHORT).show();
            } else {
                mediaPlayer.setDataSource(bean.getPath());
                playMusic();
                showCustomView();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopMusic() {
        if (mediaPlayer != null) {
            playTime = 0;
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            mediaPlayer.stop();
            mimg_stop.setImageResource(R.mipmap.playblue);
        }
    }

    private void playMusic() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            if (playTime == 0) {
                try {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                    }
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                mediaPlayer.seekTo(playTime);
                mediaPlayer.start();
            }
            mimg_stop.setImageResource(R.mipmap.stop);
        }
    }


    private void lastMusic() {
        if (nowPos == 0 || nowPos == -1) {
            Toast.makeText(this, "已是第一首或未播放歌曲", Toast.LENGTH_SHORT).show();
            return;
        }
        nowPos -= 1;
        MusicBean last = datas.get(nowPos);
        playNewMusic(last);
    }

    private void stopMusicBtn() {
        if (nowPos == -1) {
            Toast.makeText(this, "未选择歌曲", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mediaPlayer.isPlaying()) {
            pauseMusic();
        } else {
            playMusic();
        }
        showCustomView();
    }

    private void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            playTime = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
            mimg_stop.setImageResource(R.mipmap.playblue);
        }
    }

    private void nextMusic() {
        if (nowPos == datas.size() - 1) {
            Toast.makeText(this, "已是最后一首", Toast.LENGTH_SHORT).show();
            return;
        }
        nowPos += 1;
        MusicBean next = datas.get(nowPos);
        playNewMusic(next);
    }

    private void showCustomView() {
        String channelId = "ChannelId";
        String channelName = "默认通知";
        //需添加的代码
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            manager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH));
        }

        RemoteViews remoteViews = new RemoteViews(getPackageName(),
                R.layout.notification);
        if (nowPos != -1) {
            remoteViews.setTextViewText(R.id.widget_title, datas.get(nowPos).getName()); //设置textview
            remoteViews.setTextViewText(R.id.widget_artist, datas.get(nowPos).getSinger());
            MyImageLoader myImageLoader = new MyImageLoader(this);
            Handler handler = new Handler() {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case 400:
                            Bitmap bitmap = (Bitmap) msg.obj;
                            remoteViews.setImageViewBitmap(R.id.widget_album, bitmap);

                            break;
                    }

                }
            };
            myImageLoader.setURL(MusicCache.getUrl()).setUseCache(true).setHandler(handler).load();


        } else {
            remoteViews.setTextViewText(R.id.widget_title, "选一首想听的歌曲吧"); //设置textview
            remoteViews.setTextViewText(R.id.widget_artist, "");

        }
        if (mediaPlayer.isPlaying()) {
            remoteViews.setImageViewResource(R.id.widget_play, R.mipmap.stop);
        } else {
            remoteViews.setImageViewResource(R.id.widget_play, R.mipmap.playblue);
        }


//设置按钮事件 -- 发送广播 --广播接收后进行对应的处理

        Intent buttonPlayIntent = new Intent(ServiceReceiver.NOTIFICATION_ITEM_BUTTON_LAST); //----设置通知栏按钮广播
        PendingIntent pendButtonPlayIntent = PendingIntent.getBroadcast(this, 0, buttonPlayIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget_prev, pendButtonPlayIntent);//----设置对应的按钮ID监控


        Intent buttonPlayIntent1 = new Intent(ServiceReceiver.NOTIFICATION_ITEM_BUTTON_PLAY); //----设置通知栏按钮广播
        PendingIntent pendButtonPlayIntent1 = PendingIntent.getBroadcast(this, 0, buttonPlayIntent1, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget_play, pendButtonPlayIntent1);//----设置对应的按钮ID监控

        Intent buttonPlayIntent2 = new Intent(ServiceReceiver.NOTIFICATION_ITEM_BUTTON_NEXT); //----设置通知栏按钮广播
        PendingIntent pendButtonPlayIntent2 = PendingIntent.getBroadcast(this, 0, buttonPlayIntent2, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget_next, pendButtonPlayIntent2);//----设置对应的按钮ID监控


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
        builder.setContent(remoteViews).setSmallIcon(R.mipmap.me)
                .setOngoing(true).setCustomBigContentView(remoteViews)
                .setTicker("music is playing");

        manager.notify(1, builder.build());

    }


    public class ServiceReceiver extends BroadcastReceiver {
        public static final String NOTIFICATION_ITEM_BUTTON_LAST = "com.example.notification.ServiceReceiver.last";//----通知栏上一首按钮
        public static final String NOTIFICATION_ITEM_BUTTON_PLAY = "com.example.notification.ServiceReceiver.play";//----通知栏播放按钮
        public static final String NOTIFICATION_ITEM_BUTTON_NEXT = "com.example.notification.ServiceReceiver.next";//----通知栏下一首按钮

        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals(NOTIFICATION_ITEM_BUTTON_LAST)) {//----通知栏播放按钮响应事件
                Toast.makeText(context, "上一首", Toast.LENGTH_LONG).show();
                lastMusic();
            } else if (action.equals(NOTIFICATION_ITEM_BUTTON_PLAY)) {//----通知栏播放按钮响应事件
                Toast.makeText(context, "暂停", Toast.LENGTH_LONG).show();
                stopMusicBtn();
            } else if (action.equals(NOTIFICATION_ITEM_BUTTON_NEXT)) {//----通知栏下一首按钮响应事件
                Toast.makeText(context, "下一首", Toast.LENGTH_LONG).show();
                nextMusic();
            }
        }
    }

    public List<MusicBean> getDatas() {
        return datas;
    }
    public MusicBean getNowBean(){
        if (nowPos != -1)
        return datas.get(nowPos);
        else return null;
    }

    public int getPlayTime() {
        return mediaPlayer.getCurrentPosition();
    }
}
