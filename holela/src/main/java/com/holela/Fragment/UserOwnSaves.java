package com.holela.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.holela.Adapter.UserOwnLikeAdapter;
import com.holela.Adapter.UserSaveAdapter;
import com.holela.Controller.DialogBox;
import com.holela.Models.ImageModel;
import com.holela.Models.PostModel;
import com.holela.Models.UserOWnLikeImage;
import com.holela.Models.UserOwnLikeModel;
import com.holela.Models.UserSaveImage;
import com.holela.Models.UserSaveModel;
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
public class UserOwnSaves extends Fragment {

    static ListView post;
    static Pref_Master pref_master;
    static UserSaveAdapter userSaveAdapter;
    static ProgressDialog progressDialog;
    private static boolean loading = false;
    static int rr = 0;
    static ArrayList<UserSaveModel> save_list = new ArrayList<>();
    Context context;

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
        View view = inflater.inflate(R.layout.fragment_user_own_saves, container, false);


        post = (ListView) view.findViewById(R.id.recycle);
        pref_master = new Pref_Master(context);


        save_list.clear();

        userSaveAdapter = new UserSaveAdapter(context, save_list, getFragmentManager());
        post.setAdapter(userSaveAdapter);

        getPost(getActivity(), getFragmentManager(), save_list);

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
                        getPost(getActivity(), getFragmentManager(), save_list);

                    }
                }
            }
        });



        return view;
    }


    public static void getPost(final Context context, final FragmentManager manager, final ArrayList<UserSaveModel> arrayList) {

        String url = Config.baseurl + Config.getusersave + "/" + pref_master.getUID() + "/" + arrayList.size();
        String json = "";
   /*     progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
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
                Log.e("UsersaveResponse", response);
/*
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }*/

                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    if (res_flag.equals("200")) {
                        JSONArray jarray = new JSONArray(jobj.getString("data"));

                        for (int i = 0; i < jarray.length(); i++) {

                            JSONObject jsonObject = jarray.getJSONObject(i);
                            UserSaveModel userSaveModel = new UserSaveModel();
                            userSaveModel.setPostid(jsonObject.getString("postid"));
                            userSaveModel.setUserid(jsonObject.getString("userid"));
                            userSaveModel.setPosttype(jsonObject.getString("posttype"));
                            userSaveModel.setProfileiamge(jsonObject.getString("profileiamge"));
                            userSaveModel.setPostfilename(jsonObject.getString("filename"));
                            userSaveModel.setCaption(jsonObject.getString("caption"));
                            userSaveModel.setUsername(jsonObject.getString("fullname"));
                            userSaveModel.setFullname(jsonObject.getString("username"));
                            userSaveModel.setEmail(jsonObject.getString("email"));
                            userSaveModel.setPostdatetime(jsonObject.getString("postdatetime"));
                            userSaveModel.setLike(jsonObject.getString("like"));
                            userSaveModel.setComment(jsonObject.getString("comment"));
                            userSaveModel.setShare(jsonObject.getString("repost"));
                            userSaveModel.setRepost(jsonObject.getString("share"));
                            userSaveModel.setMylike(jsonObject.getString("mylike"));

                            JSONArray jsonArray1 = new JSONArray(jsonObject.getString("filename"));
                            ArrayList<UserSaveImage> save_image_list = new ArrayList<>();

                            for (int j = 0; j < jsonArray1.length(); j++) {
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                save_image_list.add(new UserSaveImage(jsonObject1.getString("image"), jsonObject1.getString("thumb")));
                            }
                            userSaveModel.setImageModels(save_image_list);
                            save_list.add(userSaveModel);
                        }

                    } else  if (res_flag.equals("400")){
                        /*DialogBox.setPOP(context, jobj.getString("msg").toString());*/
                        rr=7;
                    }

                    userSaveAdapter.notifyDataSetChanged();
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
