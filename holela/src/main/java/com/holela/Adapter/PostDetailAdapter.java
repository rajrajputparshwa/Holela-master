package com.holela.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.holela.Activity.PostDetail;
import com.holela.Models.PostDetailModel;
import com.holela.R;

import java.util.ArrayList;

/**
 * Created by admin on 6/5/2017.
 */

public class PostDetailAdapter extends RecyclerView.Adapter<PostDetailAdapter.MYview> {


    Context context;
    ArrayList<PostDetailModel> postdetail_list = new ArrayList<>();

    public PostDetailAdapter(Context context, ArrayList<PostDetailModel> postdetail_list) {
        this.context = context;
        this.postdetail_list = postdetail_list;
    }


    public class MYview extends RecyclerView.ViewHolder {

        ImageView imageView, Thumb;
        VideoView videoView;
        ImageView play;
        SurfaceHolder surfaceHolder;
        private MediaController mediaControls;


        public MYview(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.img);
            videoView = (VideoView) itemView.findViewById(R.id.videoview2);
            play = (ImageView) itemView.findViewById(R.id.plaayyyy);
            Thumb = (ImageView) itemView.findViewById(R.id.thumb);

        }
    }

    @Override
    public MYview onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pager_item, parent, false);

        return new MYview(itemView);
    }

    @Override
    public void onBindViewHolder(final MYview holder, final int position) {
        PostDetailModel postDetailModel = postdetail_list.get(position);

        holder.imageView.setVisibility(View.VISIBLE);
        holder.videoView.setVisibility(View.GONE);
        holder.Thumb.setVisibility(View.GONE);
        Glide.with(context)
                .load(postdetail_list.get(position).getImage())
                .override(512, 512)
                .placeholder(R.color.White)
                .into(holder.imageView);

        if (PostDetail.transferrr == 1) {
            Glide.with(context)
                    .load(postdetail_list.get(position).getThumb())
                    .override(512, 512)
                    .placeholder(R.color.White)
                    .into(holder.Thumb);

            holder.imageView.setVisibility(View.GONE);
            holder.Thumb.setVisibility(View.VISIBLE);
            holder.play.setVisibility(View.VISIBLE);
            holder.videoView.setVisibility(View.VISIBLE);
            final String img = postdetail_list.get(position).getImage();


            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading");

            holder.videoView.setVideoPath(img);

            holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {


                    holder.Thumb.setVisibility(View.GONE);
                    holder.play.setVisibility(View.GONE);
                    mp.start();


                }
            });

            holder.videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int i1) {
                    if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {

                        holder.Thumb.setVisibility(View.VISIBLE);
                        holder.play.setVisibility(View.VISIBLE);
                    }
                    if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                        holder.Thumb.setVisibility(View.GONE);
                        holder.play.setVisibility(View.GONE);
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


            PostDetail.transferrr = 0;


        }
    }

    @Override
    public int getItemCount() {
        return postdetail_list.size();
    }

}
