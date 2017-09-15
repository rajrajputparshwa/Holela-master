package com.holela.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import com.holela.Activity.Chat_Room;
import com.holela.Controller.Textview_open_regular;
import com.holela.Models.ChatListMOdel;
import com.holela.Models.UserOwnLikeModel;
import com.holela.R;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 5/24/2017.
 */

public class Chat_name_Adapter extends BaseAdapter {

    Context context;
    ArrayList<ChatListMOdel> chat_name_list = new ArrayList<>();
    ViewHolder holder;

    public Chat_name_Adapter(Context context, ArrayList<ChatListMOdel> chat_name_list) {
        this.context = context;
        this.chat_name_list = chat_name_list;
    }

    @Override
    public int getCount() {
        return chat_name_list.size();
    }

    @Override
    public Object getItem(int i)    {
        return chat_name_list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View itemView, ViewGroup viewGroup) {

        holder = new ViewHolder();
        ChatListMOdel chatListMOdel = chat_name_list.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);


        if (itemView == null) {

            itemView = LayoutInflater.from(context).inflate(R.layout.chat_name, null);


            holder.chat_person_name = (CircleImageView) itemView.findViewById(R.id.user_pic);
            holder.Username = (Textview_open_regular) itemView.findViewById(R.id.username);
          //  holder.new_msg = (Textview_open_regular) itemView.findViewById(R.id.newmsg);


            itemView.setTag(holder);
        } else {
            holder = (ViewHolder) itemView.getTag();
        }


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Chat_Room.class);
                i.putExtra("senderid",chat_name_list.get(position).getId());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

        holder.Username.setText(chat_name_list.get(position).getName());

        if (chat_name_list.get(position).getImg().equals("")) {


            Picasso.with(context)
                    .load(R.drawable.user)
                    .placeholder(R.color.White)
                    .into(holder.chat_person_name);

        } else {


            Picasso.with(context)
                    .load(chat_name_list.get(position).getImg())
                    .placeholder(R.color.White)
                    .into(holder.chat_person_name);

        }

        return itemView;
    }


    private class ViewHolder {
        CircleImageView chat_person_name;
        Textview_open_regular Username, new_msg;
    }
}
