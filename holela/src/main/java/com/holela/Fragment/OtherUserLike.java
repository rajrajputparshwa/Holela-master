package com.holela.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.holela.Adapter.UserOwnLikeAdapter;
import com.holela.Models.PostModel;
import com.holela.Models.UserOWnLikeImage;
import com.holela.Models.UserOwnLikeModel;
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
public class OtherUserLike extends Fragment {
    static ListView post;
    Context context;
    Pref_Master pref_master;
    private static boolean loading = false;
    static int rr = 0;
    static UserOwnLikeAdapter userOwnLikeAdapter;
    static ProgressDialog progressDialog;

    static ArrayList<UserOwnLikeModel> own_post_list = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_other_user_like, container, false);


        post = (ListView) v.findViewById(R.id.recycle);
        pref_master = new Pref_Master(getActivity());

        own_post_list.clear();

        getOwnPostLike(getActivity(), getFragmentManager(), own_post_list);

        userOwnLikeAdapter = new UserOwnLikeAdapter(getActivity(), own_post_list, getFragmentManager());
        post.setAdapter(userOwnLikeAdapter);


        post.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {


            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (rr == 7) {
                    rr = 0;
                    Log.e("Loading", "Loading");
                } else if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {

                    if (!loading) {
                        loading = true;
                        getOwnPostLike(getActivity(), getFragmentManager(), own_post_list);

                    }
                }
            }
        });


        return v;
    }

    public void getOwnPostLike(final Context context, final FragmentManager manager, ArrayList<UserOwnLikeModel> arrayList) {


        PostModel model = new PostModel();

        String url = Config.baseurl + Config.getmylike + "/" + pref_master.getOtheruserid() + "/" + arrayList.size();
        String json = "";
        progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();


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
                Log.e("UserlikeResponse", response);

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    if (res_flag.equals("200")) {
                        JSONArray jarray = new JSONArray(jobj.getString("data"));


                        for (int i = 0; i < jarray.length(); i++) {

                            JSONObject jsonObject = jarray.getJSONObject(i);
                            UserOwnLikeModel userOwnLikeModel = new UserOwnLikeModel();
                            userOwnLikeModel.setPostid(jsonObject.getString("postid"));
                            userOwnLikeModel.setUserid(jsonObject.getString("userid"));
                            userOwnLikeModel.setPosttype(jsonObject.getString("posttype"));
                            userOwnLikeModel.setProfileiamge(jsonObject.getString("profileiamge"));
                            userOwnLikeModel.setPostfilename(jsonObject.getString("filename"));
                            userOwnLikeModel.setCaption(jsonObject.getString("caption"));
                            userOwnLikeModel.setUsername(jsonObject.getString("fullname"));
                            userOwnLikeModel.setFullname(jsonObject.getString("username"));
                            userOwnLikeModel.setEmail(jsonObject.getString("email"));
                            userOwnLikeModel.setPostdatetime(jsonObject.getString("postdatetime"));
                            userOwnLikeModel.setLike(jsonObject.getString("like"));
                            userOwnLikeModel.setComment(jsonObject.getString("comment"));
                            userOwnLikeModel.setShare(jsonObject.getString("share"));
                            userOwnLikeModel.setRepost(jsonObject.getString("share"));
                            /*userOwnLikeModel.setMylike(jsonObject.getString("mylike"));*/

                            JSONArray jsonArray1 = new JSONArray(jsonObject.getString("filename"));
                            ArrayList<UserOWnLikeImage> post_image_list = new ArrayList<>();

                            for (int j = 0; j < jsonArray1.length(); j++) {
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                post_image_list.add(new UserOWnLikeImage(jsonObject1.getString("image"), jsonObject1.getString("thumb")));
                            }
                            userOwnLikeModel.setImageModels(post_image_list);
                            own_post_list.add(userOwnLikeModel);

                        }
                    } else {
                        /*DialogBox.setPOP(context, jobj.getString("msg").toString());*/
                        rr = 7;
                    }

                    userOwnLikeAdapter.notifyDataSetChanged();


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
        Connection.getconnectionVolley(url, param, Config.getHeaderParam(context), context, lis_res, lis_error);

    }


}
