package com.holela.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.holela.Adapter.CommentAdapter;
import com.holela.Adapter.PostDetailAdapter;
import com.holela.Controller.DialogBox;
import com.holela.Controller.MyRiad_Pro_Regular;
import com.holela.Fragment.Home;
import com.holela.Models.CommentModel;
import com.holela.Models.PostDetailModel;
import com.holela.Models.PostModel;
import com.holela.R;
import com.holela.Utils.Config;
import com.holela.Utils.Connection;
import com.holela.Utils.Pref_Master;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.holela.Fragment.Home.GetPostUrl;

public class PostDetail extends AppCompatActivity {

    public static int transferrr;
    static CommentAdapter commentAdapter;
    CircleImageView postpic;
    MyRiad_Pro_Regular posttext;
    LinearLayout sharebtn;
    ImageView post;
    ImageView heartlike;
    ImageView commentimg;
    ImageView sendimg;
    ImageView shareimg;
    ImageView post_play;
    VideoView postvideo;
    TextView textpost;
    PostDetail postDetail;
    ArrayList<PostDetailModel> postdetail_list = new ArrayList<>();
    ArrayList<CommentModel> comment_list = new ArrayList<>();
    ImageView sharehome;
    RelativeLayout textlayout;
    ArrayList<PostModel> post_list = new ArrayList<>();
    String posttype;
    EditText msg;
    PostDetailAdapter postDetailAdapter;
    LinearLayout medialayout;
    Handler h = new Handler();
    String userid, postid;
    ImageView sendcomment;
    RecyclerView recyclerView, Comments;
    String value;
    String intent;
    Pref_Master pref_master;
    Drawable perk_active, perk_like;
    ImageView home;
    Context context = this;
    TextView likecounts, commentcounts, repostcounts, sharecounts;
    int postion = 0;
    Home.PostAdapter postAdapter;
    private MyRiad_Pro_Regular post_textusername,alpha;
    MyRiad_Pro_Regular post_date;
    int value2;
    Runnable runn = new Runnable() {
        @Override
        public void run() {

            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onBackPressed();
                    h.postDelayed(this, 5000);
                }
            }, 5000);

        }
    };
    private int SPLASH_DISPLAY_LENGTH = 2000;
    public Runnable run = new Runnable() {
        @Override
        public void run() {

            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getCommentss();
                    h.postDelayed(this, SPLASH_DISPLAY_LENGTH);
                }
            }, SPLASH_DISPLAY_LENGTH);

        }
    };


    public static void DeleteComment(final Context context, String userid, String postid, String commentid, final int position, final ArrayList<CommentModel> arrayList) {

        String url = Config.baseurl + Config.deletecomment;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("userid", userid);
            jobj_row.put("postid", postid);
            jobj_row.put("commentid", commentid);

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("deletecomment", jarray_loginuser);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        final Map<String, String> params = new HashMap<>();
        params.put("data", "" + jobj_loginuser.toString().replaceAll("\\\\", ""));
        Log.e("raj_request", ":" + params.toString());
/*
        Map<String, String> header = new HashMap<>();
        header.put("apikey", Config.headkey);
        header.put("username", Config.headunm);
        header.put(Config.Language, pref.getLanguage());
        Map<String, String> header = new HashMap<>();
        header.put("apikey", Config.headkey);
        header.put("username", Config.headunm);
        header.put(Config.Language, pref.getLanguage());*/

        Response.Listener<String> lis_res = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("deletecomment_Response", " : " + response);
                ///     obj_dialog.dismiss();

                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    if (res_flag.equals("200")) {


                        arrayList.remove(arrayList.get(position));
                        commentAdapter.notifyDataSetChanged();


                    } else {
                        DialogBox.setPOP(context, jobj.getString("msg").toString());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        };

        Response.ErrorListener lis_error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("response error", "" + error);
                //    obj_dialog.dismiss();
            }
        };

        Connection.postconnection(url, params, Config.getHeaderParam(context), context, lis_res, lis_error);

    }

    @Override
    public void onBackPressed() {
        h.removeMessages(0);
        finish();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        postpic = (CircleImageView) findViewById(R.id.post_picc);
        posttext = (MyRiad_Pro_Regular) findViewById(R.id.post_text);
        sharebtn = (LinearLayout) findViewById(R.id.share_btn);
        heartlike = (ImageView) findViewById(R.id.heartlike);
        commentimg = (ImageView) findViewById(R.id.commentimg);
        postvideo = (VideoView) findViewById(R.id.video_post);
        sendcomment = (ImageView) findViewById(R.id.sendcomment);
        sendimg = (ImageView) findViewById(R.id.sendimg);
        shareimg = (ImageView) findViewById(R.id.shareimg);
        post_play = (ImageView) findViewById(R.id.play_post);
        sharehome = (ImageView) findViewById(R.id.share_home);
        home = (ImageView) findViewById(R.id.home);
        Comments = (RecyclerView) findViewById(R.id.commentsss);
        recyclerView = (RecyclerView) findViewById(R.id.post);
        textpost = (TextView) findViewById(R.id.text_post);
        textlayout = (RelativeLayout) findViewById(R.id.textlayout);
        medialayout = (LinearLayout) findViewById(R.id.media_layout);
        likecounts = (TextView) findViewById(R.id.likecounts);
        commentcounts = (TextView) findViewById(R.id.commentscounts);
        sharecounts = (TextView) findViewById(R.id.sharecounts);
        perk_active = context.getResources().getDrawable(R.drawable.fill_heart_3x);
        perk_like = context.getResources().getDrawable(R.drawable.like);
        repostcounts = (TextView) findViewById(R.id.repostcounts);
        post_date=(MyRiad_Pro_Regular)findViewById(R.id.post_date);
        post_textusername = (MyRiad_Pro_Regular)findViewById(R.id.post_textusername);
        alpha=(MyRiad_Pro_Regular)findViewById(R.id.alpha);
        msg = (EditText) findViewById(R.id.msg);

        pref_master = new Pref_Master(context);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);


        LinearLayoutManager postmanger = new LinearLayoutManager(context);
        postmanger.setOrientation(LinearLayoutManager.VERTICAL);
        Comments.setLayoutManager(postmanger);

        final int[] size = {comment_list.size()};
        String string = String.valueOf(size[0]);
        boolean arrraaay = Boolean.valueOf(string);
        Comments.setNestedScrollingEnabled(arrraaay);


        commentimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, CommentScreen.class);
                i.putExtra("postid", postid);
                context.startActivity(i);
            }
        });

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            value = extras.getString("postid");
            intent = extras.getString("intent");

        }


        getPost(context);
        getCommentss();


        run.run();


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        sendcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (msg.getText().toString().trim().length() == 0) {

                } else {

                    SendComment();
                    msg.setText("");
                }
            }
        });

        shareimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetPostUrl(context, postid);
            }
        });

        sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = userid;
                if (post_list.size() == 0) {

                }
                if (id.equals(pref_master.getUID())) {

                    DialogBox.SetPOstDetailDelete(context, postid);

                }
                if (!id.equals(pref_master.getUID())) {

                    DialogBox.SetShare(context, postid, postion);
                }
            }
        });

        postpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Tabs.class);
                intent.putExtra("fragmentcode", Config.Fragment_ID.Userother);
                intent.putExtra("id", userid);
                pref_master.setOtherUserid(userid);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);



            }
        });
    }

    private void SendComment() {

        String url = Config.baseurl + Config.commentpost;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();
            jobj_row.put("userid", pref_master.getUID());

            jobj_row.put("postid", value);

            jobj_row.put("comment", msg.getText().toString());


            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("commentpost", jarray_loginuser);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        final Map<String, String> params = new HashMap<>();
        params.put("data", "" + jobj_loginuser.toString().replaceAll("\\\\", ""));
        Log.e("raj_request", ":" + params.toString());
/*
        Map<String, String> header = new HashMap<>();
        header.put("apikey", Config.headkey);
        header.put("username", Config.headunm);
        header.put(Config.Language, pref.getLanguage());
        Map<String, String> header = new HashMap<>();
        header.put("apikey", Config.headkey);
        header.put("username", Config.headunm);
        header.put(Config.Language, pref.getLanguage());*/

        Response.Listener<String> lis_res = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", " : " + response);
                ///     obj_dialog.dismiss();

                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    if (res_flag.equals("200")) {

                        getPost(context);

                    } else {
                        DialogBox.setPOP(getApplicationContext(), jobj.getString("msg").toString());
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        };

        Response.ErrorListener lis_error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("response error", "" + error);
                //    obj_dialog.dismiss();
            }
        };

        Connection.postconnection(url, params, Config.getHeaderParam(getApplicationContext()), getApplicationContext(), lis_res, lis_error);

    }

    private void getCommentss() {

        String url = Config.baseurl + Config.getpostcomments;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();
            jobj_row.put("postid", value);

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("getpostcomments", jarray_loginuser);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        final Map<String, String> params = new HashMap<>();
        params.put("data", "" + jobj_loginuser.toString().replaceAll("\\\\", ""));
        Log.e("raj_request", ":" + params.toString());
/*
        Map<String, String> header = new HashMap<>();
        header.put("apikey", Config.headkey);
        header.put("username", Config.headunm);
        header.put(Config.Language, pref.getLanguage());
        Map<String, String> header = new HashMap<>();
        header.put("apikey", Config.headkey);
        header.put("username", Config.headunm);
        header.put(Config.Language, pref.getLanguage());*/

        Response.Listener<String> lis_res = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", " : " + response);
                ///     obj_dialog.dismiss();

                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    if (res_flag.equals("200")) {
                        JSONArray jarray = new JSONArray(jobj.getString("data"));
                        comment_list.clear();
                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jobj1 = jarray.getJSONObject(i);
                            CommentModel commentModel = new CommentModel();
                            commentModel.setUid(jobj1.getString("userid"));
                            commentModel.setComments(jobj1.getString("comment"));
                            commentModel.setDatetime(jobj1.getString("datetime"));
                            commentModel.setUsername(jobj1.getString("username"));
                            commentModel.setCommentid(jobj1.getString("commentid"));
                            comment_list.add(commentModel);
                        }

                    } else {
                        DialogBox.setPOP(getApplicationContext(), jobj.getString("msg").toString());
                    }

                    commentAdapter = new CommentAdapter(context, comment_list);
                    Comments.setAdapter(commentAdapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        };

        Response.ErrorListener lis_error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("response error", "" + error);
                //    obj_dialog.dismiss();
            }
        };

        Connection.postconnection(url, params, Config.getHeaderParam(getApplicationContext()), getApplicationContext(), lis_res, lis_error);

    }

    public void getPost(final Context context) {

        String url = Config.baseurl + Config.getpostdetails + "/" + value;
        final String json = "";


        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);

        Map<String, String> header = new HashMap<>();
        header.put("apikey", Config.headkey);
        header.put("username", Config.headunm);

//header.put(Config.Language, "en");
//header.put(Config.Language, pref.getLanguage());


        Response.Listener<String> lis_res = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);


                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    if (res_flag.equals("200")) {
                        JSONArray jarray = new JSONArray(jobj.getString("data"));

                        postdetail_list.clear();
                        for (int i = 0; i < jarray.length(); i++) {

                            JSONObject jsonObject = jarray.getJSONObject(i);

                            String caption = jsonObject.getString("caption");
                            String fullname = jsonObject.getString("fullname");
                            userid = jsonObject.getString("userid");

                            String username = jsonObject.getString("username");
                            String posttype = jsonObject.getString("posttype");
                            final String like = jsonObject.getString("like");
                            String comment = jsonObject.getString("comment");
                            String share = jsonObject.getString("share");
                            String repost = jsonObject.getString("repost");
                            final String mylike = jsonObject.getString("mylike");

                            final String ppostid = jsonObject.getString("postid");

                            postid = jsonObject.getString("postid");
                            textpost.setText(caption);
                            posttext.setText(fullname);
                            alpha.setText("@");
                            post_textusername.setText(username);
                            likecounts.setText(like);
                            commentcounts.setText(comment);
                            repostcounts.setText(repost);
                            sharecounts.setText(share);
                            post_date.setText(pref_master.getStr_user_posttime());


                            String propic = jsonObject.getString("profileiamge");

                            if (propic.equals("")) {
                                Picasso.with(context)
                                        .load(R.drawable.ic_defalult)
                                        .placeholder(R.drawable.ic_defalult)
                                        .into(postpic);
                            } else {
                                Picasso.with(context)
                                        .load(propic)
                                        .placeholder(R.drawable.ic_defalult)
                                        .into(postpic);

                            }


                            if (posttype.equals("4")) {
                                medialayout.setVisibility(View.GONE);
                            }
                            if (posttype.equals("1")) {
                                post_play.setVisibility(View.GONE);
                                postvideo.setVisibility(View.GONE);
                                medialayout.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                            if (posttype.equals("2")) {
                                post_play.setVisibility(View.GONE);
                                transferrr = 1;
                                postvideo.setVisibility(View.GONE);
                                medialayout.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }

                            if (mylike.equals("1")) {
                                heartlike.setImageDrawable(perk_active);
                            } else if (mylike.equals("0")) {
                                heartlike.setImageDrawable(perk_like);
                            }


                            sendimg.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DialogBox.setRepost(context, postid, userid, getSupportFragmentManager());
                                }
                            });


                            heartlike.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.e("IFFFFFFF", "FED");

                                    if (mylike.equals("0")) {

                                        if (value2 == 1) {

                                            value2 = 0;


                                        } else {

                                            ImageView imageView = (ImageView) view.findViewById(R.id.heartlike);
                                            int likcountsss = Integer.parseInt(like);
                                            int finalLikcountsss = likcountsss + 1;
                                            Home.Like(context, ppostid, getSupportFragmentManager());
                                            value2 = 1;
                                            likecounts.setText(String.valueOf(finalLikcountsss));
                                            imageView.setImageDrawable(perk_active);

                                        }


                                    } else if (mylike.equals("1")) {

                                        ImageView imageView = (ImageView) view.findViewById(R.id.heartlike);
                                        int likcountsss = Integer.parseInt(like);
                                        int finalLikcountsss = likcountsss - 1;
                                        imageView.setImageDrawable(perk_like);
                                        likecounts.setText(String.valueOf(finalLikcountsss));
                                        Home.Like(context, ppostid, getSupportFragmentManager());


                                    }


                                }

                            });


                            JSONArray jsonArray1 = new JSONArray(jsonObject.getString("filename"));


                            for (int j = 0; j < jsonArray1.length(); j++) {
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                PostDetailModel postDetailModel = new PostDetailModel();
                                postDetailModel.setImage(jsonObject1.getString("image"));
                                postDetailModel.setThumb(jsonObject1.getString("thumb"));
                                postdetail_list.add(postDetailModel);
                            }
                        }
                    } else {
                        DialogBox.setPOP(getApplicationContext(), jobj.getString("msg").toString());
                    }

                    postDetailAdapter = new PostDetailAdapter(context, postdetail_list);
                    recyclerView.setAdapter(postDetailAdapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        Response.ErrorListener lis_error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
// signup.setClickable(true);
                    /*obj_dialog.dismiss();*/
            }
        };
        Connection.getconnectionVolley(url, param, Config.getHeaderParam(context), context, lis_res, lis_error);

    }

    public void Delete_Post(final Context context, String postid) {
        Pref_Master pref_master = new Pref_Master(context);
        String url = Config.baseurl + Config.deleteuserpost;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();
            jobj_row.put("userid", pref_master.getUID());
            jobj_row.put("postid", postid);

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("deleteuserpost", jarray_loginuser);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        final Map<String, String> params = new HashMap<>();
        params.put("data", "" + jobj_loginuser.toString().replaceAll("\\\\", ""));
        Log.e("raj_request", ":" + params.toString());
/*
        Map<String, String> header = new HashMap<>();
        header.put("apikey", Config.headkey);
        header.put("username", Config.headunm);
        header.put(Config.Language, pref.getLanguage());
        Map<String, String> header = new HashMap<>();
        header.put("apikey", Config.headkey);
        header.put("username", Config.headunm);
        header.put(Config.Language, pref.getLanguage());*/

        Response.Listener<String> lis_res = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("DeleteResponse", " : " + response);
                ///     obj_dialog.dismiss();
                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    if (res_flag.equals("200")) {


                    } else {
                        // DialogBox.setPOP(context(), jobj.getString("msg").toString());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        };

        Response.ErrorListener lis_error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("response error", "" + error);
                //    obj_dialog.dismiss();
            }
        };

        Connection.postconnection(url, params, Config.getHeaderParam(context), context, lis_res, lis_error);

    }


}
