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
import com.lint0t.redrockmusic.bean.RecommsBean;
import com.lint0t.redrockmusic.utils.MyImageLoader;

import java.util.ArrayList;
import java.util.List;


public class MyLibraryRecycleViewAdapter extends RecyclerView.Adapter<MyLibraryRecycleViewAdapter.MyViewHolder> {

    List<LibraryBean> datas = new ArrayList<>();
    Context context;

    public MyLibraryRecycleViewAdapter(List<LibraryBean> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.library_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //绑定数据
        if (datas.get(position).getId() != 0) {

            MyImageLoader myImageLoader = new MyImageLoader(context);
            @SuppressLint("HandlerLeak")
            Handler handler = new Handler() {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case 400:
                            Bitmap bitmap = (Bitmap) msg.obj;
                            holder.mimg_cover.setImageBitmap(bitmap);
                            break;
                    }
                }
            };
            myImageLoader.setUseCache(true).setURL(datas.get(position).getUrl()).setHandler(handler).load();
            holder.mtv_name.setText(datas.get(position).getName());
            holder.mtv_author.setText(datas.get(position).getAuthor());
            holder.mtv_tracks.setText(datas.get(position).getTracks() + " Tracks");
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

        ImageView mimg_cover;
        TextView mtv_name, mtv_author,mtv_tracks;

        //findviewbyid
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mimg_cover = itemView.findViewById(R.id.img_library_item);
            mtv_name = itemView.findViewById(R.id.tv_library_name_item);
            mtv_author = itemView.findViewById(R.id.tv_library_author_item);
            mtv_tracks = itemView.findViewById(R.id.tv_library_tracks_item);
        }
    }

    //第一步 定义接口
    public interface OnItemClickListener {
        void onClick(int position);
    }

    private MyHomeRecycleViewAdapter.OnItemClickListener listener;

    //第二步， 写一个公共的方法
    public void setOnItemClickListener(MyHomeRecycleViewAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }



    public interface OnItemLongClickListener {
        void onClick(int position);
    }

    private MyHomeRecycleViewAdapter.OnItemLongClickListener longClickListener;

    public void setOnItemLongClickListener(MyHomeRecycleViewAdapter.OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }
}
