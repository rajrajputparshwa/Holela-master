package com.holela.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.holela.Activity.Playvideo;
import com.holela.Models.NotificationImageModel;
import com.holela.R;

import java.util.ArrayList;

/**
 * Created by admin on 6/16/2017.
 */

public class NotificationImageRecyclerAdaper extends  RecyclerView.Adapter<NotificationImageRecyclerAdaper.MyView>  {
    Context context;
    ArrayList<NotificationImageModel> post_image_list = new ArrayList<>();

    public NotificationImageRecyclerAdaper(Context context, ArrayList<NotificationImageModel> post_image_list) {
        this.context = context;
        this.post_image_list = post_image_list;
    }


    public class MyView extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView videoView;
        ImageView play;

        public MyView(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.img);
            videoView = (ImageView) itemView.findViewById(R.id.videoview2);
            play= (ImageView) itemView.findViewById(R.id.plaayyyy);

        }
    }

    @Override
    public NotificationImageRecyclerAdaper.MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item, parent, false);

        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(NotificationImageRecyclerAdaper.MyView holder, int position) {
        NotificationImageModel notificationImageModel = post_image_list.get(position);

        holder.imageView.setVisibility(View.VISIBLE);
        holder.videoView.setVisibility(View.GONE);
        holder.play.setVisibility(View.GONE);



        Glide.with(context)
                .load(post_image_list.get(position).getImage())
                .placeholder(R.color.White)
                .override(25,25)
                .into(holder.imageView);

        if (Notificationyou.transfer == 1) {

            Notificationyou.transfer=0;

            holder.play.setVisibility(View.VISIBLE);
            holder.imageView.setVisibility(View.GONE);
            holder.videoView.setVisibility(View.VISIBLE);



        }
    }

    @Override
    public int getItemCount() {
        return post_image_list.size();
    }

}
