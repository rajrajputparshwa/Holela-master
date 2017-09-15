package com.holela.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.holela.Adapter.ChatListAdapter;
import com.holela.Adapter.Chat_name_Adapter;
import com.holela.Controller.DialogBox;
import com.holela.Models.ChatListMOdel;
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
import java.util.logging.Handler;

public class ChatListActivty extends AppCompatActivity {


    ListView recyclerView;
    ChatListAdapter chatListAdapter;
    ImageView back;
    ImageView chat_icon;
    Context context = this;
    ArrayList<ChatListMOdel> chat_name_list = new ArrayList<>();
    Pref_Master pref_master;
    android.os.Handler h = new android.os.Handler();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        h.removeMessages(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        h.removeMessages(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list_activty);


        recyclerView = (ListView) findViewById(R.id.chat_name);
        back = (ImageView) findViewById(R.id.back);
        chat_icon = (ImageView) findViewById(R.id.chat_icon);
        pref_master = new Pref_Master(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        chat_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Chat_Activity.class);
                startActivity(i);
            }
        });

        chat_name_list.clear();


        getchatname();
        run.run();

    }

    public Runnable run = new Runnable() {
        @Override
        public void run() {

            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getchatname();
                    h.postDelayed(this, 2000);
                }
            }, 2000);

        }
    };



    private void getchatname() {

        String url = Config.baseurl + Config.getchatlist;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("userid", pref_master.getUID());

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("getchatlist", jarray_loginuser);
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
                Log.e("login_Response", " : " + response);
                ///     obj_dialog.dismiss();

                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    if (res_flag.equals("200")) {
                        JSONArray jarray = new JSONArray(jobj.getString("data"));
                        chat_name_list.clear();
                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jobj1 = jarray.getJSONObject(i);

                            ChatListMOdel chatListMOdel = new ChatListMOdel();
                            chatListMOdel.setId(jobj1.getString("userid"));
                            chatListMOdel.setName(jobj1.getString("fullname"));
                            chatListMOdel.setImg(jobj1.getString("image"));
                            chatListMOdel.setMsg(jobj1.getString("msg"));
                            chatListMOdel.setChatdate(jobj1.getString("chatdatetime"));
                            chat_name_list.add(chatListMOdel);
                        }

                    } else {
                        DialogBox.setPOP(context, jobj.getString("msg").toString());
                    }


                    chatListAdapter = new ChatListAdapter(getApplicationContext(), chat_name_list);
                    recyclerView.setAdapter(chatListAdapter);



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
