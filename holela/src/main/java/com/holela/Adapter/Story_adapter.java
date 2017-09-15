package com.holela.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.holela.Activity.Camera_Activity;
import com.holela.Activity.GetUserStory;
import com.holela.Activity.StoryActivity;
import com.holela.Controller.Textview_open_regular;
import com.holela.Controller.Textview_open_semi_bold;
import com.holela.Models.Stody_Model;
import com.holela.Models.StoryModel;
import com.holela.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Admin on 5/13/2017.
 */

public class Story_adapter extends RecyclerView.Adapter<Story_adapter.Viewholder> {

    Context context;
    ArrayList<StoryModel> storyModelArrayList = new ArrayList<>();
    public static int pos = 0;

    public Story_adapter(Context context, ArrayList<StoryModel> storyModelArrayList) {
        this.context = context;
        this.storyModelArrayList = storyModelArrayList;
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        CircleImageView story_pic;
        Textview_open_regular story_name;


        public Viewholder(View itemView) {
            super(itemView);

            story_pic = (CircleImageView) itemView.findViewById(R.id.story_pic);
            story_name = (Textview_open_regular) itemView.findViewById(R.id.story_name);

        }
    }

    @Override
    public Story_adapter.Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_story_row, parent, false);


        return new Viewholder(itemView);
    }

    @Override
    public void onBindViewHolder(Story_adapter.Viewholder holder, int position) {

 /*       if (position==0){
            holder.story_name.setText(R.string.Your_Story);
            holder.story_pic.setImageResource(R.drawable.holela_circle);
            holder.story_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, StoryActivity.class);
                    context.startActivity(i);
                }
            });
        }*/

        pos = position;

        final String user = storyModelArrayList.get(position).getUserid();

        holder.story_name.setText(storyModelArrayList.get(position).getFullname());

        if(storyModelArrayList.get(position).getImagepath().equals("")){
            Glide.with(context)
                    .load(R.drawable.user)
                    .placeholder(R.drawable.user)
                    .into(holder.story_pic);
        } else {

            Picasso.with(context)
                    .load(storyModelArrayList.get(position).getImagepath())
                    .placeholder(R.drawable.user)
                    .into(holder.story_pic);
        }

        holder.story_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, GetUserStory.class);
                i.putExtra("user", user);
                i.putExtra("turn", "0");
                context.startActivity(i);



            }
        });


    }

    @Override
    public int getItemCount() {
        return storyModelArrayList.size();
    }


}
