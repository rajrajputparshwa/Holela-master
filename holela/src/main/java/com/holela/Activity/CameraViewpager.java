package com.holela.Activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.holela.Fragment.Camera;
import com.holela.Fragment.Gallery;
import com.holela.Fragment.Home;
import com.holela.Fragment.Video;
import com.holela.Fragment.VideoRecorder;
import com.holela.R;

import java.util.ArrayList;
import java.util.List;

public class CameraViewpager extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    String side;
    int position;
    Bundle bundle;

    private int[] tabIcons = {
            R.drawable.home_icon,
            R.drawable.search_white_icon,
/*
            R.drawable.camera_white_icon,
*/
            R.drawable.notification_icon,
    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_viewpager);

        tabLayout = (TabLayout) findViewById(R.id.taberrr);
        viewPager = (ViewPager) findViewById(R.id.viewpagerr);


        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(1);
         tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
             @Override
             public void onTabSelected(TabLayout.Tab tab) {
                 position = tab.getPosition();
                 if (position==2){
                     Intent i = new Intent(getApplicationContext(),VideoRecorder.class);
                     startActivity(i);
                 }  if (position==0){
                     Intent i = new Intent(getApplicationContext(),StoryGallery.class);
                     startActivity(i);
                 }
             }

             @Override
             public void onTabUnselected(TabLayout.Tab tab) {

             }

             @Override
             public void onTabReselected(TabLayout.Tab tab) {

             }
         });

    }


    public void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new Gallery(), getString(R.string.Eyegallery));
        adapter.addFragment(new Camera_Activity(), getString(R.string.EyeCamera));
        adapter.addFragment(new Video(), getString(R.string.EyeVideo));

        viewPager.setAdapter(adapter);

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

    private void createTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        /*tabLayout.getTabAt(2).setIcon(tabIcons[2]);*/
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }
}