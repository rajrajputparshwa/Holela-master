package com.holela.Adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.holela.Activity.GetUserStory;
import com.holela.Models.GetUserStoryModel;
import com.holela.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Raj on 7/18/2017.
 */
public class GetOtherUserStory extends RecyclerView.Adapter<GetOtherUserStory.MyView> {

    Context mContext;
    ArrayList<GetUserStoryModel> userstory_list = new ArrayList<>();
    android.view.View View;
    ImageView story;
    public static String filetype;
    private ObjectAnimator progressAnimator;
    Runnable runnable;
    CountDownTimer timer;
    public static int size = 0;
    public static int pos = 0;
    public static int lastindex = 0;
    public static int videoduration = 0;




    public GetOtherUserStory(Context mContext, ArrayList<GetUserStoryModel> userstory_list, Runnable runnable) {
        this.mContext = mContext;
        this.userstory_list = userstory_list;
        this.runnable = runnable;
    }

    public static class MyView extends RecyclerView.ViewHolder {

        ImageView story, userimage;
        TextView username, time;
        VideoView videoView;
        ProgressBar progressBar, timeline;

        public MyView(View itemView) {
            super(itemView);


            story = (ImageView) itemView.findViewById(R.id.mystoryy);
            username = (TextView) itemView.findViewById(R.id.storyusername);
            time = (TextView) itemView.findViewById(R.id.storytime);
            videoView = (VideoView) itemView.findViewById(R.id.videoviewww);
            progressBar = (ProgressBar) itemView.findViewById(R.id.loading);
            userimage = (ImageView) itemView.findViewById(R.id.userimage);
        }
    }


    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.story_pager_item, parent, false);


        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final MyView holder, final int position) {

        filetype = userstory_list.get(position).getFiletype();

        Log.e("Size", "" + userstory_list.size());

        size = userstory_list.size();

        pos = position;

        lastindex = userstory_list.size() - 1;

        holder.username.setText(userstory_list.get(position).getUsername());
        Picasso.with(mContext)
                .load(userstory_list.get(position).getProfileimage())
                .placeholder(R.drawable.user)
                .into(holder.userimage);

        holder.time.setText(userstory_list.get(position).getDatetime());

        if (userstory_list.get(position).getFiletype().equals("1")) {
            holder.story.setVisibility(android.view.View.VISIBLE);
            holder.videoView.setVisibility(android.view.View.GONE);


            Picasso.with(mContext)
                    .load(userstory_list.get(position).getImagepath())
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(holder.story, new Callback() {
                        @Override
                        public void onSuccess() {

                            timer = new CountDownTimer(1000, 1000) {
                                public void onTick(long millisUntilFinished) {
                                }

                                public void onFinish() {

                                    holder.progressBar.setVisibility(View.GONE);

                                    if (mContext instanceof GetUserStory) {
                                        ((GetUserStory) mContext).setProgress();
                                    }
                                }
                            };
                            timer.start();


                        }

                        @Override
                        public void onError() {

                        }
                    });


        }
        if (userstory_list.get(position).getFiletype().equals("2")) {

            holder.videoView.setVisibility(android.view.View.VISIBLE);
            holder.story.setVisibility(android.view.View.GONE);
            holder.videoView.setVideoPath((userstory_list.get(position).getImagepath()));

            Log.e("VideoSize", "" + holder.videoView.getDuration());


            holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(final MediaPlayer mediaPlayer) {


                    timer = new CountDownTimer(2000, 2000) {
                        public void onTick(long millisUntilFinished) {


                        }

                        public void onFinish() {

                            holder.progressBar.setVisibility(View.GONE);
                            mediaPlayer.start();
                            videoduration = holder.videoView.getDuration();
                            if (mContext instanceof GetUserStory) {
                                ((GetUserStory) mContext).setVideoProgress();
                            }

                        }
                    };
                    timer.start();


                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return userstory_list.size();
    }


}