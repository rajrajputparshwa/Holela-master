package com.holela.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.holela.Activity.Tabs;
import com.holela.Controller.DialogBox;
import com.holela.Models.CommentModel;
import com.holela.R;
import com.holela.Utils.Config;
import com.holela.Utils.Pref_Master;

import java.util.ArrayList;

/**
 * Created by admin on 6/6/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyView> {

    Context context;
    ArrayList<CommentModel> comment_list = new ArrayList<>();
    Pref_Master pref_master;

    public CommentAdapter(Context context, ArrayList<CommentModel> comment_list) {
        this.context = context;
        this.comment_list = comment_list;
    }

    public class MyView extends RecyclerView.ViewHolder {
        TextView textView, username;

        public MyView(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.commenttext);
            username = (TextView) itemView.findViewById(R.id.usernamettext);
            pref_master = new Pref_Master(context);
        }
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.commentpage, parent, false);

        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(MyView holder, final int position) {

        CommentModel commentModel = comment_list.get(position);
        holder.textView.setText(comment_list.get(position).getComments());
        holder.username.setText(comment_list.get(position).getUsername());
        final String userid = pref_master.getUID();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, Tabs.class);
                intent.putExtra("fragmentcode", Config.Fragment_ID.Userother);
                intent.putExtra("id", comment_list.get(position).getUid());
                pref_master.setOtherUserid(comment_list.get(position).getUid());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

                /*DialogBox.setDeleteComment(context, context.getString(R.string.deletecomment), userid, comment_list.get(position).getComments(), comment_list.get(position).getCommentid(),position, comment_list);

      */      }
        });

    }

    @Override
    public int getItemCount() {
        return comment_list.size();
    }

}
