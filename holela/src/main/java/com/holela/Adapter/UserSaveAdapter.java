package com.holela.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.holela.Activity.PostDetail;
import com.holela.Activity.SaveHome;
import com.holela.Activity.Tabs;
import com.holela.Controller.DialogBox;
import com.holela.Controller.MyRiad_Pro_Regular;
import com.holela.Fragment.Home;
import com.holela.Models.PostModel;
import com.holela.Models.UserSaveModel;
import com.holela.R;
import com.holela.Utils.Config;
import com.holela.Utils.Pref_Master;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Raj on 6/29/2017.
 */

public class UserSaveAdapter extends BaseAdapter {

    Context context;
    Pref_Master pref_master;
    static int transferdfdf = 0;
    ArrayList<UserSaveModel> save_list = new ArrayList<>();
    FragmentManager manager;
    static int transfer = 0;
    LayoutInflater layoutInflater;
    ViewHolder holder;
    Drawable perk_active, perk_like;
    int value;

    public UserSaveAdapter(Context context, ArrayList<UserSaveModel> save_list, FragmentManager manager) {
        this.context = context;
        this.save_list = save_list;
        this.manager = manager;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        perk_active = context.getResources().getDrawable(R.drawable.fill_heart_3x);
        perk_like = context.getResources().getDrawable(R.drawable.like);


    }

    @Override
    public int getCount() {
        return save_list.size();
    }

    @Override
    public Object getItem(int i) {
        return save_list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View itemView, ViewGroup viewGroup) {
        holder = new ViewHolder();
        final UserSaveModel userSaveModel = save_list.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);


        if (itemView == null) {

            itemView = LayoutInflater.from(context).inflate(R.layout.savepostcust, null);
            holder.postpic = (CircleImageView) itemView.findViewById(R.id.post_pic);
            holder.posttext = (MyRiad_Pro_Regular) itemView.findViewById(R.id.post_text);
            holder.sharebtn = (LinearLayout) itemView.findViewById(R.id.share_btn);
            holder.heartlike = (ImageView) itemView.findViewById(R.id.heartlike);
            holder.commentimg = (ImageView) itemView.findViewById(R.id.commentimg);
            holder.postvideo = (VideoView) itemView.findViewById(R.id.video_post);
            holder.sendimg = (ImageView) itemView.findViewById(R.id.sendimg);
            holder.shareimg = (ImageView) itemView.findViewById(R.id.shareimg);
            holder.post_play = (ImageView) itemView.findViewById(R.id.play_post);
            holder.save_home = (ImageView) itemView.findViewById(R.id.save_home);
            holder.recyclerView = (RecyclerView) itemView.findViewById(R.id.post);
            holder.textpost = (TextView) itemView.findViewById(R.id.text_post);
            holder.textlayout = (RelativeLayout) itemView.findViewById(R.id.textlayout);
            holder.medialayout = (RelativeLayout) itemView.findViewById(R.id.media_layout);
            holder.likecount = (TextView) itemView.findViewById(R.id.likecounts);
            holder.commentcount = (TextView) itemView.findViewById(R.id.commentscounts);
            holder.sharecount = (TextView) itemView.findViewById(R.id.sharecounts);
            holder.postdate = (MyRiad_Pro_Regular) itemView.findViewById(R.id.post_date);
            // repostcount = (TextView) itemView.findViewById(R.id.repostcounts);
            itemView.setTag(holder);


        } else {
            holder = (ViewHolder) itemView.getTag();
        }

        final String userid = save_list.get(position).getUserid();
        final String postid = save_list.get(position).getPostid();
        final String mylike = save_list.get(position).getMylike();
        final String like = save_list.get(position).getLike();
        holder.postdate.setText(save_list.get(position).getPostdatetime());
        holder.likecount.setText(save_list.get(position).getLike());
        holder.commentcount.setText(save_list.get(position).getComment());
        holder.sharecount.setText(save_list.get(position).getShare());
        //   holder.repostcount.setText(post_list.get(position).getRepost());
        int likcountsss = Integer.parseInt(save_list.get(position).getLike());
        likcountsss++;

        holder.save_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveHome.Save(context, postid, manager);
                Toast.makeText(context, "Post Saved", Toast.LENGTH_LONG).show();
            }
        });

        if (save_list.get(position).getMylike().equals("1")) {
            holder.heartlike.setImageDrawable(perk_active);
        } else if (save_list.get(position).getMylike().equals("0")) {
            holder.heartlike.setImageDrawable(perk_like);
        }


        holder.heartlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("IFFFFFFF", "FED");

                if (save_list.get(position).getMylike().equals("0")) {

                    if (value == 1) {

                        value = 0;


                    } else {

                        ImageView imageView = (ImageView) view.findViewById(R.id.heartlike);
                        int likcountsss = Integer.parseInt(save_list.get(position).getLike());
                        int finalLikcountsss = likcountsss + 1;
                        Home.Like(context, postid, manager);
                        userSaveModel.setLike(Integer.toString(finalLikcountsss));
                        userSaveModel.setMylike("1");
                        save_list.add(userSaveModel);
                        notifyDataSetChanged();
                        value = 1;
                        holder.likecount.setText(save_list.get(position).getLike());
                        imageView.setImageDrawable(perk_active);
                    }


                } else if (save_list.get(position).getMylike().equals("1")) {

                    ImageView imageView = (ImageView) view.findViewById(R.id.heartlike);
                    int likcountsss = Integer.parseInt(save_list.get(position).getLike());
                    int finalLikcountsss = likcountsss - 1;
                    userSaveModel.setLike(Integer.toString(finalLikcountsss));
                    userSaveModel.setMylike("0");
                    save_list.add(userSaveModel);
                    notifyDataSetChanged();
                    imageView.setImageDrawable(perk_like);
                    Home.Like(context, postid, manager);
                }

/*

                    ImageView imageView = (ImageView) view.findViewById(R.id.heartlike);
                    imageView.setVisibility(View.GONE);
                    holder.heartunlike.setVisibility(View.VISIBLE);
                    Home.Like(context, postid, manager, post_list);
*/

            }

        });


          /*  holder.heartunlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
              *//*      Home.Like(context, postid, manager, post_list);
                    holder.heartunlike.setVisibility(View.GONE);
                    holder.heartlike.setVisibility(View.VISIBLE);*//*
                }
            });*/


        ArrayList singleSectionItems = save_list.get(position).getImageModels();

        UserSaveImageAdapter userSaveImageAdapter = new UserSaveImageAdapter(context, singleSectionItems, postid);
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(userSaveImageAdapter);
        holder.recyclerView.setNestedScrollingEnabled(false);

        if (save_list.get(position).getPosttype().equals("4")) {
            holder.medialayout.setVisibility(View.GONE);
        } else if (save_list.get(position).getPosttype().equals("1")) {
//                holder.post_play.setVisibility(View.GONE);
            //     holder.postvideo.setVisibility(View.GONE);
            holder.recyclerView.setVisibility(View.VISIBLE);
            holder.medialayout.setVisibility(View.VISIBLE);

        } else if (save_list.get(position).getPosttype().equals("2")) {
//                holder.post_play.setVisibility(View.GONE);
            transfer = 1;
//                holder.postvideo.setVisibility(View.GONE);
            holder.recyclerView.setVisibility(View.VISIBLE);
            holder.medialayout.setVisibility(View.VISIBLE);

        }

          /*   else if (post_list.get(position).getPosttype().equals("1")){
                holder.post_play.setVisibility(View.GONE);
                holder.postvideo.setVisibility(View.GONE);
            }
*/
//            holder.postvideo.requestFocus();
        holder.commentimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, PostDetail.class);
                i.putExtra("postid", postid);
                i.putExtra("intent", "profile");
                context.startActivity(i);
            }
        });

        holder.textlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, PostDetail.class);
                i.putExtra("postid", postid);
                i.putExtra("intent", "profile");
                context.startActivity(i);

            }
        });

           /* holder.postvideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                // Close the progress bar and play the video
                public void onPrepared(MediaPlayer mp) {
                    holder.postvideo.seekTo(holder.postion);
                    holder.postvideo.start();
                }
            });

*/
          /*  holder.post_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });*/


        holder.sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogBox.SetShare(context, postid, position);
            }
        });


        holder.postusername = save_list.get(position).getUserid();

        holder.posttext.setText(save_list.get(position).getUsername());

        holder.postusername = save_list.get(position).getUserid();

        holder.postuserid = save_list.get(position).getUserid();


        holder.posttext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (save_list.size() == 0) {

                } else if (holder.postuserid.equals(pref_master.getUID())) {
                    Intent intent = new Intent(context, Tabs.class);
                    intent.putExtra("fragmentcode", Config.Fragment_ID.UserOwn);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                } else if (!holder.postuserid.equals(pref_master.getUID())) {
                    Intent intent = new Intent(context, Tabs.class);
                    intent.putExtra("fragmentcode", Config.Fragment_ID.Userother);
                    intent.putExtra("id", holder.postusername);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }

            }
        });
        String pic = save_list.get(position).getProfileiamge();

        if (pic.equals("")) {
            Picasso.with(context)
                    .load(R.drawable.user)
                    .placeholder(R.drawable.user)
                    .into(holder.postpic);
        } else {
            Picasso.with(context)
                    .load(save_list.get(position).getProfileiamge())
                    .placeholder(R.drawable.user)
                    .into(holder.postpic);
        }

        holder.postpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });




          /*  CustomViewPager adapter = new CustomViewPager(getActivity(), post_list);
            holder.viewPager.setAdapter(adapter);*/

        if (save_list.get(position).getCaption().equals("")) {
            holder.textpost.setVisibility(View.GONE);
        } else {
            holder.textpost.setVisibility(View.VISIBLE);
            holder.textpost.setText(save_list.get(position).getCaption());
        }

        holder.sendimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogBox.setRepost(context, postid, userid, manager);
            }
        });


        holder.shareimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  GetPostUrl(context, postid, manager);


            }
        });


        return itemView;
    }

    private class ViewHolder {
        CircleImageView postpic;
        MyRiad_Pro_Regular posttext;
        LinearLayout sharebtn;
        ImageView post;
        ImageView heartlike, heartunlike;
        ImageView commentimg;
        ImageView sendimg;
        ArrayList<PostModel> own_post_list = new ArrayList<>();
        MyRiad_Pro_Regular postdate;
        ImageView shareimg;
        ImageView post_play;
        VideoView postvideo;
        TextView textpost;
        String postusername;
        ImageView save_home;
        RelativeLayout textlayout;
        RelativeLayout medialayout;
        String like = "";
        String postuserid;
        RecyclerView recyclerView;
        TextView likecount, commentcount, sharecount;
        int postion = 0;
    }


}
