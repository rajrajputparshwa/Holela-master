package com.holela.Activity;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.holela.Adapter.UserOwnLikeAdapter;
import com.holela.Controller.DialogBox;
import com.holela.Models.PostModel;
import com.holela.Models.UserOWnLikeImage;
import com.holela.Models.UserOwnLikeModel;
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

public class SaveHome extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_home);


    }


    public static void Save(final Context context, String postid, final FragmentManager manager) {
        Pref_Master pref_master = new Pref_Master(context);
        String url = Config.baseurl + Config.savepost;
        JSONObject jobj_loginuser = new JSONObject();

        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();
            jobj_row.put("userid", pref_master.getUID());
            jobj_row.put("postid", postid);

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("savepost", jarray_loginuser);
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
                Log.e("LikeResponse", " : " + response);
                ///     obj_dialog.dismiss();


                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    if (res_flag.equals("200")) {
                        JSONArray jarray = new JSONArray(jobj.getString("data"));

                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jobj1 = jarray.getJSONObject(i);

                        }

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
