package com.holela.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.holela.Activity.PostDetail;
import com.holela.Models.UserMediaImageModel;
import com.holela.R;

import java.util.ArrayList;

/**
 * Created by admin on 6/9/2017.
 */

public class UserMediaImgAdapter extends RecyclerView.Adapter<UserMediaImgAdapter.Myview> {
    Context context;
    ArrayList<UserMediaImageModel> post_own_image_list = new ArrayList<>();
    String id;


    public UserMediaImgAdapter(Context context, ArrayList<UserMediaImageModel> post_own_image_list , String id) {
        this.context = context;
        this.post_own_image_list = post_own_image_list;
        this.id=id;
    }

    public class Myview extends RecyclerView.ViewHolder {


        ImageView imageView;
        ImageView videoView;
        ImageView play;


        public Myview(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imggg);
            videoView = (ImageView) itemView.findViewById(R.id.videoview2);
            play= (ImageView) itemView.findViewById(R.id.plaayyyy);
        }
    }

    @Override
    public UserMediaImgAdapter.Myview onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.usermediaitem, parent, false);

        return new Myview(itemView);
    }

    @Override
    public void onBindViewHolder(UserMediaImgAdapter.Myview holder, int position) {
        UserMediaImageModel userMediaImageModel = post_own_image_list.get(position);

        holder.imageView.setVisibility(View.VISIBLE);

        Glide.with(context)
                .load(post_own_image_list.get(position).getImage()).override(200,200)
                .placeholder(R.color.White)
                .into(holder.imageView);


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PostDetail.class);
                i.putExtra("postid",id);
                i.putExtra("intent","profile");
                context.startActivity(i);
            }
        });



        if (UserMediaAdapter.transfer == 1) {
            final String img = post_own_image_list.get(position).getImage();
            UserMediaAdapter.transfer=0;
            holder.imageView.setVisibility(View.GONE);
            holder.videoView.setVisibility(View.VISIBLE);
            holder.play.setVisibility(View.VISIBLE);


            Glide.with(context)
                    .load(post_own_image_list.get(position).getThumb())
                    .override(200,200)
                    .placeholder(R.color.White)
                    .into(holder.videoView);

            holder.videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, PostDetail.class);
                    i.putExtra("postid",id);
                    i.putExtra("intent","profile");
                    context.startActivity(i);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return post_own_image_list.size();
    }

}
