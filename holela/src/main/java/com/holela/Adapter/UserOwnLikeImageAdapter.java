package com.holela.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.holela.Activity.PostDetail;
import com.holela.Models.UserOWnLikeImage;
import com.holela.R;

import java.util.ArrayList;

/**
 * Created by admin on 6/9/2017.
 */

public class UserOwnLikeImageAdapter extends RecyclerView.Adapter<UserOwnLikeImageAdapter.Myview> {


    Context context;
    ArrayList<UserOWnLikeImage> post_image_list = new ArrayList<>();
    String postid;


    public UserOwnLikeImageAdapter(Context context, ArrayList<UserOWnLikeImage> post_image_list,String postid) {
        this.context = context;
        this.post_image_list = post_image_list;
        this.postid = postid;
    }

    public class Myview extends RecyclerView.ViewHolder {

        ImageView imageView,Thumb;
        VideoView videoView;
        ImageView play;
        private MediaController mediaControls;

        public Myview(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.img);
            videoView = (VideoView) itemView.findViewById(R.id.videoview2);
            play = (ImageView) itemView.findViewById(R.id.plaayyyy);
            Thumb = (ImageView) itemView.findViewById(R.id.thumb);


        }
    }

    @Override
    public Myview onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pager_item, parent, false);

        return new Myview(itemView);
    }

    @Override
    public void onBindViewHolder(final Myview holder, int position) {
        UserOWnLikeImage userOWnLikeImage = post_image_list.get(position);

        holder.imageView.setVisibility(View.VISIBLE);
        holder.videoView.setVisibility(View.GONE);
        holder.Thumb.setVisibility(View.GONE);
        Glide.with(context)
                .load(post_image_list.get(position).getImage())
                .override(512, 512)
                .placeholder(R.color.White)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, PostDetail.class);
                i.putExtra("postid", postid);
                i.putExtra("intent","profile");
                context.startActivity(i);
            }
        });

        if (UserOwnLikeAdapter.transferdfdf == 1) {
            Glide.with(context)
                    .load(post_image_list.get(position).getThumb())
                    .override(512, 512)
                    .placeholder(R.color.White)
                    .into(holder.Thumb);

            holder.imageView.setVisibility(View.GONE);
            holder.Thumb.setVisibility(View.VISIBLE);
            holder.videoView.setVisibility(View.VISIBLE);
            final String img = post_image_list.get(position).getImage();


            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading");
            holder.Thumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, PostDetail.class);
                    i.putExtra("postid", postid);
                    i.putExtra("intent","profile");
                    context.startActivity(i);
                }
            });


            holder.videoView.setVideoPath(img);

            holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {


                    holder.Thumb.setVisibility(View.GONE);
                    mp.start();


                }
            });

            holder.videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int i1) {
                    if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {

                        holder.Thumb.setVisibility(View.VISIBLE);
                    }
                    if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                        holder.Thumb.setVisibility(View.GONE);
                    }
                    return false;
                }
            });

            holder.videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {

                    return true;
                }
            });


            UserOwnLikeAdapter.transferdfdf = 0;


        }
    }

    @Override
    public int getItemCount() {
        return post_image_list.size();
    }

}
