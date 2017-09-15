package com.holela.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import android.widget.ListView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.holela.Adapter.Chat_name_Adapter;
import com.holela.Adapter.Story_adapter;
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

public class Chat_Activity extends AppCompatActivity {

    ListView recyclerView;
    Chat_name_Adapter chat_name_adapter;
    ImageView back;
    Context context = this;
    ArrayList<ChatListMOdel> chat_name_list = new ArrayList<>();
    Pref_Master pref_master;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_);

        recyclerView = (ListView) findViewById(R.id.chat_name);
        back = (ImageView) findViewById(R.id.back);
        pref_master = new Pref_Master(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        chat_name_list.clear();



        getchatname();


    }

    private void getchatname() {

        String url = Config.baseurl + Config.getchatmember;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("userid", pref_master.getUID());

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("getchatmember", jarray_loginuser);
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
                    if (res_flag.equals("400")) {
                        JSONArray jarray = new JSONArray(jobj.getString("data"));

                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jobj1 = jarray.getJSONObject(i);

                            ChatListMOdel chatListMOdel = new ChatListMOdel();
                            chatListMOdel.setId(jobj1.getString("userid"));
                            chatListMOdel.setName(jobj1.getString("fullname"));
                            chatListMOdel.setImg(jobj1.getString("imagepath"));
                            chat_name_list.add(chatListMOdel);
                        }

                    } else {
                        DialogBox.setPOP(context, jobj.getString("msg").toString());
                    }


                    chat_name_adapter = new Chat_name_Adapter(getApplicationContext(), chat_name_list);
                    recyclerView.setAdapter(chat_name_adapter);



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
