package com.holela.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;
import com.holela.Activity.Tabs;
import com.holela.Fragment.OtherUser;
import com.holela.Fragment.Setting;
import com.holela.Models.SearchMOdel;
import com.holela.R;
import com.holela.Utils.Config;
import com.holela.Utils.Pref_Master;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 6/7/2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MYview> {


    Context context;
    FragmentManager manager;
    ArrayList<SearchMOdel> search_list = new ArrayList<>();
    FragmentTransaction transaction;
    Pref_Master pref_master;


    public SearchAdapter(Context context, ArrayList<SearchMOdel> search_list, FragmentManager manager) {
        this.context = context;
        this.search_list = search_list;
        this.manager = manager;
    }

    public class MYview extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView serachusername;
        public MYview(View itemView) {
            super(itemView);

            circleImageView= (CircleImageView) itemView.findViewById(R.id.searchuserimage);
            serachusername= (TextView) itemView.findViewById(R.id.searchusername);
            pref_master = new Pref_Master(context);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }
    }

    @Override
    public MYview onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.searchview, parent, false);



        return new MYview(itemView);
    }

    @Override
    public void onBindViewHolder(MYview holder, int position) {

        String img = search_list.get(position).getImage();
        final String userid = search_list.get(position).getUserid();

        if (img.equals("")){

            Picasso.with(context)
                    .load(R.drawable.user)
                    .placeholder(R.drawable.ic_defalult)
                    .into(holder.circleImageView);
        }else {

            Picasso.with(context)
                    .load(search_list.get(position).getImage())
                    .placeholder(R.drawable.user)
                    .into(holder.circleImageView);
        }


        holder.serachusername.setText(search_list.get(position).getUsernamename());




        holder.serachusername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(context, Tabs.class);
                intent.putExtra("fragmentcode", Config.Fragment_ID.Userother);
                intent.putExtra("id", userid);
                pref_master.setOtherUserid(userid);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

                InputMethodManager imm = (InputMethodManager) view.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });





    }

    @Override
    public int getItemCount() {
        return search_list.size();
    }

}
