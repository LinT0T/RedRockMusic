package com.lint0t.redrockmusic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lint0t.redrockmusic.R;
import com.lint0t.redrockmusic.bean.LibraryBean;
import com.lint0t.redrockmusic.bean.PlayListBean;
import com.lint0t.redrockmusic.utils.MyImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MyPlayListRecycleViewAdapter extends RecyclerView.Adapter<MyPlayListRecycleViewAdapter.MyViewHolder> {

    List<PlayListBean> datas = new ArrayList<>();
    Context context;

    public MyPlayListRecycleViewAdapter(List<PlayListBean> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.play_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //绑定数据
        PlayListBean playListBean = datas.get(position);
        if (playListBean.getId() != 0) {
            holder.mtv_number.setText(String.valueOf(playListBean.getNum()));
            holder.mtv_name.setText(playListBean.getName());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
            String time = simpleDateFormat.format(new Date(playListBean.getDt()));
            holder.mtv_time.setText(time);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {

        if (datas == null) {
            return 1;
        } else {
            return datas.size();
        }
    }
//library
    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mtv_name, mtv_time,mtv_number;

        //findviewbyid
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mtv_number = itemView.findViewById(R.id.tv_play_list_number_item);
            mtv_name = itemView.findViewById(R.id.tv_play_list_name_item);
            mtv_time = itemView.findViewById(R.id.tv_play_list_time_item);
        }
    }

    //第一步 定义接口
    public interface OnItemClickListener2 {
        void onClick(int position);
    }

    private OnItemClickListener2 listener;

    //第二步， 写一个公共的方法
    public void setOnItemClickListener(OnItemClickListener2 listener) {
        this.listener = listener;
    }



    public interface OnItemLongClickListener {
        void onClick(int position);
    }

    private OnItemLongClickListener longClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }
}
