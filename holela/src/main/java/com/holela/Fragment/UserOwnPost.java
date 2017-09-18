package com.holela.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.holela.Adapter.OwnPostAdapter;
import com.holela.Models.PostModel;
import com.holela.Models.UserOwnPostImages;
import com.holela.Models.UserOwnPostModel;
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
public class UserOwnPost extends Fragment {

    static ListView post;
    Context context;
    private static boolean loading = false;
    static Pref_Master pref_master;
    static OwnPostAdapter ownPostAdapter;
    static ProgressDialog progressDialog;
    static int rr = 0;

    static ArrayList<UserOwnPostModel> own_post_list = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_own_post, container, false);

        post = (ListView) v.findViewById(R.id.recycle);
        pref_master = new Pref_Master(getActivity());

        own_post_list.clear();

        ownPostAdapter = new OwnPostAdapter(getActivity(), own_post_list, getFragmentManager());
        post.setAdapter(ownPostAdapter);


        getOwnPost(getActivity(), getFragmentManager(), own_post_list);




        post.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                        getOwnPost(getActivity(), getFragmentManager(), own_post_list);

                    }
                }
            }
        });


        return v;
    }


    public static void getOwnPost(final Context context, final FragmentManager manager, ArrayList<UserOwnPostModel> arrayList) {


        PostModel model = new PostModel();

        String url = Config.baseurl + Config.getuserwall + "/" + pref_master.getUID() + "/" + arrayList.size();
        String json = "";
/*
        progressDialog = new ProgressDialog(context , R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();*/

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
                Log.e("GetuserwallResponse", response);

            /*    if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }*/

                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    if (res_flag.equals("200")) {
                        JSONArray jarray = new JSONArray(jobj.getString("data"));


                        for (int i = 0; i < jarray.length(); i++) {

                            JSONObject jsonObject = jarray.getJSONObject(i);
                            UserOwnPostModel userOwnPostModel = new UserOwnPostModel();
                            userOwnPostModel.setPostid(jsonObject.getString("postid"));
                            userOwnPostModel.setUserid(jsonObject.getString("userid"));
                            userOwnPostModel.setPosttype(jsonObject.getString("posttype"));
                            userOwnPostModel.setProfileiamge(jsonObject.getString("profileiamge"));
                            //   userOwnPostModel.setPostfilename(jsonObject.getString("filename"));
                            userOwnPostModel.setCaption(jsonObject.getString("caption"));
                            userOwnPostModel.setUsername(jsonObject.getString("fullname"));
                            userOwnPostModel.setFullname(jsonObject.getString("username"));
                            userOwnPostModel.setEmail(jsonObject.getString("email"));
                            userOwnPostModel.setPostdatetime(jsonObject.getString("postdatetime"));
                            userOwnPostModel.setLike(jsonObject.getString("like"));
                            userOwnPostModel.setComment(jsonObject.getString("comment"));
                            userOwnPostModel.setShare(jsonObject.getString("share"));
                            userOwnPostModel.setRepost(jsonObject.getString("share"));
                            userOwnPostModel.setMylike(jsonObject.getString("mylike"));

                            JSONArray jsonArray1 = new JSONArray(jsonObject.getString("filename"));
                            ArrayList<UserOwnPostImages> post_image_list = new ArrayList<>();

                            for (int j = 0; j < jsonArray1.length(); j++) {
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                post_image_list.add(new UserOwnPostImages(jsonObject1.getString("image"), jsonObject1.getString("thumb")));
                            }
                            userOwnPostModel.setImageModels(post_image_list);
                            own_post_list.add(userOwnPostModel);

                        }
                    } else {
                        /*DialogBox.setPOP(context, jobj.getString("msg").toString());*/
                        rr = 7;
                    }

                    ownPostAdapter.notifyDataSetChanged();
                    loading=false;

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


}
