package com.holela.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.holela.Activity.MainActivity;
import com.holela.Activity.Tabs;
import com.holela.Controller.DialogBox;
import com.holela.Controller.MyRiad_Pro_Regular;
import com.holela.Controller.Textview_open_regular;
import com.holela.R;
import com.holela.Utils.Config;
import com.holela.Utils.Connection;
import com.holela.Utils.Pref_Master;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Admin on 5/13/2017.
 */

public class Profile extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    Context context;
    NestedScrollView nestedScrollView;
    LinearLayout header, containeree;
    ViewPagerAdapter adapter;
    int start, position;
    FragmentManager manager;
    Bitmap bitmap;
    static int scroll;
    ImageView headmain;
    MyRiad_Pro_Regular followerstext, followingtext;
    Textview_open_regular headusername;
    Button EditProfile;
    Pref_Master pref_master;
    String userid;
    ProgressBar progressBar;
    CircleImageView circleImageView;
    MyRiad_Pro_Regular post, followers, following, username, statusss;

    ImageView chat_icon;

    private int[] tabIcons = {
            R.drawable.list3x,
            R.drawable.grid,
            R.drawable.fill_heart_3x,
            R.drawable.share_home_fill,
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile, container, false);


        context = getActivity();
        chat_icon = (ImageView) v.findViewById(R.id.chat_icon);

        viewPager = (ViewPager) v.findViewById(R.id.viewpagerr);
        containeree = (LinearLayout) v.findViewById(R.id.containere);
        EditProfile = (Button) v.findViewById(R.id.editprofile);
        headmain = (ImageView) v.findViewById(R.id.headmain);
        post = (MyRiad_Pro_Regular) v.findViewById(R.id.postcounts);
        followers = (MyRiad_Pro_Regular) v.findViewById(R.id.followerscounts);
        headusername = (Textview_open_regular) v.findViewById(R.id.headusername);
        following = (MyRiad_Pro_Regular) v.findViewById(R.id.followingcounts);
        circleImageView = (CircleImageView) v.findViewById(R.id.circleImageView);
        followerstext = (MyRiad_Pro_Regular) v.findViewById(R.id.followerstext);
        followingtext = (MyRiad_Pro_Regular) v.findViewById(R.id.followingtext);
        username = (MyRiad_Pro_Regular) v.findViewById(R.id.username);
        statusss = (MyRiad_Pro_Regular) v.findViewById(R.id.statusss);



        /*post.setNestedScrollingEnabled(true);*/
        pref_master = new Pref_Master(getActivity());
        getPost();

        headmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
            }
        });


        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

/*
                Intent intent = new Intent(context, Tabs.class);
                intent.putExtra("fragmentcode", Config.Fragment_ID.Followerslist);
                intent.putExtra("fo", userid);
                intent.putExtra("lo", "0");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
*/

           /*     Fragment newFragment = new Followerslist();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.frame_home, newFragment);
                Bundle bundle = new Bundle();
                bundle.putString("fo", userid);
                newFragment.setArguments(bundle);
                transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commit();*/


                Fragment newFragment = new Followerslist();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.relacon, newFragment);
                Bundle bundle = new Bundle();
                bundle.putString("fo", userid);
                newFragment.setArguments(bundle);
                transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commit();


            }
        });

        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent intent = new Intent(context, Tabs.class);
                intent.putExtra("fragmentcode", Config.Fragment_ID.FollowingList);
                intent.putExtra("fo", userid);
                intent.putExtra("lo", "0");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);



*/

                Fragment newFragment = new FollowingList();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.relacon, newFragment);
                Bundle bundle = new Bundle();
                bundle.putString("fo", userid);
                newFragment.setArguments(bundle);
                transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commit();

            /*    Fragment newFragment = new FollowingList();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back

                transaction.replace(R.id.frame_home, newFragment);

                Bundle bundle = new Bundle();
                bundle.putString("fo", userid);
                newFragment.setArguments(bundle);


                transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commit();*/

            }
        });

        followerstext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Fragment newFragment = new Followerslist();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.relacon, newFragment);
                Bundle bundle = new Bundle();
                bundle.putString("fo", userid);
                newFragment.setArguments(bundle);
                transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commit();
           /*     Fragment newFragment = new Followerslist();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.frame_home, newFragment);
                Bundle bundle = new Bundle();
                bundle.putString("fo", userid);
                newFragment.setArguments(bundle);
                transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commit();*/
            }
        });

        followingtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

/*
                Intent intent = new Intent(context, Tabs.class);
                intent.putExtra("fragmentcode", Config.Fragment_ID.FollowingList);
                intent.putExtra("fo", userid);
                intent.putExtra("lo", "0");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
*/

               /* Fragment newFragment = new FollowingList();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back

                transaction.replace(R.id.frame_home, newFragment);
                Bundle bundle = new Bundle();
                bundle.putString("fo", userid);
                newFragment.setArguments(bundle);
                transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commit();*/


                Fragment newFragment = new FollowingList();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.relacon, newFragment);
                Bundle bundle = new Bundle();
                bundle.putString("fo", userid);
                newFragment.setArguments(bundle);
                transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commit();

            }
        });

        EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Tabs.class);
                intent.putExtra("fragmentcode", Config.Fragment_ID.EditProfile);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);

            }
        });
        containeree.setVisibility(View.VISIBLE);

        setupViewPager(viewPager);

   /*     nestedScrollView= (NestedScrollView) v.findViewById(R.id.nestedscroll);
        nestedScrollView.setFillViewport(true);*/

        tabLayout = (TabLayout) v.findViewById(R.id.profilee_tabs);
        tabLayout.setupWithViewPager(viewPager);

        createTabIcons();


        chat_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //   Toast.makeText(getActivity(),"Hello",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), Tabs.class);
                intent.putExtra("fragmentcode", Config.Fragment_ID.Setting);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);


            }
        });


        return v;
    }

    private void createTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }

    public void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new UserOwnPost(), "");
        adapter.addFragment(new UserOwnMedia(), "");
        adapter.addFragment(new UserOwnLikes(), "");
        adapter.addFragment(new UserOwnSaves(), "");
        viewPager.setAdapter(adapter);
    }


    private void getPost() {

        String url = Config.baseurl + Config.getuserDetails + "/" + pref_master.getUID() + "/" + pref_master.getUID();
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
                            statusss.setText(jsonObject.getString("aboutme"));
                            String propic = jsonObject.getString("profilepic");
                            String usernamee = jsonObject.getString("fullname");
                            pref_master.setImage(propic);
                            pref_master.setStr_user_BIO(jsonObject.getString("aboutme"));
                            headusername.setText(jsonObject.getString("fullname"));
                            String logintype = jsonObject.getString("logintype");

                            userid = jsonObject.getString("userid");


                            if (logintype.equals("1")) {
                                if (propic.equals(jsonObject.getString("profilepic"))) {
                                    Picasso.with(getActivity())
                                            .load(propic)
                                            .placeholder(R.drawable.user)
                                            .into(circleImageView);


                                    Log.e("1", "1");
                                }

                            } else if (logintype.equals("2") && propic.equals("")) {

                                if (propic.equals("")) {

                                    Picasso.with(getActivity())
                                            .load(pref_master.getFbImage()) //extract as User instance method
                                            .resize(120, 120)
                                            .into(circleImageView);

                                } else {
                                    Picasso.with(getActivity())
                                            .load(propic)
                                            .placeholder(R.drawable.user)
                                            .into(circleImageView);

                                }

                                if (usernamee.equals("")) {
                                    username.setText(pref_master.getFullname());
                                } else {
                                    username.setText(jsonObject.getString("fullname"));
                                }

                                Log.e("2", pref_master.getFbImage());

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

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
