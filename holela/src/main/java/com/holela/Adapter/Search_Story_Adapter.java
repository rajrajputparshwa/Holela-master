package com.holela.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.holela.Controller.Textview_open_regular;
import com.holela.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 5/17/2017.
 */

public class Search_Story_Adapter extends RecyclerView.Adapter<Search_Story_Adapter.MyView>{

    Context context;
    ArrayList<String> array_search_story=new ArrayList<>();

    public Search_Story_Adapter(FragmentActivity activity, ArrayList<String> array_search_story) {
        this.context = activity;
        this.array_search_story = array_search_story;
    }

    public class MyView extends RecyclerView.ViewHolder {


        CircleImageView story_pic;
        Textview_open_regular story_name;

        public MyView(View itemView) {
            super(itemView);

            story_pic=(CircleImageView)itemView.findViewById(R.id.story_pic_search);
            story_name=(Textview_open_regular)itemView.findViewById(R.id.story_name_search);

        }
    }

    @Override
    public Search_Story_Adapter.MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_story_row, parent, false);

        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(Search_Story_Adapter.MyView holder, int position) {


        if (position==0){
            holder.story_name.setText(R.string.Your_Story);
            holder.story_pic.setImageResource(R.drawable.holela_circle);
            holder.story_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }else {
        }

    }

    @Override
    public int getItemCount() {
        return array_search_story.size();
    }

}
