package com.holela.Adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.VideoView;

import com.holela.Fragment.OtherUserMedia;
import com.holela.Models.OtherUserMEdiaModel;
import com.holela.Models.UserMediaModel;
import com.holela.R;

import java.util.ArrayList;

/**
 * Created by admin on 6/9/2017.
 */

public class OtherUserMediaAdapter extends BaseAdapter{
    Context context;
    static int transfer;
    ArrayList<OtherUserMEdiaModel> array_image_story=new ArrayList<>();
    LayoutInflater layoutInflater;
    ViewHolder holder;
    static String postid ;



    public OtherUserMediaAdapter(Context context, ArrayList<OtherUserMEdiaModel> array_image_story) {
        this.context = context;
        this.array_image_story = array_image_story;
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

        LayoutInflater inflater = LayoutInflater.from(context);

        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.usermedia, null);

            holder.recyclerView = (RecyclerView) convertView.findViewById(R.id.usermedia);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        postid = array_image_story.get(position).getPostid();
        ArrayList singleSectionItems = array_image_story.get(position).getImageModels();

        OtherUSerMediaimgAdapter otherUSerMediaimgAdapter = new OtherUSerMediaimgAdapter(context, singleSectionItems , postid);
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(otherUSerMediaimgAdapter);
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
