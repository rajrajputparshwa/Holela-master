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
import com.holela.Models.OtherUserimgModel;
import com.holela.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 6/9/2017.
 */

public class OtherUSerMediaimgAdapter extends RecyclerView.Adapter<OtherUSerMediaimgAdapter.Myview> {

    Context context;
    ArrayList<OtherUserimgModel> post_own_image_list = new ArrayList<>();
    String postid;

    public OtherUSerMediaimgAdapter(Context context, ArrayList<OtherUserimgModel> post_own_image_list, String postid) {
        this.context = context;
        this.post_own_image_list = post_own_image_list;
        this.postid = postid;
    }

    public class Myview extends RecyclerView.ViewHolder {


        ImageView imageView;
        ImageView videoView;
        ImageView play;


        public Myview(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imggg);
            videoView = (ImageView) itemView.findViewById(R.id.videoview2);
            play = (ImageView) itemView.findViewById(R.id.plaayyyy);

        }
    }

    @Override
    public OtherUSerMediaimgAdapter.Myview onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.usermediaitem, parent, false);

        return new Myview(itemView);
    }

    @Override
    public void onBindViewHolder(OtherUSerMediaimgAdapter.Myview holder, final int position) {
        OtherUserimgModel otherUserimgModel = post_own_image_list.get(position);

        holder.imageView.setVisibility(View.VISIBLE);
        holder.videoView.setVisibility(View.GONE);
        Picasso.with(context)
                .load(post_own_image_list.get(position).getImage())
                .placeholder(R.color.White)
                .into(holder.imageView);


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, PostDetail.class);
                i.putExtra("postid", postid);
                i.putExtra("intent", "otheruser");
                context.startActivity(i);
            }
        });

        if (OtherUserMediaAdapter.transfer == 1) {
            final String img = post_own_image_list.get(position).getImage();
            OtherUserMediaAdapter.transfer = 0;
            holder.imageView.setVisibility(View.GONE);
            holder.videoView.setVisibility(View.VISIBLE);
            holder.play.setVisibility(View.VISIBLE);


            Glide.with(context)
                    .load(post_own_image_list.get(position).getThumb())
                    .override(70, 70)
                    .placeholder(R.color.White)
                    .into(holder.videoView);


            holder.videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, PostDetail.class);
                    i.putExtra("postid", postid);
                    i.putExtra("intent", "otheruser");
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
