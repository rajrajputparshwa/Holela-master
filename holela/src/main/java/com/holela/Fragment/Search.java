package com.holela.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.holela.Activity.StoryActivity;
import com.holela.Adapter.Search_Image_Adapter;
import com.holela.Adapter.Search_Story_Adapter;
import com.holela.Adapter.Story_adapter;
import com.holela.Adapter.UserMediaAdapter;
import com.holela.Models.StoryModel;
import com.holela.Models.UserMediaImageModel;
import com.holela.Models.UserMediaModel;
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
 * Created by Admin on 5/13/2017.
 */

public class Search extends Fragment {

    RecyclerView story_search;
    static int s = 0;
    GridView list_images;
    Context context;
    Search_Story_Adapter search_story_adapter;
    Search_Image_Adapter search_image_adapter;
    ArrayList<UserMediaModel> array_image_story = new ArrayList<>();
    ArrayList<String> array_search_story = new ArrayList<>();
    private static boolean loadingimage = false;
    UserMediaAdapter userMediaAdapter;
    private static boolean loading = false;
    SwipeRefreshLayout container_items;
    TextView text;
    int rr = 0;
    ProgressDialog progressDialog;
    ArrayList<StoryModel> storyModelArrayList = new ArrayList<>();
    Story_adapter story_adapter;
    Pref_Master pref_master;
    ImageView eye;
    TextView eyetext;
    public static int story = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        story_search = (RecyclerView) v.findViewById(R.id.story_list_search);

        list_images = (GridView) v.findViewById(R.id.list_grid);
        container_items = (SwipeRefreshLayout) v.findViewById(R.id.container_items);
        text = (TextView) v.findViewById(R.id.texttt);
        eye = (ImageView) v.findViewById(R.id.eyeeee);
        eyetext = (TextView) v.findViewById(R.id.eyetext);

        userMediaAdapter = new UserMediaAdapter(getActivity(), array_image_story, getFragmentManager());
        list_images.setAdapter(userMediaAdapter);

        container_items.setRefreshing(true);
        pref_master = new Pref_Master(getActivity());

        getAllmedia(container_items, array_image_story);


        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStoryPost();

            }
        });


        eyetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, StoryActivity.class);
                context.startActivity(i);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        story_search.setLayoutManager(layoutManager);

        if (Connection.isconnection(getActivity())) {
            storyModelArrayList.clear();
            getStorydata();


        } else {
            Toast.makeText(getActivity(), "Internet not available !", Toast.LENGTH_LONG).show();
        }

        container_items.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (s == 0) {
                    s = 1;
                    rr = 0;
                    array_image_story.clear();
                    getAllmedia(container_items, array_image_story);
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
                        Log.e("Loadsdfding", "Loadsdsdfing");
                        getAllmedia(container_items, array_image_story);
                    }
                }
            }
        });


        return v;
    }


    public void getAllmedia(final SwipeRefreshLayout swipeRefreshLayout, ArrayList<UserMediaModel> arrayList) {


        String url = Config.baseurl + Config.getallmedia + "/" + arrayList.size();
        String json = "";

       /* progressDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();
*/

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

           /*     if (progressDialog.isShowing()) {
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
                                post_image_list.add(new UserMediaImageModel(jsonObject1.getString("image"), jsonObject1.getString("thumb")));
                            }

                            userMediaModel.setImageModels(post_image_list);
                            array_image_story.add(userMediaModel);

                        }
                    } else if (res_flag.equals("400")) {
                        /*DialogBox.setPOP(getActivity(), jobj.getString("msg").toString());*/

                        rr = 7;

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


    private void getStorydata() {

        String url = Config.baseurl + Config.storyfollowinglist;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("userid", pref_master.getUID());

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("storyfollowinglist", jarray_loginuser);
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
                Log.e("StoryResponse", " : " + response);
                ///     obj_dialog.dismiss();

                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    if (res_flag.equals("200")) {
                        JSONArray jarray = new JSONArray(jobj.getString("data"));

                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jobj1 = jarray.getJSONObject(i);
                            StoryModel storyModel = new StoryModel();
                            storyModel.setFullname(jobj1.getString("fullname"));
                            storyModel.setUsername(jobj1.getString("username"));
                            storyModel.setUserid(jobj1.getString("userid"));
                            storyModel.setImagepath(jobj1.getString("imagepath"));
                            storyModelArrayList.add(storyModel);

                        }

                    } else {
                        //DialogBox.setPOP(context, jobj.getString("msg").toString());
                    }

                    story_adapter = new Story_adapter(getActivity(), storyModelArrayList);
                    story_search.setAdapter(story_adapter);

                    story_adapter.notifyDataSetChanged();

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

        Connection.postconnection(url, params, Config.getHeaderParam(getActivity()), getActivity(), lis_res, lis_error);

    }


    public void getStoryPost() {


        String url = Config.baseurl + Config.getuserstory + "/" + pref_master.getUID();
        String json = "";


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

                        for (int i = 0; i < jarray.length(); i++) {

                            Intent ii = new Intent(context, StoryActivity.class);
                            context.startActivity(ii);
                        }

                    } else if (res_flag.equals("400")) {
                        /*DialogBox.setPOP(context, jobj.getString("msg").toString());*/
                        story = 1;
                        Intent i = new Intent(context, StoryActivity.class);
                        context.startActivity(i);
                    }

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
