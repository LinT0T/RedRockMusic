package com.lint0t.redrockmusic.fragment;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lint0t.redrockmusic.R;
import com.lint0t.redrockmusic.activity.HomeActivity;
import com.lint0t.redrockmusic.bean.MusicBean;
import com.lint0t.redrockmusic.bean.MusicCache;
import com.lint0t.redrockmusic.utils.MyImageLoader;
import com.lint0t.redrockmusic.utils.MyThreadPool;
import com.lint0t.redrockmusic.utils.MyWaveView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class MusicFragment extends Fragment implements View.OnTouchListener, View.OnClickListener {

    private ImageView mimg_cover;
    private boolean running = false;
    public static boolean isMusic = false;
    private boolean isPlaying = false;
    private SeekBar mSeekBar;
    private TextView mtv_left, mtv_right;
    private ImageView mimg_music_next, mimg_music_last, mimg_music_stop, mimg_shaffle, mimg_repeat;
    private Context context;
    private HomeActivity activity;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 400:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    mimg_cover.setImageBitmap(bitmap);
                    break;
                case 100:
                    Bundle bundle = msg.getData();
                    long musicDuration = bundle.getLong("musicDuration");
                    long position = bundle.getLong("position");
                    Date dateTotal = new Date(musicDuration);
                    SimpleDateFormat sb = new SimpleDateFormat("mm:ss");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                    String time1 = simpleDateFormat.format(new Date(position));
                    String time2 = simpleDateFormat.format(new Date(musicDuration));
                    mtv_right.setText(time2);
                    mtv_left.setText(time1);
                    mSeekBar.setMax((int) musicDuration);
                    mSeekBar.setProgress((int) position);
                    break;
            }
        }
    };


    private Handler handler1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        isMusic = true;
        view.setOnTouchListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mimg_cover = view.findViewById(R.id.img_music_cover);
        mSeekBar = view.findViewById(R.id.music_seek_bar);
        mtv_left = view.findViewById(R.id.tv_music_time_left);
        mtv_right = view.findViewById(R.id.tv_music_time_right);
        mimg_shaffle = view.findViewById(R.id.img_music_shaffle);
        mimg_repeat = view.findViewById(R.id.img_music_repeat);
        mimg_music_stop = view.findViewById(R.id.img_music_play);
        mimg_music_last = view.findViewById(R.id.img_music_last);
        mimg_music_next = view.findViewById(R.id.img_music_next);
        mimg_music_stop.setOnClickListener(this);
        mimg_music_next.setOnClickListener(this);
        mimg_music_last.setOnClickListener(this);
        mimg_shaffle.setOnClickListener(this);
        mimg_repeat.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    Message message = new Message();
                    message.what = 6;
                    message.arg2 = i;
                    handler1.sendMessage(message);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        MyWaveView wave = view.findViewById(R.id.wave);
        initAnim(wave);
        MyImageLoader myImageLoader = new MyImageLoader(getContext());
        if (MusicCache.getUrl() != null) {
            myImageLoader.setURL(MusicCache.getUrl()).setUseCache(true).setHandler(handler).load();
        }

    }

    private void initAnim(MyWaveView wave) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mimg_cover, "rotation", 0, 359);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setDuration(20000);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();
        PropertyValuesHolder propertyValuesHolder1 = PropertyValuesHolder.ofFloat("scaleX", 2f);
        PropertyValuesHolder propertyValuesHolder2 = PropertyValuesHolder.ofFloat("scaleY", 2f);
        PropertyValuesHolder propertyValuesHolder3 = PropertyValuesHolder.ofFloat("alpha", 0f);
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofPropertyValuesHolder(wave, propertyValuesHolder1, propertyValuesHolder2, propertyValuesHolder3);
        objectAnimator1.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator1.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator1.setDuration(2000);
        objectAnimator1.start();
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofArgb(wave, "color", Color.BLUE);
        objectAnimator2.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator2.setDuration(2000);
        objectAnimator2.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isMusic = false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        this.activity = (HomeActivity) context;
        this.handler1 = activity.getHandler();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_music_last:
                handler1.sendEmptyMessage(1);
                break;
            case R.id.img_music_play:
                if (isPlaying) {
                    mimg_music_stop.setImageResource(R.mipmap.playblue);
                    isPlaying = false;
                } else {
                    seekBarRun();
                    mimg_music_stop.setImageResource(R.mipmap.stop);
                    isPlaying = true;
                }
                handler1.sendEmptyMessage(2);

                break;
            case R.id.img_music_next:
                handler1.sendEmptyMessage(3);
                break;
            case R.id.img_music_shaffle:
            case R.id.img_music_repeat:
                int min=10;
                int max=99;
                Random random = new Random();
                int num = random.nextInt(max)%(max-min+1) + min;
                Toast.makeText(context,"随机播放没做出来，给你roll一个数吧^w^:" + num,Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void seekBarRun() {
        MyThreadPool myThreadPool = new MyThreadPool();
        myThreadPool.setMaxThread(MyThreadPool.USE_FIXEDTHREADPOOL).setMaxThread(1).setTaskNumber(1).setRunnable(new Runnable() {
            @Override
            public void run() {
                try {
                    while (running) {
                        MusicBean nowBean = activity.getNowBean();
                        if (nowBean != null) {
                            long musicDuration = Integer.parseInt(nowBean.getTime());
                            final long position = activity.getPlayTime();
                            if (isPlaying) {
                                Message message = new Message();
                                Bundle bundle = new Bundle();
                                bundle.putLong("musicDuration", musicDuration);
                                bundle.putLong("position", position);
                                message.what = 100;
                                message.setData(bundle);
                                handler.sendMessage(message);
                            }


                        }
                        Thread.sleep(500);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).go();
        running = true;


    }
}