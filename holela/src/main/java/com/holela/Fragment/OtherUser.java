package com.holela.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.holela.Activity.MainActivity;
import com.holela.Activity.Otp_Activity;
import com.holela.Activity.Tabs;
import com.holela.Adapter.PostDetailAdapter;
import com.holela.Controller.DialogBox;
import com.holela.Controller.MyRiad_Pro_Regular;
import com.holela.Controller.Textview_open_regular;
import com.holela.Controller.WrapContentHeightViewPager;
import com.holela.Models.PostDetailModel;
import com.holela.R;
import com.holela.Utils.Config;
import com.holela.Utils.Connection;
import com.holela.Utils.Pref_Master;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class OtherUser extends Fragment {

    TabLayout tabLayout;
    WrapContentHeightViewPager viewPager;
    Context context;
    String myfollowing;
    NestedScrollView nestedScrollView;
    LinearLayout header, containeree;
    ViewPagerAdapter adapter;
    int start, position;
    Textview_open_regular headusername;
    ImageView settingicon;
    FragmentManager manager;
    String getUserid;
    static Pref_Master pref_master;
    Button Follow, Unfollow;
    MyRiad_Pro_Regular followercounts, followingcounts, followertext, followingtext;
    String block;
    int counts;
    ImageView holelamain;
    ImageView chat_icon;

    static String userid;
    CircleImageView circleImageView;
    MyRiad_Pro_Regular post, followers, following, username;

    private int[] tabIcons = {
            R.drawable.list3x,
            R.drawable.grid,
            R.drawable.fill_heart_3x,
    };


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

                       FragmentManager fm = getFragmentManager();
                       FragmentTransaction ft = fm.beginTransaction();
                       ft.remove(OtherUser.this);
                       ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                       ft.commit();
                   }
                   return false;
               }
           });
       }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_other_user, container, false);
        context = getActivity();
        chat_icon = (ImageView) view.findViewById(R.id.chat_icon);
        circleImageView = (CircleImageView) view.findViewById(R.id.circleImageView);
        viewPager = (WrapContentHeightViewPager) view.findViewById(R.id.viewpagerr);
        containeree = (LinearLayout) view.findViewById(R.id.containere);
        headusername = (Textview_open_regular) view.findViewById(R.id.headusername);
        post = (MyRiad_Pro_Regular) view.findViewById(R.id.postcounts);
        followers = (MyRiad_Pro_Regular) view.findViewById(R.id.followerscounts);
        following = (MyRiad_Pro_Regular) view.findViewById(R.id.followingcounts);
        Follow = (Button) view.findViewById(R.id.follow);
        followercounts = (MyRiad_Pro_Regular) view.findViewById(R.id.followerscounts);
        followingcounts = (MyRiad_Pro_Regular) view.findViewById(R.id.followingcounts);
        followertext = (MyRiad_Pro_Regular) view.findViewById(R.id.followerstext);
        followingtext = (MyRiad_Pro_Regular) view.findViewById(R.id.followingtext);
        Unfollow = (Button) view.findViewById(R.id.unfollow);
        holelamain = (ImageView) view.findViewById(R.id.holelamain);
        username = (MyRiad_Pro_Regular) view.findViewById(R.id.username);
        pref_master = new Pref_Master(getActivity());


        final Bundle bundle = getArguments();
        userid = bundle.getString("id");


        setupViewPager(viewPager);


        tabLayout = (TabLayout) view.findViewById(R.id.profilee_tabs);
        tabLayout.setupWithViewPager(viewPager);
        createTabIcons();
        getPost();
        FollowCheck();

        followercounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, Tabs.class);
                intent.putExtra("fragmentcode", Config.Fragment_ID.Followerslist);
                intent.putExtra("fo", userid);
                intent.putExtra("lo","1");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

/*
                Fragment newFragment = new Followerslist();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.frame_home2, newFragment);
                Bundle bundle = new Bundle();
                bundle.putString("fo", getUserid);
                bundle.putString("lo", "1");
                newFragment.setArguments(bundle);
                transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commit();*/

            }
        });

        followingcounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, Tabs.class);
                intent.putExtra("fragmentcode", Config.Fragment_ID.FollowingList);
                intent.putExtra("fo", userid);
                intent.putExtra("lo","1");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);


           /*     Fragment newFragment = new FollowingList();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back

                transaction.replace(R.id.frame_home2, newFragment);
                Bundle bundle = new Bundle();
                bundle.putString("fo", getUserid);
                bundle.putString("lo", "1");
                newFragment.setArguments(bundle);
                transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commit();*/
            }
        });


        followertext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, Tabs.class);
                intent.putExtra("fragmentcode", Config.Fragment_ID.Followerslist);
                intent.putExtra("fo", userid);
                intent.putExtra("lo","1");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
/*
                Fragment newFragment = new Followerslist();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.frame_home2, newFragment);
                Bundle bundle = new Bundle();
                bundle.putString("fo", getUserid);
                bundle.putString("lo", "1");
                newFragment.setArguments(bundle);
                transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commit();*/
            }
        });


        followingtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, Tabs.class);
                intent.putExtra("fragmentcode", Config.Fragment_ID.FollowingList);
                intent.putExtra("fo", userid);
                intent.putExtra("lo","1");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

          /*      Fragment newFragment = new FollowingList();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back

                transaction.replace(R.id.frame_home2, newFragment);
                Bundle bundle = new Bundle();
                bundle.putString("fo", getUserid);
                bundle.putString("lo", "1");
                newFragment.setArguments(bundle);
                transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commit();*/
            }
        });


        holelamain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
            }
        });


        Follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Follow.setVisibility(View.GONE);
                Unfollow.setVisibility(View.VISIBLE);
                Follow();
            }
        });


        Unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Unfollow.setVisibility(View.GONE);
                Follow.setVisibility(View.VISIBLE);
                Unfollow();

            }
        });


        chat_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogBox.SetOtherUSer(getActivity(), pref_master.getUID(), userid);
            }
        });


        return view;
    }


    public void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new otherUserPost(), "");
        adapter.addFragment(new OtherUserMedia(), "");
        adapter.addFragment(new OtherUserLike(), "");
        viewPager.setAdapter(adapter);
    }


    private void createTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }


    private void getPost() {

        String url = Config.baseurl + Config.getuserDetails + "/" + userid + "/" + pref_master.getUID();
        final String json = "";


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

                            JSONObject jsonObject = jarray.getJSONObject(i);

                            post.setText(jsonObject.getString("posts"));
                            followers.setText(jsonObject.getString("followers"));
                            following.setText(jsonObject.getString("followings"));
                            username.setText(jsonObject.getString("fullname"));
                            String propic = jsonObject.getString("profilepic");
                            String uid = jsonObject.getString("userid");
                            block = jsonObject.getString("blocked");
                            getUserid = jsonObject.getString("userid");


                            headusername.setText(jsonObject.getString("fullname"));


                            if (propic.equals("")) {
                                Picasso.with(getActivity())
                                        .load(R.drawable.user)
                                        .placeholder(R.drawable.ic_defalult)
                                        .into(circleImageView);
                            } else {
                                Picasso.with(getActivity())
                                        .load(propic)
                                        .placeholder(R.drawable.user)
                                        .into(circleImageView);
                            }


                        }
                    } else {
                        DialogBox.setPOP(getActivity(), jobj.getString("msg").toString());
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

    private void FollowCheck() {

        String url = Config.baseurl + Config.checkfollow;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("userid", pref_master.getUID());
            jobj_row.put("followingid", userid);

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("checkfollow", jarray_loginuser);
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

                            myfollowing = jobj1.getString("myfollowing");

                            if (myfollowing.equals("0")) {
                                Follow.setVisibility(View.VISIBLE);
                                Unfollow.setVisibility(View.GONE);
                            } else if (myfollowing.equals("1")) {
                                Follow.setVisibility(View.GONE);
                                Unfollow.setVisibility(View.VISIBLE);
                            }

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

        Connection.postconnection(url, params, Config.getHeaderParam(getActivity()), getActivity(), lis_res, lis_error);

    }


    public void Follow() {
        Pref_Master pref_master = new Pref_Master(getActivity());
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

        Connection.postconnection(url, params, Config.getHeaderParam(getActivity()), getActivity(), lis_res, lis_error);

    }


    public void Unfollow() {
        Pref_Master pref_master = new Pref_Master(getActivity());
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

        Connection.postconnection(url, params, Config.getHeaderParam(getActivity()), getActivity(), lis_res, lis_error);

    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            Log.e("Paageer", "" + position);
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public void setfragment(String title, Fragment fragment) {

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_home2, fragment);
            Bundle bundle = new Bundle();
            bundle.putString("userid", userid);
            fragment.setArguments(bundle);
            fragmentTransaction.commit();
        }


        /*  public void setfragment(String title, Fragment fragment) {

              FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
              fragmentTransaction.replace(R.id.frame_home2, fragment);
              Bundle bundle = new Bundle();
              bundle.putString("id",id);
              fragment.setArguments(bundle);
              fragmentTransaction.commit();
          }
  */
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    public static void Block(final Context context) {

        String url = Config.baseurl + Config.blockuser;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("userid", pref_master.getUID());
            jobj_row.put("blockeduserid", userid);

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("blockuser", jarray_loginuser);
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
                Log.e("block_Response", " : " + response);
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


    public static void Unblock(final Context context) {

        String url = Config.baseurl + Config.unblockuser;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("userid", pref_master.getUID());
            jobj_row.put("blockeduserid", userid);

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("unblockuser", jarray_loginuser);
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
                Log.e("block_Response", " : " + response);
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
