package com.holela.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.holela.Activity.MainActivity;
import com.holela.Activity.Otp_Activity;
import com.holela.Adapter.Notification_Photo;
import com.holela.Adapter.Notificationyou;
import com.holela.Adapter.Story_adapter;
import com.holela.Controller.DialogBox;
import com.holela.Models.ImageModel;
import com.holela.Models.NotificationImageModel;
import com.holela.Models.NotificationModel;
import com.holela.Models.PostModel;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class Notification_You extends Fragment {

    ListView recyclerView;
    Context context;
    private boolean loading = false;
    Notificationyou notificationyou;
    int rr = 0;
    Pref_Master pref_master;
    ArrayList<NotificationModel> array_notify_you_list = new ArrayList<>();
    ArrayList<String> array_notify_you_photo = new ArrayList<>();

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
// handle back button's click listener

                    Intent ii = new Intent(getActivity(), MainActivity.class);
                    startActivity(ii);
                }
                return false;
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_notification__you, container, false);
        recyclerView = (ListView) view.findViewById(R.id.recycle);
        pref_master = new Pref_Master(getActivity());

        array_notify_you_list.clear();

        notificationyou = new Notificationyou(getActivity(), array_notify_you_list);
        recyclerView.setAdapter(notificationyou);


        getNotification(array_notify_you_list);



        recyclerView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {


            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


                if (rr == 7) {

                    Log.e("Loading", "Loading");
                } else if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {

                    if (!loading) {
                        loading = true;
                        Log.e("Loading", "True");
                        getNotification(array_notify_you_list);
                    }
                }
            }
        });


        return view;
    }

    private void getNotification(ArrayList<NotificationModel> arrayList) {

        String url = Config.baseurl + Config.getnotification;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("userid", pref_master.getUID());
            jobj_row.put("limit", arrayList.size());

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("getnotification", jarray_loginuser);
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
                Log.e("Notification Response",response);
                ///     obj_dialog.dismiss();

                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    if (res_flag.equals("200")) {
                        JSONArray jarray = new JSONArray(jobj.getString("data"));

                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jobj1 = jarray.getJSONObject(i);
                            NotificationModel notificationModel = new NotificationModel();
                            notificationModel.setPostid(jobj1.getString("postid"));
                            notificationModel.setDate(jobj1.getString("date"));
                            notificationModel.setDescription(jobj1.getString("description"));
                            notificationModel.setReadstatus(jobj1.getString("readstatus"));
                            notificationModel.setType(jobj1.getString("type"));
                            notificationModel.setRid(jobj1.getString("rid"));
                            notificationModel.setRname(jobj1.getString("rname"));
                            notificationModel.setRimage(jobj1.getString("rimage"));
                            notificationModel.setSid(jobj1.getString("sid"));
                            notificationModel.setSname(jobj1.getString("sname"));
                            notificationModel.setSimage(jobj1.getString("simage"));
                            notificationModel.setPosttype(jobj1.getString("posttype"));
                            notificationModel.setCaption(jobj1.getString("caption"));
                            notificationModel.setVideo(jobj1.getString("video"));


                            JSONArray jsonArray1 = new JSONArray(jobj1.getString("postfilename"));
                            ArrayList<NotificationImageModel> post_image_list = new ArrayList<>();

                            for (int j = 0; j < jsonArray1.length(); j++) {
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                post_image_list.add(new NotificationImageModel(jsonObject1.getString("image")));
                            }

                            notificationModel.setImageModels(post_image_list);
                            array_notify_you_list.add(notificationModel);


                        }

                    } else if (res_flag.equals("400")) {

                        rr = 7;
                        /*DialogBox.setPOP(context, jobj.getString("msg").toString());*/
                    }

                    notificationyou.notifyDataSetChanged();
                    loading = false;

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
