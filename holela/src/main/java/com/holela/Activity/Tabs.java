package com.holela.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.holela.Fragment.Change_PassWord;
import com.holela.Fragment.EditProfile;
import com.holela.Fragment.Followerslist;
import com.holela.Fragment.FollowingList;
import com.holela.Fragment.Home;
import com.holela.Fragment.LikeScreen;
import com.holela.Fragment.MainFragment;
import com.holela.Fragment.Notification;
import com.holela.Fragment.OtherUser;
import com.holela.Fragment.Profile;
import com.holela.Fragment.Searchview;
import com.holela.Fragment.Setting;
import com.holela.R;
import com.holela.Utils.Config;
import com.holela.Utils.Pref_Master;

public class Tabs extends AppCompatActivity {
    int fragmentcode = 0;
    String id, Scroll, fo,lo,postid;
    Pref_Master pref_master;

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);
        pref_master = new Pref_Master(getApplicationContext());
        if (getIntent().getExtras() != null) {
            fragmentcode = getIntent().getIntExtra("fragmentcode", 1);
            id = getIntent().getStringExtra("id");
            fo = getIntent().getStringExtra("fo");
            lo = getIntent().getStringExtra("lo");
            postid = getIntent().getStringExtra("postid");


            switch (fragmentcode) {
                case Config.Fragment_ID.Userother:
                    fragmentcode = 1;
                    setfragment("", new OtherUser());
                    break;

                case Config.Fragment_ID.UserOwn:
                    fragmentcode = 1;
                    setfragment("", new Profile());
                    break;

                case Config.Fragment_ID.Myprofile:
                    fragmentcode = 1;
                    setfragment("", new Profile());
                    break;

                case Config.Fragment_ID.EditProfile:
                    fragmentcode = 1;
                    setfragment("", new EditProfile());
                    break;

                case Config.Fragment_ID.Notification:
                    fragmentcode = 1;
                    setfragment("", new Notification());
                    break;

                case Config.Fragment_ID.MainFragment:
                    fragmentcode = 1;
                    setfragment("", new MainFragment());
                    break;

                case Config.Fragment_ID.Followerslist:
                    fragmentcode = 1;
                    setfragment("", new Followerslist());
                    break;


                case Config.Fragment_ID.FollowingList:
                    fragmentcode = 1;
                    setfragment("", new FollowingList());
                    break;


                case Config.Fragment_ID.Search:
                    fragmentcode = 1;
                    setfragment("", new Searchview());
                    break;


                case Config.Fragment_ID.Setting:
                    fragmentcode = 1;
                    setfragment("", new Setting());
                    break;


                case Config.Fragment_ID.ChangePassword:
                    fragmentcode = 1;
                    setfragment("", new Change_PassWord());
                    break;

                case Config.Fragment_ID.Likes:
                    fragmentcode = 1;
                    setfragment("", new LikeScreen());
                    break;


            }
        } else {
            setfragment("", new MainFragment());
        }

    }

    public void setfragment(String title, Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_home2, fragment);
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("fo", fo);
        bundle.putString("postid", postid);
        bundle.putInt("scroll", 2);
        bundle.putString("lo",lo);
        fragment.setArguments(bundle);
        fragmentTransaction.commit();
    }

}
