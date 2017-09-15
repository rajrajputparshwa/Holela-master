package com.holela.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.holela.Activity.MainActivity;
import com.holela.Activity.Tabs;
import com.holela.Adapter.FollowingAdapter;
import com.holela.Controller.DialogBox;
import com.holela.Models.FolowingListModel;
import com.holela.R;
import com.holela.Utils.Config;
import com.holela.Utils.Connection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class LikeScreen extends Fragment {

    static RecyclerView recyclerView;
    String userid, lo, postid;
    static ArrayList<FolowingListModel> following_list = new ArrayList<>();
    static FollowingAdapter followingAdapter;
    ImageView back;

    @Override
    public void onResume() {

        super.onResume();

        if (getView() == null) {
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_BACK) {

                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.remove(LikeScreen.this);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    ft.commit();
// handle back button's click listener
                }

                return false;
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_like_screen, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.likess);
        back=(ImageView)v.findViewById(R.id.back);

        LinearLayoutManager postmanger = new LinearLayoutManager(getActivity());
        postmanger.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(postmanger);

        Bundle bundle = getArguments();
        userid = bundle.getString("fo");
        postid = bundle.getString("postid");
        lo = bundle.getString("lo");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(LikeScreen.this);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                ft.commit();

            }
        });

        following_list.clear();

        Likes(getActivity(), getFragmentManager(), userid, postid);

        return v;
    }


    public static void Likes(final FragmentActivity fragmentActivity, final FragmentManager fragmentManager, String userid, String postid) {

        String url = Config.baseurl + Config.postlikeuserlist;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("userid", userid);
            jobj_row.put("postid", postid);

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("postlikeuserlist", jarray_loginuser);
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

                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jobj1 = jarray.getJSONObject(i);
                            FolowingListModel folowingListModel = new FolowingListModel();
                            folowingListModel.setUserid(jobj1.getString("userid"));
                            folowingListModel.setUsernamename(jobj1.getString("username"));
                            folowingListModel.setImage(jobj1.getString("profilepic"));
                            folowingListModel.setMyfollow(jobj1.getString("followstatus"));
                            following_list.add(folowingListModel);
                        }

                    } else {
                        DialogBox.setPOP(fragmentActivity, jobj.getString("msg").toString());
                    }

                    followingAdapter = new FollowingAdapter(fragmentActivity, following_list, fragmentManager);
                    recyclerView.setAdapter(followingAdapter);

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

        Connection.postconnection(url, params, Config.getHeaderParam(fragmentActivity), fragmentActivity, lis_res, lis_error);

    }

}
