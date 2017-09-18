package com.holela.Fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.holela.Activity.CommentScreen;
import com.holela.Activity.SaveHome;
import com.holela.Activity.StoryActivity;
import com.holela.Activity.Tab_Profile;
import com.holela.Activity.Tabs;
import com.holela.Adapter.Story_adapter;
import com.holela.Adapter.Suggestion_adapter;
import com.holela.Adapter.Userimage_Adapter;
import com.holela.Controller.DialogBox;
import com.holela.Controller.MyRiad_Pro_Regular;
import com.holela.Controller.RecyclerItemDoubleClickListener;
import com.holela.Models.ImageModel;
import com.holela.Models.PostModel;
import com.holela.Models.RepostImagemodel;
import com.holela.Models.RepostModel;
import com.holela.Models.StoryModel;
import com.holela.Models.SuggestionModel;
import com.holela.R;
import com.holela.Utils.Config;
import com.holela.Utils.Connection;
import com.holela.Utils.Pref_Master;
import com.holela.Utils.Util;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.rockerhieu.emojicon.EmojiconTextView;

/**
 * Created by Admin on 5/13/2017.
 */

public class Home extends Fragment {

    Context context;
    static RecyclerView story_list;
    static ListView post;
    Story_adapter adapter;
    Handler h = new Handler();
    static String postype, caption;
    public static int transfer;
    static int rr = 0;
    RecyclerView suggestion;
    LinearLayout header;
    static ProgressDialog progressDialog;
    static PostAdapter postAdapter;
    static private boolean mLoadingItems;
    Story_adapter story_adapter;
    static Pref_Master pref_master;
    ArrayList<SuggestionModel> suggestionModels = new ArrayList<>();
    private TransferUtility transferUtility;
    int size;
    public static int story = 0;

    ImageView eye;
    TextView eyetext;
    static int s = 0;
    ArrayList<StoryModel> storyModelArrayList = new ArrayList<>();
    int mScrollState;
    static ArrayList<RepostImagemodel> post_image_list = new ArrayList<>();
    static ArrayList<PostModel> post_list = new ArrayList<>();
    static String string;
    boolean doubleBackToExitPressedOnce = false;
    private static boolean loading = false;
    static SwipeRefreshLayout container_items;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (this.isVisible()) {
            if (!isVisibleToUser)   // If we are becoming invisible, then...
            {
                //pause or stop video


            }

            if (isVisibleToUser) // If we are becoming visible, then...
            {
                //play your videostory_list

            }
        }

    }

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
                    post.smoothScrollToPosition(0);

                    if (doubleBackToExitPressedOnce) {
// super.onBackPressed();

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().finish();

                    }
                    doubleBackToExitPressedOnce = true;
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce = false;
                        }
                    }, 2000);


                }
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        context = getActivity();

        story_list = (RecyclerView) v.findViewById(R.id.story_list);
        post = (ListView) v.findViewById(R.id.recycle);
        post_list = new ArrayList<>();
        pref_master = new Pref_Master(getActivity());
        header = (LinearLayout) v.findViewById(R.id.header);
        eye = (ImageView) v.findViewById(R.id.eyeeee);
        eyetext = (TextView) v.findViewById(R.id.eyetext);
        container_items = (SwipeRefreshLayout) v.findViewById(R.id.container_items);
        suggestion = (RecyclerView) v.findViewById(R.id.suggestion);
        transferUtility = Util.getTransferUtility(getActivity());

     /*   final ListItemsVisibilityCalculator mVideoVisibilityCalculator = new SingleListViewItemActiveCalculator(new DefaultSingleItemCalculatorCallback(), (List<? extends ListItem>) post_list);


        final VideoPlayerManager<MetaData> mVideoPlayerManager = new SingleVideoPlayerManager(new PlayerItemChangeListener() {
            @Override
            public void onPlayerItemChanged(MetaData metaData) {

            }
        });*/
        container_items.setRefreshing(true);


        getPost(context, getFragmentManager(), post_list, container_items);


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

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        story_list.setLayoutManager(layoutManager);


/*

        final LinearLayoutManager postmanger = new LinearLayoutManager(context);
        postmanger.setOrientation(LinearLayoutManager.VERTICAL);
        post.setLayoutManager(postmanger);*/

        final int[] size = {post_list.size()};
        String string = String.valueOf(size[0]);
        boolean arrraaay = Boolean.valueOf(string);
/*
        post.setNestedScrollingEnabled(true);
*/


        postAdapter = new PostAdapter(getActivity(), post_list, getFragmentManager());
        post.setAdapter(postAdapter);


        container_items.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (s == 0) {


                    s = 1;
                    rr = 0;
                    post_list.clear();
                    getPost(context, getFragmentManager(), post_list, container_items);
                }
            }
        });


        if (Connection.isconnection(context)) {
            storyModelArrayList.clear();
            getStory();


        } else {
            Toast.makeText(context, "Internet not available !", Toast.LENGTH_LONG).show();
        }


        post.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {

                mScrollState = scrollState;

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


                if (rr == 7) {

                    Log.e("Loading", "Loading");
                } else if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {

                    if (!loading) {
                        loading = true;
                        getPost(getActivity(), getFragmentManager(), post_list, container_items);
                    }
                }
            }
        });



 /*       post.setOnScrollListener(new EndlessRecyclerOnScrollListener(postmanger) {
            @Override
            public void onLoadMore(int current_page) {
                {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            if (mLoadingItems == true) {
                                mLoadingItems = false;

                            } else if (!loading) {
                                loading = true;
                                getPost(getActivity(), getFragmentManager());
                                postAdapter.notifyDataSetChanged();
                            }


                        }
                    }, 8000);
                }


            }
        });
*/








      /* array_story.clear();

        adapter = new Story_adapter(getActivity(), storyModelArrayList);
        story_list.setAdapter(adapter);

        array_story.add("1");*/


        return v;
    }


    public Runnable setKM = new Runnable() {
        public void run() {
            getPost(getActivity(), getFragmentManager(), post_list, container_items);
        }
    };

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
        Connection.getconnectionVolley(url, param, Config.getHeaderParam(context), context, lis_res, lis_error);

    }


    private void getStory() {

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
                    story_list.setAdapter(story_adapter);

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

        Connection.postconnection(url, params, Config.getHeaderParam(context), context, lis_res, lis_error);

    }


    public static void getPost(final Context context, final FragmentManager manager, final ArrayList<PostModel> arrayList, final SwipeRefreshLayout swipeRefreshLayout) {


        final View mProgressBarFooter = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.loaderview, null, false);


        Log.e("ArraySize", "" + post_list.size());
        String url = Config.baseurl + Config.getuserpost + "/" + pref_master.getUID() + "/" + arrayList.size();
        String json = "";
        if (swipeRefreshLayout.isRefreshing()) {

        } else {
            post.addFooterView(mProgressBarFooter);

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
                Log.e("Response", response);

                post.removeFooterView(mProgressBarFooter);


                if (swipeRefreshLayout.isRefreshing()) {
                    Home.s = 0;
                    swipeRefreshLayout.setRefreshing(false);
                }

                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    if (res_flag.equals("200")) {
                        JSONArray jarray = new JSONArray(jobj.getString("data"));

                        for (int i = 0; i < jarray.length(); i++) {

                            JSONObject jsonObject = jarray.getJSONObject(i);
                            PostModel postModel = new PostModel();
                            postModel.setPostid(jsonObject.getString("postid"));
                            postModel.setUserid(jsonObject.getString("userid"));
                            postModel.setPosttype(jsonObject.getString("posttype"));
                            postModel.setProfileiamge(jsonObject.getString("profileiamge"));
                            postModel.setPostfilename(jsonObject.getString("postfilename"));
                            postModel.setCaption(jsonObject.getString("caption"));
                            postModel.setUsername(jsonObject.getString("fullname"));
                            postModel.setFullname(jsonObject.getString("username"));
                            postModel.setEmail(jsonObject.getString("email"));
                            postModel.setPostdatetime(jsonObject.getString("postdatetime"));
                            postModel.setLike(jsonObject.getString("like"));
                            postModel.setComment(jsonObject.getString("comment"));
                            postModel.setShare(jsonObject.getString("repost"));
                            postModel.setRepost(jsonObject.getString("share"));
                            postModel.setMylike(jsonObject.getString("mylike"));
                            postModel.setMySave(jsonObject.getString("mySave"));


                            JSONArray jsonArray1 = new JSONArray(jsonObject.getString("postfilename"));
                            ArrayList<ImageModel> post_image_list = new ArrayList<>();

                            for (int j = 0; j < jsonArray1.length(); j++) {
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                post_image_list.add(new ImageModel(jsonObject1.getString("image"), jsonObject1.getString("thumb")));
                            }

                            postModel.setImageModels(post_image_list);
                            post_list.add(postModel);

                        }

                    } else if (res_flag.equals("400")) {
                        rr = 7;
                    }

                    postAdapter.notifyDataSetChanged();

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


    public static void UploadPost(final Context context, String postuserid, String postid, final FragmentManager manager) {


        RepostModel repostModel = new RepostModel();


        String url = Config.baseurl + Config.repost;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("userid", pref_master.getUID());
            jobj_row.put("postuserid", postuserid);
            jobj_row.put("postid", postid);
            jobj_row.put("posttype", postype);



          /*  JSONObject postarray = new JSONObject();
            postarray.put("image",jsonArray);*/


            JSONArray jarray_user = new JSONArray();


            for (int i = 0; i < post_image_list.size(); i++) {
                RepostImagemodel repostImagemodel = post_image_list.get(i);
                JSONObject jobj_user = new JSONObject();
                jobj_user.put("image", repostImagemodel.getImage());
                jarray_user.put(jobj_user);

            }
            Log.e("Arrayyy_jinn", ":" + jarray_user);


            jobj_row.put("postfilename", jarray_user);


            jobj_row.put("caption", caption);


            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("repost", jarray_loginuser);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Map<String, String> params = new HashMap<>();
        params.put("data", "" + jobj_loginuser.toString().replaceAll("\\\\", ""));
        Log.e("rajupload_request", ":" + params.toString());
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
                Log.e("uplOADResponse", " : " + response);

                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    if (res_flag.equals("200")) {
                        JSONArray jarray = new JSONArray(jobj.getString("data"));
                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jobj1 = jarray.getJSONObject(i);
                        }
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

    public static void GetPostUrl(final Context context, String postid) {
        Pref_Master pref_master = new Pref_Master(context);
        String url = Config.baseurl + Config.copyurl;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();
            jobj_row.put("postid", postid);

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("copyurl", jarray_loginuser);
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
                Log.e("Response", " : " + response);
                ///     obj_dialog.dismiss();

                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    if (res_flag.equals("200")) {
                        JSONArray jarray = new JSONArray(jobj.getString("data"));

                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jobj1 = jarray.getJSONObject(i);
                            String urll;
                            urll = jobj1.getString("url");
                            Intent sendIntent = new Intent();
                            sendIntent.setType("text/plain");
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, urll);
                            context.startActivity(sendIntent);
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


    public static void GetFacebookUrl(final Context context, String postid) {
        Pref_Master pref_master = new Pref_Master(context);
        String url = Config.baseurl + Config.copyurl;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();
            jobj_row.put("postid", postid);

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("copyurl", jarray_loginuser);
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
                Log.e("Response", " : " + response);
                ///     obj_dialog.dismiss();

                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    if (res_flag.equals("200")) {
                        JSONArray jarray = new JSONArray(jobj.getString("data"));

                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jobj1 = jarray.getJSONObject(i);
                            String urll;
                            urll = jobj1.getString("url");


                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("text/plain");
                            share.putExtra(Intent.EXTRA_TEXT, urll);
                            share.setPackage("com.facebook.katana"); //Facebook App package
                            context.startActivity(Intent.createChooser(share, "Title of the dialog the system will open"));
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


    public static void CopyUrl(final Context context, String postid) {
        Pref_Master pref_master = new Pref_Master(context);
        String url = Config.baseurl + Config.copyurl;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();
            jobj_row.put("postid", postid);

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("copyurl", jarray_loginuser);
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
                Log.e("Response", " : " + response);
                ///     obj_dialog.dismiss();

                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    if (res_flag.equals("200")) {
                        JSONArray jarray = new JSONArray(jobj.getString("data"));

                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jobj1 = jarray.getJSONObject(i);
                            String urll;
                            urll = jobj1.getString("url");
                            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                clipboard.setText(urll);
                            } else {
                                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", urll);
                                clipboard.setPrimaryClip(clip);

                                Toast.makeText(context, "Link Copied", Toast.LENGTH_LONG).show();
                            }
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


    public static void Flag(final Context context, String postid, final int position) {
        Pref_Master pref_master = new Pref_Master(context);
        String url = Config.baseurl + Config.blockuserpost;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();
            jobj_row.put("userid", pref_master.getUID());
            jobj_row.put("postid", postid);

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("blockuserpost", jarray_loginuser);
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
                Log.e("Response", " : " + response);
                ///     obj_dialog.dismiss();

                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    if (res_flag.equals("200")) {
                        post_list.remove(post_list.get(position));
                        postAdapter.notifyDataSetChanged();

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











   /* private void getImage() {


        String url = Config.baseurl + Config.getuserpost + "/" + pref_master.getUID();
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
                        post_image_list = new ArrayList<>();
                        for (int i = 0; i < jarray.length(); i++) {

                            JSONObject jsonObject = jarray.getJSONObject(i);
                            PostModel postModel = new PostModel();
                            postModel.setPostfilename(jsonObject.getString("postfilename"));
                            JSONArray imagearray = new JSONArray(jsonObject.getString("postfilename"));

                            for (int j = 0; j < imagearray.length(); j++) {

                                JSONObject jsonObject1 = jarray.getJSONObject(i);
                                postModel.setImage(jsonObject1.getString("image"));
                            }
                            post_image_list.add(postModel);
                        }

                    } else {
                        DialogBox.setPOP(context, jobj.getString("msg").toString());
                    }

                    customViewPagerm = new CustomViewPager(getActivity(), post_image_list);
                    v.setAdapter(customViewPagerm);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        Response.ErrorListener lis_error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
// signup.setClickable(true);
                    *//*obj_dialog.dismiss();*//*
            }
        };
        Connection.getconnectionVolley(url, param, Config.getHeaderParam(getActivity()), context, lis_res, lis_error);

    }*/

    public static void Repost(final Context context, final String postid, final String postuserid, final FragmentManager manager) {
        final Pref_Master pref_master = new Pref_Master(context);
        String url = Config.baseurl + Config.getrepostdetails;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();
            jobj_row.put("userid", pref_master.getUID());
            jobj_row.put("postid", postid);

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("getrepostdetails", jarray_loginuser);
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
                Log.e("Response", " : " + response);
                ///     obj_dialog.dismiss();

                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    if (res_flag.equals("200")) {
                        JSONArray jarray = new JSONArray(jobj.getString("data"));

                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jobj1 = jarray.getJSONObject(i);


                            postype = jobj1.getString("posttype");
                            caption = jobj1.getString("caption");
                            JSONArray jsonArray1 = new JSONArray(jobj1.getString("filename"));

                            post_image_list.clear();

                            for (int j = 0; j < jsonArray1.length(); j++) {
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                RepostImagemodel repostImagemodel = new RepostImagemodel();
                                repostImagemodel.setImage(jsonObject1.getString("image").substring(jsonObject1.getString("image").lastIndexOf("/") + 1));
                                post_image_list.add(repostImagemodel);
                            }

                        }


                    } else {
                        // DialogBox.setPOP(context(), jobj.getString("msg").toString());
                    }
                    UploadPost(context, postuserid, postid, manager);
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


    public static void Delete_Post(final Context context, String postid, final int position) {
        Pref_Master pref_master = new Pref_Master(context);
        String url = Config.baseurl + Config.deleteuserpost;
        JSONObject jobj_loginuser = new JSONObject();
        mLoadingItems = true;
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();
            jobj_row.put("userid", pref_master.getUID());
            jobj_row.put("postid", postid);

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("deleteuserpost", jarray_loginuser);
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
                Log.e("DeleteResponse", " : " + response);
                ///     obj_dialog.dismiss();


                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    if (res_flag.equals("200")) {

                        post_list.remove(post_list.get(position));
                        postAdapter.notifyDataSetChanged();

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


    public static void Like(final Context context, String postid, final FragmentManager manager) {
        Pref_Master pref_master = new Pref_Master(context);
        String url = Config.baseurl + Config.likepost;
        JSONObject jobj_loginuser = new JSONObject();
        mLoadingItems = true;
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();
            jobj_row.put("userid", pref_master.getUID());
            jobj_row.put("postid", postid);

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("likepost", jarray_loginuser);
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
                         /*   if (context instanceof PostDetail) {
                                ((PostDetail) context).getPost();
                            }*/
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


    public static class PostAdapter extends BaseAdapter {

        Context context;
        LayoutInflater layoutInflater;
        ViewHolder holder;
        int go;
        int value, value1, recyclervalue;
        Drawable perk_active, perk_like, unfillsave, fillsave;
        ArrayList<PostModel> post_list = new ArrayList<>();
        FragmentManager manager;
        LinearLayout header;
        private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR
                = new DecelerateInterpolator();
        private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR
                = new AccelerateInterpolator();


        public PostAdapter(Context context, ArrayList<PostModel> post_list, FragmentManager manager) {
            this.context = context;
            this.post_list = post_list;
            this.manager = manager;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            perk_active = context.getResources().getDrawable(R.drawable.fill_heart_3x);
            perk_like = context.getResources().getDrawable(R.drawable.like);
            unfillsave = context.getResources().getDrawable(R.drawable.share_home);
            fillsave = context.getResources().getDrawable(R.drawable.share_home_fill);
        }

        @Override
        public int getCount() {
            return post_list.size();
        }

        @Override
        public Object getItem(int i) {
            return post_list.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View itemView, ViewGroup viewGroup) {
            holder = new ViewHolder();
            final PostModel postModel = post_list.get(position);
            LayoutInflater inflater = LayoutInflater.from(context);


            if (itemView == null) {

                itemView = LayoutInflater.from(context).inflate(R.layout.post_cust, null);
                holder.postpic = (CircleImageView) itemView.findViewById(R.id.post_pic);
                holder.posttext = (MyRiad_Pro_Regular) itemView.findViewById(R.id.post_text);
                holder.sharebtn = (LinearLayout) itemView.findViewById(R.id.share_btn);
                holder.heartlike = (ImageView) itemView.findViewById(R.id.heartlike);
                holder.commentimg = (ImageView) itemView.findViewById(R.id.commentimg);
                holder.postvideo = (VideoView) itemView.findViewById(R.id.video_post);
                holder.sendimg = (ImageView) itemView.findViewById(R.id.sendimg);
                holder.shareimg = (ImageView) itemView.findViewById(R.id.shareimg);
                holder.post_play = (ImageView) itemView.findViewById(R.id.play_post);
                holder.save_home = (ImageView) itemView.findViewById(R.id.save_home);
                holder.recyclerView = (RecyclerView) itemView.findViewById(R.id.post);
                holder.textpost = (EmojiconTextView) itemView.findViewById(R.id.text_post);
                holder.textlayout = (RelativeLayout) itemView.findViewById(R.id.textlayout);
                holder.medialayout = (RelativeLayout) itemView.findViewById(R.id.media_layout);
                holder.likecount = (TextView) itemView.findViewById(R.id.likecounts);
                holder.commentcount = (TextView) itemView.findViewById(R.id.commentscounts);
                holder.sharecount = (TextView) itemView.findViewById(R.id.sharecounts);
                holder.postdate = (MyRiad_Pro_Regular) itemView.findViewById(R.id.post_date);
                holder.alpha = (MyRiad_Pro_Regular) itemView.findViewById(R.id.alpha);
                holder.suggestion = (RecyclerView) itemView.findViewById(R.id.suggestion);
                holder.textss = (TextView) itemView.findViewById(R.id.textss);
                holder.heartImageView = (ImageView) itemView.findViewById(R.id.heart);
                holder.circleBackground = itemView.findViewById(R.id.circleBg);
                holder.post_textusername = (MyRiad_Pro_Regular) itemView.findViewById(R.id.post_textusername);
                // repostcount = (TextView) itemView.findViewById(R.id.repostcounts);
                itemView.setTag(holder);
            } else {
                holder = (ViewHolder) itemView.getTag();
            }
            final String userid = post_list.get(position).getUserid();
            final String postid = post_list.get(position).getPostid();
            final String mylike = post_list.get(position).getMylike();
            final String like = post_list.get(position).getLike();
            holder.postdate.setText(post_list.get(position).getPostdatetime());
            pref_master.setStr_user_posttime(post_list.get(position).getPostdatetime());
            holder.likecount.setText(post_list.get(position).getLike());


            holder.likecount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("fo", userid);
                    bundle.putString("postid", postid);
                    bundle.putString("lo", "0");
                    LikeScreen fragment = new LikeScreen();
                    FragmentTransaction transaction = manager.beginTransaction();
                    fragment.setArguments(bundle);
                    transaction.replace(R.id.whole, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();


                    Log.e("HEllo", "Hello");
                }
            });


            holder.commentcount.setText(post_list.get(position).getComment());

            holder.commentcount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CommentScreen.class);
                    intent.putExtra("fo", userid);
                    intent.putExtra("postid", postid);
                    intent.putExtra("lo", "0");
                    context.startActivity(intent);
                }
            });
            holder.sharecount.setText(post_list.get(position).getShare());
            //   holder.repostcount.setText(post_list.get(position).getRepost());
            int likcountsss = Integer.parseInt(post_list.get(position).getLike());
            likcountsss++;

            if (post_list.get(position).getMySave().equals("1")) {
                holder.save_home.setImageDrawable(fillsave);
            } else if (post_list.get(position).getMySave().equals("0")) {
                holder.save_home.setImageDrawable(unfillsave);
            }


            holder.save_home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (post_list.get(position).getMySave().equals("0")) {

                        if (value1 == 1) {

                            value1 = 0;
                        } else {
                            SaveHome.Save(context, postid, manager);
                            postModel.setMySave("1");
                            post_list.add(postModel);
                            notifyDataSetChanged();
                            value1 = 1;
                            holder.save_home.setImageDrawable(fillsave);
                        }


                    } else if (post_list.get(position).getMySave().equals("1")) {


                        postModel.setMySave("0");
                        post_list.add(postModel);
                        notifyDataSetChanged();
                        holder.save_home.setImageDrawable(unfillsave);

                    }

                }
            });

            if (post_list.get(position).getMylike().equals("1")) {
                holder.heartlike.setImageDrawable(perk_active);
            } else if (post_list.get(position).getMylike().equals("0")) {
                holder.heartlike.setImageDrawable(perk_like);
            }


            holder.heartlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("IFFFFFFF", "FED");

                    if (post_list.get(position).getMylike().equals("0")) {

                        if (value == 1) {

                            value = 0;


                        } else {

                            ImageView imageView = (ImageView) view.findViewById(R.id.heartlike);
                            int likcountsss = Integer.parseInt(post_list.get(position).getLike());
                            int finalLikcountsss = likcountsss + 1;
                            Like(context, postid, manager);
                            postModel.setLike(Integer.toString(finalLikcountsss));
                            postModel.setMylike("1");
                            notifyDataSetChanged();
                            value = 1;
                            holder.likecount.setText(post_list.get(position).getLike());
                            imageView.setImageDrawable(perk_active);
                        }


                    } else if (post_list.get(position).getMylike().equals("1")) {

                        ImageView imageView = (ImageView) view.findViewById(R.id.heartlike);
                        int likcountsss = Integer.parseInt(post_list.get(position).getLike());
                        int finalLikcountsss = likcountsss - 1;
                        postModel.setLike(Integer.toString(finalLikcountsss));
                        postModel.setMylike("0");
                        notifyDataSetChanged();
                        imageView.setImageDrawable(perk_like);
                        Like(context, postid, manager);
                    }


                }

            });

            holder.textlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, CommentScreen.class);
                    i.putExtra("postid", postid);
                    i.putExtra("intent", "home");
                    context.startActivity(i);
                }
            });


            ArrayList singleSectionItems = post_list.get(position).getImageModels();

            Userimage_Adapter userimage_adapter = new Userimage_Adapter(context, singleSectionItems, postid);

            holder.recyclerView.setHasFixedSize(true);
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            holder.recyclerView.setAdapter(userimage_adapter);
            holder.recyclerView.setNestedScrollingEnabled(false);

            holder.recyclerView.addOnItemTouchListener(new RecyclerItemDoubleClickListener(context, new RecyclerItemDoubleClickListener.OnItemDoubleClickListener() {
                @Override
                public void onItemDoubleClick(View view, int positions) {
                    if (post_list.get(position).getMylike().equals("0")) {

                        if (recyclervalue == 1) {

                            recyclervalue = 0;


                        } else {


                            int likcountsss = Integer.parseInt(post_list.get(position).getLike());
                            int finalLikcountsss = likcountsss + 1;
                            Like(context, postid, manager);
                            postModel.setLike(Integer.toString(finalLikcountsss));
                            postModel.setMylike("1");
                            notifyDataSetChanged();
                            recyclervalue = 1;
                            holder.likecount.setText(post_list.get(position).getLike());
                            holder.heartlike.setImageDrawable(perk_active);
                            Log.e("Position", "" + position);
                            Log.e("Log", "Like");

                        }


                    } else if (post_list.get(position).getMylike().equals("1")) {




                        Log.e("Position", "" + position);
                        Log.e("Log", "UnLike");
                    }
                }
            }));


            if (position == 0) {
                getSuggestion(holder.suggestionModels);


                holder.suggestion.setVisibility(View.GONE);
                holder.suggestion.setHasFixedSize(true);
                holder.suggestion.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                holder.suggestion.setNestedScrollingEnabled(false);
                holder.textss.setVisibility(View.GONE);

            } else {
                holder.suggestion.setVisibility(View.GONE);
                holder.textss.setVisibility(View.GONE);

            }


            if (post_list.get(position).getPosttype().equals("4")) {
                holder.medialayout.setVisibility(View.GONE);
            } else if (post_list.get(position).getPosttype().equals("1")) {
//                holder.post_play.setVisibility(View.GONE);
                //     holder.postvideo.setVisibility(View.GONE);
                holder.recyclerView.setVisibility(View.VISIBLE);
                holder.medialayout.setVisibility(View.VISIBLE);

            } else if (post_list.get(position).getPosttype().equals("2")) {
//                holder.post_play.setVisibility(View.GONE);
                transfer = 1;
//                holder.postvideo.setVisibility(View.GONE);
                holder.recyclerView.setVisibility(View.VISIBLE);
                holder.medialayout.setVisibility(View.VISIBLE);

            }

          /*   else if (post_list.get(position).getPosttype().equals("1")){
                holder.post_play.setVisibility(View.GONE);
                holder.postvideo.setVisibility(View.GONE);
            }
*/
//            holder.postvideo.requestFocus();
            holder.commentimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, CommentScreen.class);
                    i.putExtra("postid", postid);
                    i.putExtra("intent", "home");
                    context.startActivity(i);
                }
            });

           /* holder.postvideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                // Close the progress bar and play the video
                public void onPrepared(MediaPlayer mp) {
                    holder.postvideo.seekTo(holder.postion);
                    holder.postvideo.start();
                }
            });

*/
          /*  holder.post_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });*/


            holder.sharebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String id = post_list.get(position).getUserid();
                    if (post_list.size() == 0) {

                    }
                    if (id.equals(pref_master.getUID())) {

                        DialogBox.SetDelete(context, postid, position);

                    }
                    if (!id.equals(pref_master.getUID())) {

                        DialogBox.SetShare(context, postid, position);
                    }
                }
            });


            holder.postusername = post_list.get(position).getUserid();

            holder.posttext.setText(post_list.get(position).getUsername());

            holder.post_textusername.setText(post_list.get(position).getFullname());


            holder.alpha.setText("@");


            holder.postusername = post_list.get(position).getUserid();

            holder.postuserid = post_list.get(position).getUserid();


            holder.posttext.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    String id = post_list.get(position).getUserid();


                    if (post_list.size() == 0) {

                    }
                    if (id.equals(pref_master.getUID())) {

                        Bundle bundle = new Bundle();
                        bundle.putString("id", userid);
                        Profile fragment = new Profile();
                        FragmentTransaction transaction = manager.beginTransaction();
                        fragment.setArguments(bundle);
                        transaction.replace(R.id.whole, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();



                    }
                    if (!id.equals(pref_master.getUID())) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id", userid);
                        OtherUser fragment = new OtherUser();
                        FragmentTransaction transaction = manager.beginTransaction();
                        fragment.setArguments(bundle);
                        transaction.replace(R.id.whole, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        pref_master.setOtherUserid(userid);

                    }

                }
            });
            String pic = post_list.get(position).getProfileiamge();

            if (pic.equals("")) {
                Picasso.with(context)
                        .load(R.drawable.user)
                        .placeholder(R.drawable.user)
                        .into(holder.postpic);
            } else {
                Picasso.with(context)
                        .load(post_list.get(position).getProfileiamge())
                        .placeholder(R.drawable.user)
                        .into(holder.postpic);
            }

            holder.postpic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, Tabs.class);
                    intent.putExtra("fragmentcode", Config.Fragment_ID.Userother);
                    intent.putExtra("id", userid);
                    pref_master.setOtherUserid(userid);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);


                }
            });




          /*  CustomViewPager adapter = new CustomViewPager(getActivity(), post_list);
            holder.viewPager.setAdapter(adapter);*/

            if (post_list.get(position).getCaption().equals("")) {
                holder.textpost.setVisibility(View.GONE);
            } else {
                holder.textpost.setVisibility(View.VISIBLE);
                holder.textpost.setText(post_list.get(position).getCaption());
            }

            holder.sendimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogBox.setRepost(context, postid, userid, manager);
                }
            });


            holder.shareimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GetPostUrl(context, postid);
                }
            });


            return itemView;
        }

        private class ViewHolder {

            CircleImageView postpic;
            MyRiad_Pro_Regular posttext;
            MyRiad_Pro_Regular post_textusername, alpha;
            LinearLayout sharebtn;
            ImageView post;
            ImageView heartlike, heartunlike;
            ImageView commentimg;
            ImageView sendimg;
            ArrayList<PostModel> post_list = new ArrayList<>();
            MyRiad_Pro_Regular postdate;
            ImageView shareimg;
            ImageView post_play;
            VideoView postvideo;
            EmojiconTextView textpost;
            String postusername;
            ImageView save_home;
            RelativeLayout textlayout;
            RelativeLayout medialayout;
            String like = "";
            ImageView heartImageView;
            String postuserid;
            RecyclerView recyclerView;
            View circleBackground;
            ArrayList<SuggestionModel> suggestionModels = new ArrayList<>();
            Suggestion_adapter suggestion_adapter;
            RecyclerView suggestion;
            TextView likecount, commentcount, sharecount, textss;
            int postion = 0;
        }


        private void getSuggestion(ArrayList<SuggestionModel> arrayList) {

            String url = Config.baseurl + Config.suggestionlist;
            JSONObject jobj_loginuser = new JSONObject();
            //  obj_dialog.show();
            try {

                JSONObject jobj_row = new JSONObject();

                jobj_row.put("userid", pref_master.getUID());
                jobj_row.put("limit", 0);

                JSONArray jarray_loginuser = new JSONArray();
                jarray_loginuser.put(jobj_row);

                jobj_loginuser.put("suggestionlist", jarray_loginuser);
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
                    ///     obj_dialog.dismiss();

                    try {
                        JSONObject jobj = new JSONObject(response);
                        String res_flag = jobj.getString("status");
                        if (res_flag.equals("200")) {
                            JSONArray jarray = new JSONArray(jobj.getString("data"));

                            for (int i = 0; i < jarray.length(); i++) {
                                JSONObject jobj1 = jarray.getJSONObject(i);


                                SuggestionModel suggestionModel = new SuggestionModel();
                                suggestionModel.setFullname(jobj1.getString("fullname"));
                                suggestionModel.setImagepath(jobj1.getString("imagepath"));
                                suggestionModel.setUserid(jobj1.getString("userid"));
                                holder.suggestionModels.add(suggestionModel);


                            }

                        } else if (res_flag.equals("400")) {

                        /*DialogBox.setPOP(context, jobj.getString("msg").toString());*/
                        }

                        holder.suggestion_adapter = new Suggestion_adapter(context, holder.suggestionModels);
                        holder.suggestion.setAdapter(holder.suggestion_adapter);

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

    public String likesss(String s) {

        return s;
    }


    public static Runnable run = new Runnable() {
        @Override
        public void run() {

            story_list.scrollToPosition(Story_adapter.pos + 1);

        }
    };


    public static void Follow(Context context, String userid) {
        Pref_Master pref_master = new Pref_Master(context);
        String url = Config.baseurl + Config.addfollowing;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();
            jobj_row.put("userid", pref_master.getUID());
            jobj_row.put("followingid", userid);

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("addfollowing", jarray_loginuser);
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
                Log.e("Response", " : " + response);
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


    public static void Unfollow(Context context, String userid) {
        Pref_Master pref_master = new Pref_Master(context);
        String url = Config.baseurl + Config.removefollowing;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();
            jobj_row.put("userid", pref_master.getUID());
            jobj_row.put("followingid", userid);

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("removefollowing", jarray_loginuser);
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
                Log.e("Response", " : " + response);
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
