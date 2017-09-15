package com.holela.Adapter;

import android.app.Activity;
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
 * Created by admin on 5/24/2017.
 */

public class Notification_Photo extends RecyclerView.Adapter<Notification_Photo.MyView> {

    Context context;
    ArrayList array_notification_photo =new ArrayList();

    public Notification_Photo(Context activity, ArrayList array_notification_photo) {
        this.context = activity;
        this.array_notification_photo = array_notification_photo;
    }

    public class MyView extends RecyclerView.ViewHolder {

        ImageView imageView;

        public MyView(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.story_pic_array);
        }
    }

    @Override
    public Notification_Photo.MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_photo_array, parent, false);



        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(Notification_Photo.MyView holder, int position) {

    }

    @Override
    public int getItemCount() {
        return array_notification_photo.size();
    }

}
