package com.holela.Activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.holela.Adapter.CommentAdapter;
import com.holela.Controller.DialogBox;
import com.holela.Models.CommentModel;
import com.holela.R;
import com.holela.Utils.Config;
import com.holela.Utils.Connection;
import com.holela.Utils.Pref_Master;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentScreen extends AppCompatActivity {

    RecyclerView comments;
    EditText msg;
    ImageView send;
    CommentAdapter commentAdapter;
    String intent;
    Pref_Master pref_master ;
    Handler h = new Handler();
    Context context = this;
    String value;
    ArrayList<CommentModel> comment_list = new ArrayList<>();
    ImageView back;


    @Override
    public void onBackPressed() {
        h.removeMessages(0);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_screen);

        comments = (RecyclerView) findViewById(R.id.commentsss);
        msg = (EditText) findViewById(R.id.msg);
        pref_master = new Pref_Master(getApplicationContext());
        send = (ImageView) findViewById(R.id.sendcomment);
        back=(ImageView)findViewById(R.id.back);

        LinearLayoutManager postmanger = new LinearLayoutManager(getApplicationContext());
        postmanger.setOrientation(LinearLayoutManager.VERTICAL);
        comments.setLayoutManager(postmanger);

        final int[] size = {comment_list.size()};
        String string = String.valueOf(size[0]);
        boolean arrraaay = Boolean.valueOf(string);
        comments.setNestedScrollingEnabled(arrraaay);


        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            value = extras.getString("postid");
            intent = extras.getString("intent");

        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (msg.getText().toString().trim().length() == 0) {

                } else {

                    SendComment();
                    msg.setText("");
                }
            }
        });



        getCommentss();
        run.run();


    }

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
                    comments.setAdapter(commentAdapter);

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

}
