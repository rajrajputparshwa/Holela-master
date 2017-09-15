package com.holela.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.holela.Adapter.Chat_room_adapter;
import com.holela.Controller.DialogBox;
import com.holela.Models.GetChatModel;
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

public class Chat_Room extends AppCompatActivity {

    ListView chat;
    Chat_room_adapter chat_room_adapter;
    ArrayList<GetChatModel> chat_array = new ArrayList<>();
    Context context = this;
    ImageView back;
    String senderid;
    TextView usertextname;
    android.os.Handler h = new android.os.Handler();
    EditText chatmsg;
    ImageView send;
    Pref_Master pref_master;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       finish();
        h.removeMessages(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        h.removeMessages(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        run.run();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat__room);

        chat = (ListView) findViewById(R.id.chat);
        pref_master = new Pref_Master(context);
        usertextname = (TextView) findViewById(R.id.usertextname);
        back = (ImageView) findViewById(R.id.back);
        chatmsg = (EditText) findViewById(R.id.chatmsg);
        send = (ImageView) findViewById(R.id.sendcomment);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (chatmsg.getText().toString().trim().length()==0){

                } else {
                    postchat(chatmsg.getText().toString());
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        senderid = bundle.getString("senderid");

        chat_array.clear();

        chat_room_adapter = new Chat_room_adapter(getApplicationContext(), chat_array);
        chat.setAdapter(chat_room_adapter);


        getchat();
        run.run();
    }

    public Runnable run = new Runnable() {
        @Override
        public void run() {

            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getchat();
                    h.postDelayed(this, 2000);
                }
            }, 2000);

        }
    };


    private void getchat() {

        String url = Config.baseurl + Config.getchat;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("userid", pref_master.getUID());
            jobj_row.put("senderid", senderid);

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("getchat", jarray_loginuser);
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
                Log.e("getchat_Response", " : " + response);
                ///     obj_dialog.dismiss();

                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    if (res_flag.equals("200")) {
                        JSONArray jarray = new JSONArray(jobj.getString("data"));
                        chat_array.clear();
                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jobj1 = jarray.getJSONObject(i);

                            GetChatModel getChatModel = new GetChatModel();
                            getChatModel.setSenderid(jobj1.getString("senderid"));
                            getChatModel.setMsg(jobj1.getString("msg"));
                            getChatModel.setReveiverid(jobj1.getString("reveiverid"));
                            getChatModel.setChatdatetime(jobj1.getString("chatdatetime"));
                            chat_array.add(getChatModel);
                        }

                    } else {
                        /*DialogBox.setPOP(context, jobj.getString("msg").toString());*/
                    }


                    chat_room_adapter.notifyDataSetChanged();



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

    private void postchat(String msg) {

        String url = Config.baseurl + Config.postchat;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("userid", pref_master.getUID());
            jobj_row.put("msg", msg);
            jobj_row.put("reveiverid", senderid);
            jobj_row.put("chatmediatype", "");
            jobj_row.put("chatmedianame", "");

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("postchat", jarray_loginuser);
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
                Log.e("getchat_Response", " : " + response);
                ///     obj_dialog.dismiss();

                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    if (res_flag.equals("200")) {

                        chatmsg.setText("");
                        getchat();
                        chat.smoothScrollToPosition(Chat_room_adapter.lastValue);
                        Log.e("Pos", String.valueOf(Chat_room_adapter.lastValue - 1));

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


}
