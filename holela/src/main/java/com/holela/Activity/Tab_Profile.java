package com.holela.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.holela.Fragment.*;
import com.holela.R;
import com.holela.Utils.Config;
import com.holela.Utils.Pref_Master;

public class Tab_Profile extends AppCompatActivity {
    int fragmentcode = 0;
    String id, Scroll, fo;
    Pref_Master pref_master;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab__profile);

        pref_master = new Pref_Master(getApplicationContext());
        if (getIntent().getExtras() != null) {
            fragmentcode = getIntent().getIntExtra("fragmentcode", 1);
            id = getIntent().getStringExtra("id");
            fo = getIntent().getStringExtra("fo");


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
                    setfragment("", new com.holela.Fragment.FollowingList());
                    break;


                case Config.Fragment_ID.Search:
                    fragmentcode = 1;
                    setfragment("", new Searchview());
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
        bundle.putInt("scroll", 3);
        fragment.setArguments(bundle);
        fragmentTransaction.commit();
    }
}
