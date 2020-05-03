package com.lint0t.redrockmusic.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lint0t.redrockmusic.R;
import com.lint0t.redrockmusic.adapter.MyHomeRecycleViewAdapter;
import com.lint0t.redrockmusic.adapter.MyLibraryRecycleViewAdapter;
import com.lint0t.redrockmusic.bean.LibraryBean;
import com.lint0t.redrockmusic.bean.MyBean;
import com.lint0t.redrockmusic.model.InitDatas;

import java.util.ArrayList;
import java.util.List;


public class LibraryFragment extends Fragment {

  //  private static SelectListener listener;
    private RecyclerView mrv_library;
    private PlayListFragment playListFragment;
    private MyLibraryRecycleViewAdapter adapter;
    private FragmentManager fragmentManager;
    private List<LibraryBean> libraryBeans = new ArrayList<>();
    @SuppressLint("HandlerLeak")
    private Handler hander = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 3:
                    libraryBeans.clear();
                    libraryBeans.addAll((List<LibraryBean>) msg.obj);
                    adapter.notifyItemInserted(libraryBeans.size() - 1);
                    adapter.notifyDataSetChanged();

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
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mrv_library = view.findViewById(R.id.rv_library);
        InitDatas initDatas = new InitDatas();
        initDatas.initLibrary(MyBean.baseURL + "/user/playlist?uid=318082831", hander);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mrv_library.setLayoutManager(manager);
        LibraryBean libraryBean = new LibraryBean("歌曲", "歌手", null, 0, 0);
        libraryBeans.add(libraryBean);
        fragmentManager = getActivity().getSupportFragmentManager();
        playListFragment = new PlayListFragment();
        adapter = new MyLibraryRecycleViewAdapter(libraryBeans, getContext());
        mrv_library.setAdapter(adapter);
        adapter.setOnItemClickListener(new MyHomeRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                playListFragment = PlayListFragment.newInstance(String.valueOf(libraryBeans.get(position).getId()));
                fragmentManager.beginTransaction().replace(R.id.frm_home, playListFragment, "playList").commitAllowingStateLoss();
              //  listener.returnWhich(1);
            }
        });
    }
//    public interface SelectListener {
//        public void returnWhich(int inWhich);
//    }
//
//    public static void setSelectListener(SelectListener myListener) {
//        listener = myListener;
//    }


}
