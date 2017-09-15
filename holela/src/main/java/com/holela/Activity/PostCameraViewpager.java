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

import com.holela.Fragment.Gallery;
import com.holela.Fragment.PostCamera_Activity;
import com.holela.Fragment.Video;
import com.holela.Fragment.VideoRecorder;
import com.holela.R;

import java.util.ArrayList;
import java.util.List;

public class PostCameraViewpager extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    static int side;
    int position;
    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_camera_viewpager);


        tabLayout = (TabLayout) findViewById(R.id.taberrr);
        viewPager = (ViewPager) findViewById(R.id.viewpagerr);


        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(0);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                position = tab.getPosition();

                if (position==1){
                    Intent i = new Intent(getApplicationContext(),Eye.class);
                    i.putExtra("api","2");
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

        adapter.addFragment(new PostCamera_Activity(), getString(R.string.EyeCamera));
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
}
