package com.lint0t.redrockmusic.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lint0t.redrockmusic.R;
import com.lint0t.redrockmusic.activity.HomeActivity;
import com.lint0t.redrockmusic.adapter.MyPlayListRecycleViewAdapter;
import com.lint0t.redrockmusic.bean.MusicBean;
import com.lint0t.redrockmusic.bean.MusicCache;
import com.lint0t.redrockmusic.bean.MusicCacheList;
import com.lint0t.redrockmusic.bean.MyBean;
import com.lint0t.redrockmusic.bean.PlayListBean;
import com.lint0t.redrockmusic.model.InitDatas;
import com.lint0t.redrockmusic.utils.MyImageLoader;

import java.util.ArrayList;
import java.util.List;

public class PlayListFragment extends Fragment {

    //    private IBackInterface backInterface;
    private Handler handler1;
    public static boolean inPlayList;
    private String id;
    private RecyclerView mrv_play_list;
    private MyPlayListRecycleViewAdapter myPlayListRecycleViewAdapter;
    private List<PlayListBean> playListBeans = new ArrayList<>();
    private TextView mtv_name, mtv_author, mtv_tracks;
    private ImageView mimg_cover;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 5:
                    playListBeans.clear();
                    playListBeans.addAll((List<PlayListBean>) msg.obj);
                    mtv_tracks.setText(String.valueOf(playListBeans.get(0).getTracks()) + " Tracks");
                    myPlayListRecycleViewAdapter.notifyItemInserted(playListBeans.size() - 1);
                    myPlayListRecycleViewAdapter.notifyDataSetChanged();
                    handler1.sendEmptyMessage(5);
                    break;
                case 400:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    System.out.println(bitmap);
                    if (bitmap != null) {
                        mimg_cover.setImageBitmap(bitmap);
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_list, container, false);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString("param");
            this.id = mParam1;
        }
//        backInterface = (IBackInterface)getActivity();
//        backInterface.setSelectedFragment(this);//将fragment传递到Activity中
        inPlayList = true;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mrv_play_list = view.findViewById(R.id.rv_play_list);
        mtv_name = view.findViewById(R.id.tv_play_list_name);
        mtv_author = view.findViewById(R.id.tv_play_list_author);
        mimg_cover = view.findViewById(R.id.img_play_list_cover);
        mtv_tracks = view.findViewById(R.id.tv_play_list_tracks);
        PlayListBean playListBean = new PlayListBean("加载中", "稍等片刻", null, 0, 0, 0, 0, 0);
        playListBeans.add(playListBean);
        myPlayListRecycleViewAdapter = new MyPlayListRecycleViewAdapter(playListBeans, getContext());
        mrv_play_list.setAdapter(myPlayListRecycleViewAdapter);
        InitDatas initDatas = new InitDatas();
        initDatas.initPlayList(MyBean.baseURL + "/playlist/detail?id=" + id, handler);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mrv_play_list.setLayoutManager(manager);
        myPlayListRecycleViewAdapter.setOnItemClickListener(new MyPlayListRecycleViewAdapter.OnItemClickListener2() {
            @Override
            public void onClick(int position) {
                PlayListBean bean = playListBeans.get(position);
                mtv_name.setText(bean.getName());
                mtv_author.setText(bean.getAuthor());
                String url = bean.getUrl();
                MusicCache.setNowPos(position);
                Message message = new Message();
                message.what = 4;
                message.arg1 = position;
                handler1.sendMessage(message);
                System.out.println(position);
                MusicCache.load(bean);
                if (url != null) {
                    MyImageLoader myImageLoader = new MyImageLoader(getContext());
                    myImageLoader.setURL(url).setHandler(handler).setUseCache(true).load();
                }

            }
        });
    }

    public static PlayListFragment newInstance(String text) {
        PlayListFragment fragment = new PlayListFragment();
        Bundle args = new Bundle();
        args.putString("param", text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        inPlayList = false;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        HomeActivity activity = (HomeActivity) context;
        handler1 = activity.getHandler();
    }
}
