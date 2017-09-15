package com.holela.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.holela.R;

import java.util.ArrayList;

/**
 * Created by admin on 5/18/2017.
 */

public class Search_Image_Adapter extends RecyclerView.Adapter<Search_Image_Adapter.Myview>{

    Context context;
    ArrayList<String> array_image_story=new ArrayList<>();


    public Search_Image_Adapter(FragmentActivity activity, ArrayList<String> array_image_story) {
        this.context = activity;
        this.array_image_story = array_image_story;
    }

    public class Myview extends RecyclerView.ViewHolder {

        ImageView imageView;

        public Myview(View itemView) {
            super(itemView);

            imageView= (ImageView) itemView.findViewById(R.id.imageee);
        }
    }

    @Override
    public Search_Image_Adapter.Myview onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alert_image, parent, false);


        return new Myview(itemView);
    }

    @Override
    public void onBindViewHolder(Search_Image_Adapter.Myview holder, int position) {


    }

    @Override
    public int getItemCount() {
        return array_image_story.size();
    }

}
