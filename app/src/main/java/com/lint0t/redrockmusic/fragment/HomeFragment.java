package com.lint0t.redrockmusic.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lint0t.redrockmusic.R;
import com.lint0t.redrockmusic.adapter.MyHomeRecycleViewAdapter;
import com.lint0t.redrockmusic.bean.Bean;
import com.lint0t.redrockmusic.bean.ImageBean;
import com.lint0t.redrockmusic.bean.RecommsBean;
import com.lint0t.redrockmusic.model.InitDatas;
import com.lint0t.redrockmusic.utils.MyImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements View.OnTouchListener {

    private static SelectListener listener;
    private PlayListFragment playListFragment;
    private FragmentManager fragmentManager;
    private RecyclerView mrv_home;
    private List<Bean> datas;
    private ImageBean imageBean;
    private Banner banner;
    public static boolean inHome = true;
    private MyHomeRecycleViewAdapter recycleViewAdapter;
    private static List<String> url = new ArrayList<>(), title = new ArrayList<>();
    private List<RecommsBean> recommsBeans = new ArrayList<>();
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    datas = (List<Bean>) msg.obj;
                    for (int i = 0; i < datas.size(); i++) {
                        url.add(datas.get(i).getPicUrl());
                        title.add(datas.get(i).getName());
                    }
                    notifyBanner(url);
                    break;
                case 2:
                    recommsBeans.clear();
                    recommsBeans.addAll((List<RecommsBean>) msg.obj);
                    recycleViewAdapter.notifyItemInserted(recommsBeans.size() - 1);
                    recycleViewAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };


    private void notifyBanner(List<String> url) {
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                MyImageLoader myImageLoader = new MyImageLoader(getContext());
                @SuppressLint("HandlerLeak")
                Handler handler1 = new Handler() {
                    @SuppressLint("HandlerLeak")
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        switch (msg.what) {
                            case 400:
                                Bitmap bitmap = (Bitmap) msg.obj;
                                imageView.setImageBitmap(bitmap);
                                break;
                        }
                    }
                };
                myImageLoader.setURL((String) path).setHandler(handler1).setUseCache(true).load();

            }
        });
        banner.setBannerTitles(title).setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE).setOffscreenPageLimit(4)
                .setBannerAnimation(Transformer.ZoomIn).setIndicatorGravity(BannerConfig.CENTER).setDelayTime(3000);
        banner.setImages(url);
        banner.start();

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inHome = true;
        banner = view.findViewById(R.id.ban_home);
        mrv_home = view.findViewById(R.id.rv_home);
        InitDatas initDatas = new InitDatas();
        initDatas.initBannerData(Bean.baseURL + "/album/newest", handler);
        initDatas.initRecomms(Bean.baseURL + "/top/playlist" + "?limit=30&cat=古风", handler);
        fragmentManager = getActivity().getSupportFragmentManager();
        playListFragment = new PlayListFragment();
        //fragmentManager.beginTransaction().add(R.id.frm_home, playListFragment).commit();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        mrv_home.setHasFixedSize(true);
        mrv_home.setLayoutManager(layoutManager);
        mrv_home.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int position = layoutManager.findFirstVisibleItemPosition();
                if (position >= 7) {
                    Animation animBottomOut = AnimationUtils.loadAnimation(getContext(),
                            R.anim.bottom_out);
                    animBottomOut.setDuration(500);
                    if (banner.getVisibility() != View.GONE)
                        banner.startAnimation(animBottomOut);
                    banner.setVisibility(View.INVISIBLE);

                    banner.setVisibility(View.GONE);

                } else {
                    if (position <= 3) {
                        Animation animBottomIn = AnimationUtils.loadAnimation(getContext(),
                                R.anim.bottom_in);
                        animBottomIn.setDuration(500);
                        if (banner.getVisibility() != View.VISIBLE)
                            banner.startAnimation(animBottomIn);
                        banner.setVisibility(View.VISIBLE);
                    }


                }
            }
        });

        RecommsBean recommsBean = new RecommsBean("加载中", null, "稍等片刻", 0);
        recommsBeans.add(recommsBean);
        recycleViewAdapter = new MyHomeRecycleViewAdapter(recommsBeans, getContext());
        mrv_home.setAdapter(recycleViewAdapter);
        recycleViewAdapter.setOnItemClickListener(new MyHomeRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {

                playListFragment = PlayListFragment.newInstance(String.valueOf(recommsBeans.get(position).getId()));
                fragmentManager.beginTransaction().replace(R.id.frm_home, playListFragment, "playList").commitAllowingStateLoss();
                inHome = false;
                listener.returnWhich(1);
            }
        });

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }


    public interface SelectListener {
        public void returnWhich(int inWhich);
    }

    public static void setSelectListener(SelectListener myListener) {
        listener = myListener;
    }


}


