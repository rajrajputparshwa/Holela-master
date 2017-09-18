package com.holela.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.holela.Activity.MainActivity;
import com.holela.Adapter.SearchAdapter;
import com.holela.Adapter.Search_Image_Adapter;
import com.holela.Adapter.UserMediaAdapter;
import com.holela.Controller.DialogBox;
import com.holela.Models.ImageModel;
import com.holela.Models.PostModel;
import com.holela.Models.UserMediaImageModel;
import com.holela.Models.UserMediaModel;
import com.holela.R;
import com.holela.Utils.Config;
import com.holela.Utils.Connection;
import com.holela.Utils.Pref_Master;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserOwnMedia extends Fragment {

    GridView list_images;
    Search_Image_Adapter search_image_adapter;
    Pref_Master pref_master;
    UserMediaAdapter userMediaAdapter;
    private static boolean loading = false;
    static int s = 0;
    SwipeRefreshLayout container_items;
    static int rr = 0;
    ProgressDialog progressDialog;
    ArrayList<UserMediaModel> array_image_story = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_own_photos, container, false);

        list_images = (GridView) v.findViewById(R.id.list_grid);
        container_items = (SwipeRefreshLayout) v.findViewById(R.id.container_items);

        pref_master = new Pref_Master(getActivity());


        array_image_story.clear();

        userMediaAdapter = new UserMediaAdapter(getActivity(), array_image_story, getFragmentManager());
        list_images.setAdapter(userMediaAdapter);

        getPost(container_items, array_image_story);


        container_items.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (s == 0) {
                    s = 1;
                    array_image_story.clear();
                    getPost(container_items, array_image_story);
                }
            }
        });


        list_images.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                        getPost(container_items, array_image_story);
                    }
                }
            }
        });


        return v;
    }


    public void getPost(final SwipeRefreshLayout swipeRefreshLayout, ArrayList<UserMediaModel> arrayList) {


        PostModel model = new PostModel();

        String url = Config.baseurl + Config.getpostimages + "/" + pref_master.getUID() + "/" + arrayList.size();
        String json = "";
        if (swipeRefreshLayout.isRefreshing()) {
        } else {
         /*   progressDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
            progressDialog.setMessage("Please wait ...");
            progressDialog.show();*/
        }


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
                Log.e("UsermediaResponse", response);

            /*    if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }*/

                if (swipeRefreshLayout.isRefreshing()) {
                    s = 0;
                    swipeRefreshLayout.setRefreshing(false);
                }

                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    if (res_flag.equals("200")) {
                        JSONArray jarray = new JSONArray(jobj.getString("data"));


                        for (int i = 0; i < jarray.length(); i++) {

                            JSONObject jsonObject = jarray.getJSONObject(i);
                            UserMediaModel userMediaModel = new UserMediaModel();
                            userMediaModel.setPostid(jsonObject.getString("postid"));
                            userMediaModel.setUserid(jsonObject.getString("userid"));
                            userMediaModel.setPosttype(jsonObject.getString("posttype"));

                            JSONArray jsonArray1 = new JSONArray(jsonObject.getString("filename"));
                            ArrayList<UserMediaImageModel> post_image_list = new ArrayList<>();

                            for (int j = 0; j < jsonArray1.length(); j++) {
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                post_image_list.add(new UserMediaImageModel(jsonObject1.getString("image") , jsonObject1.getString("thumb")));
                            }

                            userMediaModel.setImageModels(post_image_list);
                            array_image_story.add(userMediaModel);

                        }
                    } else {
                        /*DialogBox.setPOP(getActivity(), jobj.getString("msg").toString());*/

                        rr=7;

                    }

                    userMediaAdapter.notifyDataSetChanged();

                    loading = false;

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
        Connection.getconnectionVolley(url, param, Config.getHeaderParam(getActivity()), getActivity(), lis_res, lis_error);

    }

}
