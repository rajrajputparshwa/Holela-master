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
import com.holela.Fragment.FollowingList;
import com.holela.Models.FolowingListModel;
import com.holela.R;
import com.holela.Utils.Config;
import com.holela.Utils.Pref_Master;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 6/17/2017.
 */

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.Myview>{


    FragmentActivity context;
    ArrayList<FolowingListModel> following_list = new ArrayList<>();
    FragmentManager manager;
    Pref_Master pref_master;
    FragmentTransaction transaction;

    public FollowingAdapter(FragmentActivity activity, ArrayList<FolowingListModel> following_list, FragmentManager manager) {
        this.context = activity;
        this.following_list = following_list;
        this.manager = manager;
    }




    public class Myview extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView serachusername;
        Button unfollow;
        String userid;

        public Myview(View itemView) {
            super(itemView);


            circleImageView= (CircleImageView) itemView.findViewById(R.id.searchuserimage);
            serachusername= (TextView) itemView.findViewById(R.id.searchusername);
            unfollow = (Button) itemView.findViewById(R.id.unfollow);
            pref_master = new Pref_Master(context);



        }
    }

    @Override
    public FollowingAdapter.Myview onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.followinglistview, parent, false);


        return new Myview(itemView);
    }

    @Override
    public void onBindViewHolder(final FollowingAdapter.Myview holder, final int position) {
        String img = following_list.get(position).getImage();
        final String userid = following_list.get(position).getUserid();

        if (img.equals("")){

            Picasso.with(context)
                    .load(R.drawable.ic_defalult)
                    .placeholder(R.drawable.ic_defalult)
                    .into(holder.circleImageView);
        }else {

            Picasso.with(context)
                    .load(following_list.get(position).getImage())
                    .placeholder(R.drawable.ic_defalult)
                    .into(holder.circleImageView);
        }

        holder.userid=following_list.get(position).getUserid();

        holder.serachusername.setText(following_list.get(position).getUsernamename());

        holder.unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str = following_list.get(position).getUserid();

                FollowingList.Unfollow(context,manager,userid,position);

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
        return following_list.size();
    }

}
