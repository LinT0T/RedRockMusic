package com.lint0t.redrockmusic.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lint0t.redrockmusic.R;
import com.lint0t.redrockmusic.bean.MyBean;
import com.lint0t.redrockmusic.model.InitDatas;

import java.util.List;

public class ProfileFragment extends Fragment {

    private TextView mtv_name, mtv_city;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 4:
                    List<String> datas = (List<String>) msg.obj;
                    mtv_name.setText(datas.get(0));
                    mtv_city.setText(datas.get(1));
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mtv_city = view.findViewById(R.id.tv_profile_city);
        mtv_name = view.findViewById(R.id.tv_profile_name);
        InitDatas initDatas = new InitDatas();
        initDatas.initProfile(MyBean.baseURL + "/user/detail?uid=318082831", handler);
    }
}
