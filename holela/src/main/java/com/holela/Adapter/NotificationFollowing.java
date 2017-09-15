package com.holela.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.holela.Activity.PostDetail;
import com.holela.Activity.Tabs;
import com.holela.Fragment.OtherUser;
import com.holela.Models.NotificationModel;
import com.holela.R;
import com.holela.Utils.Config;
import com.holela.Utils.Pref_Master;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Raj on 8/9/2017.
 */

public class NotificationFollowing extends BaseAdapter {

    Context context;
    ArrayList<String> arrayList_photo = new ArrayList<>();
    ArrayList<NotificationModel> array_notify_you_list = new ArrayList<>();
   ViewHolder holder;
    static int transfer;
    String postid;
    Pref_Master pref_master ;
    LayoutInflater layoutInflater;
    Notification_Photo notification_photo;

    public NotificationFollowing(FragmentActivity activity, ArrayList<NotificationModel> array_notify_you_list) {
        this.context = activity;
        this.array_notify_you_list = array_notify_you_list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return array_notify_you_list.size();
    }

    @Override
    public Object getItem(int position) {
        return array_notify_you_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View itemView, ViewGroup viewGroup) {
        holder = new ViewHolder();
        final NotificationModel model = array_notify_you_list.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.notificationfollowing, null);

            holder.noti_pic = (CircleImageView) itemView.findViewById(R.id.noti_story_pic);
            holder.Noti = (TextView) itemView.findViewById(R.id.noti);
            holder.time = (TextView) itemView.findViewById(R.id.noti_time);
            // holder.post = (ImageView) itemView.findViewById(R.id.noti_post_pic);
            holder.postimagelist = (RecyclerView) itemView.findViewById(R.id.story_photo_array);
            holder.wholelayout = (LinearLayout) itemView.findViewById(R.id.wholelayout);
            pref_master = new Pref_Master(context);

            itemView.setTag(holder);
        } else {
            holder = (ViewHolder) itemView.getTag();
        }

        if (array_notify_you_list.get(position).getType().equals("4")){
            holder.wholelayout.setVisibility(View.VISIBLE);

        } else {
            holder.wholelayout.setVisibility(View.GONE);
        }



        if (array_notify_you_list.get(position).getPosttype().equals("1")){
            holder.postimagelist.setVisibility(View.VISIBLE);
        } else if (array_notify_you_list.get(position).getPosttype().equals("2")){
            transfer=1;
        } else if (array_notify_you_list.get(position).getPosttype().equals("4")){
            holder.postimagelist.setVisibility(View.GONE);

        }  else if (array_notify_you_list.get(position).getPosttype().equals("")){
            holder.postimagelist.setVisibility(View.GONE);
        }


        ArrayList singleSectionItems = array_notify_you_list.get(position).getImageModels();

        NotificationImageRecyclerAdaper notificationImageRecyclerAdaper = new NotificationImageRecyclerAdaper(context, singleSectionItems);
        holder.postimagelist.setHasFixedSize(true);
        holder.postimagelist.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.postimagelist.setAdapter(notificationImageRecyclerAdaper);



        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Tabs.class);
                intent.putExtra("fragmentcode", Config.Fragment_ID.Userother);
                intent.putExtra("id", array_notify_you_list.get(position).getSid());
                pref_master.setOtherUserid(array_notify_you_list.get(position).getSid());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


        Picasso.with(context)
                .load(array_notify_you_list.get(position).getSimage())
                .placeholder(R.drawable.user)
                .into(holder.noti_pic);

        holder.Noti.setText(array_notify_you_list.get(position).getDescription());

        holder.time.setText(array_notify_you_list.get(position).getDate());



        return itemView;
    }


    private class ViewHolder {

        CircleImageView noti_pic;
        TextView Noti;
        TextView time;
        ImageView post;
        RecyclerView postimagelist;
        LinearLayout wholelayout;

    }
}
