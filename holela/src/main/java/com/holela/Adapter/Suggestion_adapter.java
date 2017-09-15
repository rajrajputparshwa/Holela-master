package com.holela.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.holela.Activity.Tabs;
import com.holela.Fragment.Home;
import com.holela.Fragment.Profile;
import com.holela.Models.StoryModel;
import com.holela.Models.SuggestionModel;
import com.holela.R;
import com.holela.Utils.Config;
import com.holela.Utils.Pref_Master;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Raj on 7/19/2017.
 */

public class Suggestion_adapter extends RecyclerView.Adapter<Suggestion_adapter.MyView> {

    Context context;
    ArrayList<SuggestionModel> suggestionModels = new ArrayList<>();
    Pref_Master pref_master;

    public Suggestion_adapter(Context context, ArrayList<SuggestionModel> suggestionModels) {
        this.context = context;
        this.suggestionModels = suggestionModels;
    }

    public class MyView extends RecyclerView.ViewHolder {

        ImageView suggestionimage;
        TextView suggestionname;
        Button followbtn, unfollow;


        public MyView(View itemView) {
            super(itemView);


            suggestionimage = (ImageView) itemView.findViewById(R.id.suggestionimage);
            suggestionname = (TextView) itemView.findViewById(R.id.username);
            followbtn = (Button) itemView.findViewById(R.id.follow);
            unfollow = (Button) itemView.findViewById(R.id.unfollow);
            pref_master = new Pref_Master(context);

        }
    }

    @Override
    public Suggestion_adapter.MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.suggestion, parent, false);


        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final Suggestion_adapter.MyView holder, final int position) {

        String img = suggestionModels.get(position).getImagepath();

        final String userid = suggestionModels.get(position).getUserid();


        if (img.equals("")) {

            Picasso.with(context)
                    .load(R.drawable.user)
                    .placeholder(R.drawable.user)
                    .into(holder.suggestionimage);
        } else {

            Picasso.with(context)
                    .load(suggestionModels.get(position).getImagepath())
                    .placeholder(R.drawable.user)
                    .into(holder.suggestionimage);
        }


        holder.suggestionname.setText(suggestionModels.get(position).getFullname());
        holder.followbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Home.Follow(context, suggestionModels.get(position).getUserid());


                holder.followbtn.setVisibility(View.GONE);
                holder.unfollow.setVisibility(View.VISIBLE);
            }
        });

        holder.unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Home.Unfollow(context, suggestionModels.get(position).getUserid());
                holder.followbtn.setVisibility(View.VISIBLE);
                holder.unfollow.setVisibility(View.GONE);
            }
        });

        holder.suggestionname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, Tabs.class);
                intent.putExtra("fragmentcode", Config.Fragment_ID.Userother);
                intent.putExtra("id", userid);
                pref_master.setOtherUserid(userid);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.suggestionimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Tabs.class);
                intent.putExtra("fragmentcode", Config.Fragment_ID.Userother);
                intent.putExtra("id", userid);
                pref_master.setOtherUserid(userid);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return suggestionModels.size();
    }

}
