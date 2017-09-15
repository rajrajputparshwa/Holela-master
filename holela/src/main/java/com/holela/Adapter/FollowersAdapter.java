package com.holela.Adapter;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.holela.Activity.Tabs;
import com.holela.Fragment.Followerslist;
import com.holela.Models.FollowersModel;
import com.holela.R;
import com.holela.Utils.Config;
import com.holela.Utils.Pref_Master;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 6/17/2017.
 */

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.Myiew> {

    FragmentActivity context;
    ArrayList<FollowersModel> follower_list = new ArrayList<>();
    FragmentManager manager;
    Pref_Master pref_master;
    FragmentTransaction transaction;

    public FollowersAdapter(FragmentActivity context, ArrayList<FollowersModel> follower_list, FragmentManager manager) {
        this.context = context;
        this.follower_list = follower_list;
        this.manager = manager;
    }

    public class Myiew extends RecyclerView.ViewHolder {


        CircleImageView circleImageView;
        TextView serachusername;
        Button unfollow,follow;
        String userid;

        public Myiew(View itemView) {
            super(itemView);

            circleImageView= (CircleImageView) itemView.findViewById(R.id.searchuserimage);
            serachusername= (TextView) itemView.findViewById(R.id.searchusername);
            unfollow = (Button) itemView.findViewById(R.id.unfollow);
            follow = (Button) itemView.findViewById(R.id.follow);
            pref_master = new Pref_Master(context);


        }
    }

    @Override
    public FollowersAdapter.Myiew onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.followersview, parent, false);


        return new Myiew(itemView);
    }

    @Override
    public void onBindViewHolder(final FollowersAdapter.Myiew holder, final int position) {
        String img = follower_list.get(position).getImage();
        final String userid = follower_list.get(position).getUserid();

        if (img.equals("")){

            Picasso.with(context)
                    .load(R.drawable.ic_defalult)
                    .placeholder(R.drawable.ic_defalult)
                    .into(holder.circleImageView);
        }else {

            Picasso.with(context)
                    .load(follower_list.get(position).getImage())
                    .placeholder(R.drawable.ic_defalult)
                    .into(holder.circleImageView);
        }

        holder.userid=follower_list.get(position).getUserid();

        holder.serachusername.setText(follower_list.get(position).getUsernamename());

        String following = follower_list.get(position).getFollowing();


        if (following.equals("0")){
            holder.unfollow.setVisibility(View.GONE);
            holder.follow.setVisibility(View.VISIBLE);
        }else if (following.equals("1")){
            holder.unfollow.setVisibility(View.VISIBLE);
            holder.follow.setVisibility(View.GONE);
        }

        holder.unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str = follower_list.get(position).getUserid();

                Followerslist.Unfollow(context,manager,userid,position);

                holder.unfollow.setVisibility(View.GONE);
                holder.follow.setVisibility(View.VISIBLE);

            }
        });


        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str = follower_list.get(position).getUserid();

                Followerslist.Follow(context,manager,userid);

                holder.unfollow.setVisibility(View.VISIBLE);
                holder.follow.setVisibility(View.GONE);

            }
        });


        holder.serachusername.setOnClickListener(new View.OnClickListener() {
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
        return follower_list.size();
    }

}
