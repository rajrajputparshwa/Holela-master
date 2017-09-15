package com.holela.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.holela.Controller.Textview_open_regular;
import com.holela.Models.ChatListMOdel;
import com.holela.Models.GetChatModel;
import com.holela.R;

import java.util.ArrayList;

import com.holela.Utils.Pref_Master;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 5/24/2017.
 */

public class Chat_room_adapter extends BaseAdapter {
    Context context;
    ArrayList<GetChatModel> chat_array = new ArrayList<>();
    ViewHolder holder;
    Pref_Master pref_master;
    public static int lastValue;

    public Chat_room_adapter(Context context, ArrayList<GetChatModel> chat_array) {
        this.context = context;
        this.chat_array = chat_array;
    }

    @Override
    public int getCount() {
        return chat_array.size();
    }

    @Override
    public Object getItem(int i) {
        return chat_array.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View itemView, ViewGroup viewGroup) {

        holder = new ViewHolder();
        GetChatModel getChatModel = chat_array.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);

        lastValue = chat_array.size() - 1;


        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.chat_msg, null);

            holder.user_side_pic = (CircleImageView) itemView.findViewById(R.id.user_side_pic);
            holder.usersidetext = (Textview_open_regular) itemView.findViewById(R.id.usersidetext);
            holder.my_side = (CircleImageView) itemView.findViewById(R.id.my_side);
            holder.mysidetext = (Textview_open_regular) itemView.findViewById(R.id.mysidetext);
            holder.mylayout = (RelativeLayout) itemView.findViewById(R.id.mylayout);
            holder.userlayout = (LinearLayout) itemView.findViewById(R.id.userlayout);


            pref_master = new Pref_Master(context);

            itemView.setTag(holder);

        } else {
            holder = (ViewHolder) itemView.getTag();
        }


        String senderid = chat_array.get(position).getSenderid();
        String recieverid = chat_array.get(position).getReveiverid();


        if (senderid.equals(pref_master.getUID())) {

            holder.mylayout.setVisibility(View.VISIBLE);
            holder.userlayout.setVisibility(View.GONE);
            holder.my_side.setVisibility(View.GONE);
            holder.user_side_pic.setVisibility(View.GONE);
            holder.mysidetext.setText(chat_array.get(position).getMsg());

        } else {

            holder.userlayout.setVisibility(View.VISIBLE);
            holder.mylayout.setVisibility(View.GONE);
            holder.user_side_pic.setVisibility(View.GONE);
            holder.my_side.setVisibility(View.GONE);
            holder.usersidetext.setText(chat_array.get(position).getMsg());

        }



        return itemView;
    }

    private class ViewHolder {
        CircleImageView user_side_pic;
        Textview_open_regular usersidetext;
        CircleImageView my_side;
        RelativeLayout mylayout;
        LinearLayout userlayout;
        Textview_open_regular mysidetext;

    }
}
