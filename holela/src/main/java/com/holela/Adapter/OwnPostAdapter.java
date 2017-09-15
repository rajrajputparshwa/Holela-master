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

import com.holela.Activity.CommentScreen;
import com.holela.Activity.SaveHome;
import com.holela.Activity.Tabs;
import com.holela.Controller.DialogBox;
import com.holela.Controller.MyRiad_Pro_Regular;
import com.holela.Fragment.Home;
import com.holela.Models.PostModel;
import com.holela.Models.UserOwnPostModel;
import com.holela.R;
import com.holela.Utils.Config;
import com.holela.Utils.Pref_Master;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 6/8/2017.
 */

public class OwnPostAdapter extends BaseAdapter {

    Context context;
    Pref_Master pref_master;
    static int transferdfdf = 0;
    ArrayList<UserOwnPostModel> own_post_list = new ArrayList<>();
    FragmentManager manager;
    LayoutInflater layoutInflater;
    ViewHolder holder;
    Drawable perk_active, perk_like;
    int value;

    public OwnPostAdapter(Context context, ArrayList<UserOwnPostModel> own_own_post_list, FragmentManager manager) {
        this.context = context;
        this.own_post_list = own_own_post_list;
        this.manager = manager;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        perk_active = context.getResources().getDrawable(R.drawable.fill_heart_3x);
        perk_like = context.getResources().getDrawable(R.drawable.like);
    }

    @Override
    public int getCount() {
        return own_post_list.size();
    }

    @Override
    public Object getItem(int i) {
        return own_post_list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View itemView, ViewGroup viewGroup) {
        holder = new ViewHolder();
        final UserOwnPostModel userOwnPostModel = own_post_list.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);


        if (itemView == null) {

            itemView = LayoutInflater.from(context).inflate(R.layout.ownpostcust, null);
            holder.postpic = (CircleImageView) itemView.findViewById(R.id.post_pic);
            holder.posttext = (MyRiad_Pro_Regular) itemView.findViewById(R.id.post_text);
            holder.sharebtn = (LinearLayout) itemView.findViewById(R.id.share_btn);
            holder.heartlike = (ImageView) itemView.findViewById(R.id.heartlike);
            holder.commentimg = (ImageView) itemView.findViewById(R.id.commentimg);
            holder.postvideo = (VideoView) itemView.findViewById(R.id.video_post);
            holder.alpha = (MyRiad_Pro_Regular) itemView.findViewById(R.id.alpha);
            holder.posttextusername = (MyRiad_Pro_Regular) itemView.findViewById(R.id.post_textusername);

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


        final String userid = own_post_list.get(position).getUserid();
        final String postid = own_post_list.get(position).getPostid();
        final String mylike = own_post_list.get(position).getMylike();
        final String like = own_post_list.get(position).getLike();
        holder.postdate.setText(own_post_list.get(position).getPostdatetime());
        holder.likecount.setText(own_post_list.get(position).getLike());
        holder.commentcount.setText(own_post_list.get(position).getComment());
        holder.sharecount.setText(own_post_list.get(position).getShare());
        //   holder.repostcount.setText(own_post_list.get(position).getRepost());
        int likcountsss = Integer.parseInt(own_post_list.get(position).getLike());
        likcountsss++;

        holder.save_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveHome.Save(context, postid, manager);
                Toast.makeText(context, "Post Saved", Toast.LENGTH_LONG).show();
            }
        });

        holder.posttextusername.setText(own_post_list.get(position).getFullname());
        holder.alpha.setText("@");

        if (own_post_list.get(position).getMylike().equals("1")) {
            holder.heartlike.setImageDrawable(perk_active);
        } else if (own_post_list.get(position).getMylike().equals("0")) {
            holder.heartlike.setImageDrawable(perk_like);
        }


        holder.heartlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("IFFFFFFF", "FED");

                if (own_post_list.get(position).getMylike().equals("0")) {

                    if (value == 1) {

                        value = 0;


                    } else {

                        ImageView imageView = (ImageView) view.findViewById(R.id.heartlike);
                        int likcountsss = Integer.parseInt(own_post_list.get(position).getLike());
                        int finalLikcountsss = likcountsss + 1;
                        Home.Like(context, postid, manager);
                        userOwnPostModel.setLike(Integer.toString(finalLikcountsss));
                        userOwnPostModel.setMylike("1");

                        notifyDataSetChanged();
                        value = 1;
                        holder.likecount.setText(own_post_list.get(position).getLike());
                        imageView.setImageDrawable(perk_active);
                    }


                } else if (own_post_list.get(position).getMylike().equals("1")) {

                    ImageView imageView = (ImageView) view.findViewById(R.id.heartlike);
                    int likcountsss = Integer.parseInt(own_post_list.get(position).getLike());
                    int finalLikcountsss = likcountsss - 1;
                    userOwnPostModel.setLike(Integer.toString(finalLikcountsss));
                    userOwnPostModel.setMylike("0");

                    notifyDataSetChanged();
                    imageView.setImageDrawable(perk_like);
                    Home.Like(context, postid, manager);
                }

/*

                    ImageView imageView = (ImageView) view.findViewById(R.id.heartlike);
                    imageView.setVisibility(View.GONE);
                    holder.heartunlike.setVisibility(View.VISIBLE);
                    Home.Like(context, postid, manager, own_post_list);
*/

            }

        });


          /*  holder.heartunlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
              *//*      Home.Like(context, postid, manager, own_post_list);
                    holder.heartunlike.setVisibility(View.GONE);
                    holder.heartlike.setVisibility(View.VISIBLE);*//*
                }
            });*/

        pref_master = new Pref_Master(context);

        ArrayList singleSectionItems = own_post_list.get(position).getImageModels();

        OwnPostImageAdapter userOwnPostImages = new OwnPostImageAdapter(context, singleSectionItems, postid);
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(userOwnPostImages);
        holder.recyclerView.setNestedScrollingEnabled(false);

        if (own_post_list.get(position).getPosttype().equals("4")) {
            holder.medialayout.setVisibility(View.GONE);
        } else if (own_post_list.get(position).getPosttype().equals("1")) {
//                holder.post_play.setVisibility(View.GONE);
            //     holder.postvideo.setVisibility(View.GONE);
            holder.recyclerView.setVisibility(View.VISIBLE);
            holder.medialayout.setVisibility(View.VISIBLE);

        } else if (own_post_list.get(position).getPosttype().equals("2")) {
//                holder.post_play.setVisibility(View.GONE);
            transferdfdf = 1;
//                holder.postvideo.setVisibility(View.GONE);
            holder.recyclerView.setVisibility(View.VISIBLE);
            holder.medialayout.setVisibility(View.VISIBLE);

        }

          /*   else if (own_post_list.get(position).getPosttype().equals("1")){
                holder.post_play.setVisibility(View.GONE);
                holder.postvideo.setVisibility(View.GONE);
            }
*/
//            holder.postvideo.requestFocus();
        holder.commentimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, CommentScreen.class);
                i.putExtra("postid", postid);
                context.startActivity(i);

            }
        });

        holder.textlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, CommentScreen.class);
                i.putExtra("postid", postid);
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
                DialogBox.SetDelete(context,postid,position);
            }
        });


        holder.postusername = own_post_list.get(position).getUserid();

        holder.posttext.setText(own_post_list.get(position).getUsername());

        holder.postusername = own_post_list.get(position).getUserid();

        holder.postuserid = own_post_list.get(position).getUserid();


        holder.posttext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                if (own_post_list.size() == 0) {

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
        String pic = own_post_list.get(position).getProfileiamge();

        if (pic.equals("")) {
            Picasso.with(context)
                    .load(R.drawable.user)
                    .placeholder(R.drawable.user)
                    .into(holder.postpic);
        } else {
            Picasso.with(context)
                    .load(own_post_list.get(position).getProfileiamge())
                    .placeholder(R.drawable.user)
                    .into(holder.postpic);
        }

        holder.postpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });




          /*  CustomViewPager adapter = new CustomViewPager(getActivity(), own_post_list);
            holder.viewPager.setAdapter(adapter);*/

        if (own_post_list.get(position).getCaption().equals("")) {
            holder.textpost.setVisibility(View.GONE);
        } else {
            holder.textpost.setVisibility(View.VISIBLE);
            holder.textpost.setText(own_post_list.get(position).getCaption());
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
                Home.GetPostUrl(context, postid);


            }
        });

        holder.textlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, CommentScreen.class);
                i.putExtra("postid", postid);
                context.startActivity(i);

            }
        });


        return itemView;
    }


    private class ViewHolder {
        CircleImageView postpic;
        MyRiad_Pro_Regular posttext, alpha, posttextusername;
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
