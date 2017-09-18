package com.holela.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.holela.Fragment.Home;
import com.holela.Models.ImageModel;
import com.holela.R;

import java.util.ArrayList;

/**
 * Created by admin on 6/3/2017.
 */

public class Userimage_Adapter extends RecyclerView.Adapter<Userimage_Adapter.MyView> {


    Context context;
    ArrayList<ImageModel> post_image_list = new ArrayList<>();
    String postid;

    public Userimage_Adapter(Context context, ArrayList<ImageModel> post_image_list, String postid) {
        this.context = context;
        this.post_image_list = post_image_list;
        this.postid = postid;
    }

    public class MyView extends RecyclerView.ViewHolder {

        ImageView imageView, Thumb;
        VideoView videoView;
        ImageView play;
        ProgressBar progressBar;
        private MediaController mediaControls;

        public MyView(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.img);
            videoView = (VideoView) itemView.findViewById(R.id.videoview2);
            play = (ImageView) itemView.findViewById(R.id.plaayyyy);
            Thumb = (ImageView) itemView.findViewById(R.id.thumb);
            progressBar = (ProgressBar) itemView.findViewById(R.id.loading);

        }
    }


    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pager_item, parent, false);

        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final MyView holder, int position) {
        ImageModel imageModel = post_image_list.get(position);

        holder.imageView.setVisibility(View.VISIBLE);
        holder.videoView.setVisibility(View.GONE);
        holder.Thumb.setVisibility(View.GONE);
        holder.progressBar.setVisibility(View.VISIBLE);
        Glide.with(context)
                .load(post_image_list.get(position).getImage())
                .override(512,512)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                        holder.progressBar.setVisibility(View.GONE);

                        return false;
                    }
                })
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           /*     Intent i = new Intent(context, PostDetail.class);
                i.putExtra("postid", postid);
                i.putExtra("intent","profile");
                context.startActivity(i);*/
            }
        });




        if (Home.transfer == 1) {
            holder.progressBar.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(post_image_list.get(position).getThumb())
                    .override(512,512)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.Thumb);

            holder.imageView.setVisibility(View.GONE);
            holder.Thumb.setVisibility(View.VISIBLE);
            holder.play.setVisibility(View.VISIBLE);
            holder.videoView.setVisibility(View.VISIBLE);
            final String img = post_image_list.get(position).getImage();


            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading");

            holder.Thumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  /*  Intent i = new Intent(context, PostDetail.class);
                    i.putExtra("postid", postid);
                    i.putExtra("intent","profile");
                    context.startActivity(i);*/
                }
            });
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

            holder.videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
               /*     Intent i = new Intent(context, PostDetail.class);
                    i.putExtra("postid", postid);
                    i.putExtra("intent","profile");
                    context.startActivity(i);*/
                }
            });


            Home.transfer = 0;


        }
    }

    @Override
    public int getItemCount() {

        return (null != post_image_list ? post_image_list.size() : 0);
    }

}
