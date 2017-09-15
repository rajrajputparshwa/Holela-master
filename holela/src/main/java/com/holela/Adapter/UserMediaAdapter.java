package com.holela.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.VideoView;

import com.holela.Models.UserMediaModel;
import com.holela.R;

import java.util.ArrayList;

/**
 * Created by admin on 6/9/2017.
 */

public class UserMediaAdapter extends BaseAdapter {

    Context context;
    ArrayList<UserMediaModel> array_image_story = new ArrayList<>();
    FragmentManager manager;
    static int transfer;
    LayoutInflater layoutInflater;
    ViewHolder holder;
    static String postid ;


    public UserMediaAdapter(Context context, ArrayList<UserMediaModel> array_image_story, FragmentManager manager) {
        this.context = context;
        this.array_image_story = array_image_story;
        this.manager = manager;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return array_image_story.size();
    }

    @Override
    public Object getItem(int position) {
        return array_image_story.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        holder = new ViewHolder();
        UserMediaModel userMediaModel = array_image_story.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);

        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.usermedia, null);

            holder.recyclerView = (RecyclerView) convertView.findViewById(R.id.usermedia);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        ArrayList singleSectionItems = array_image_story.get(position).getImageModels();

        UserMediaImgAdapter userMediaImgAdapter = new UserMediaImgAdapter(context, singleSectionItems,array_image_story.get(position).getPostid());
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(userMediaImgAdapter);
        holder.recyclerView.setNestedScrollingEnabled(false);


        if (array_image_story.get(position).getPosttype().equals("1")) {
            holder.recyclerView.setVisibility(View.VISIBLE);
            postid = array_image_story.get(position).getPostid();

        }
        if (array_image_story.get(position).getPosttype().equals("2")) {
            transfer = 1;
            holder.recyclerView.setVisibility(View.VISIBLE);
            postid = array_image_story.get(position).getPostid();

        }


        return convertView;
    }

    private class ViewHolder {
        RecyclerView recyclerView;


    }
}
