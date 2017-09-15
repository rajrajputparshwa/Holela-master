package com.holela.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.holela.Activity.BlockList;
import com.holela.Models.Blocklist;
import com.holela.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 8/24/2017.
 */

public class BlocklistAdapter extends BaseAdapter {

    Context context;
    ArrayList<Blocklist> blocklistArrayList = new ArrayList<>();
    ViewHolder holder;

    public BlocklistAdapter(Context context, ArrayList<Blocklist> blocklistArrayList) {
        this.context = context;
        this.blocklistArrayList = blocklistArrayList;
    }


    @Override
    public int getCount() {
        return blocklistArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return blocklistArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View itemView, ViewGroup viewGroup) {

        holder = new ViewHolder();
        final Blocklist blocklist = blocklistArrayList.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);

        if (itemView == null) {

            itemView = LayoutInflater.from(context).inflate(R.layout.blocklistview, null);

            holder.blockusername = (TextView) itemView.findViewById(R.id.blockusername);
            holder.blockuserpic = (CircleImageView) itemView.findViewById(R.id.blockuserpic);
            holder.unblock = (Button) itemView.findViewById(R.id.unblock);

            itemView.setTag(holder);

        } else {
            holder = (ViewHolder) itemView.getTag();
        }


        if (blocklistArrayList.get(position).getImagepath().equals("")) {
            Glide.with(context)
                    .load(R.drawable.user)
                    .placeholder(R.drawable.user)
                    .into(holder.blockuserpic);
        } else {
            Glide.with(context)
                    .load(blocklistArrayList.get(position).getImagepath())
                    .placeholder(R.drawable.user)
                    .into(holder.blockuserpic);
        }


        holder.blockusername.setText(blocklistArrayList.get(position).getFullname());

        holder.unblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BlockList.Unblock(context, blocklistArrayList.get(position).getUserid(),position);
            }
        });


        return itemView;
    }


    private class ViewHolder {

        CircleImageView blockuserpic;
        TextView blockusername;
        Button unblock;


    }


}
