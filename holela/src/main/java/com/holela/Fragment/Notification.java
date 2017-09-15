package com.holela.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.holela.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 5/13/2017.
 */

public class Notification extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    Context context;
    static int start;
    static int scroll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.notification, container, false);
        context = getActivity();

        viewPager = (ViewPager) v.findViewById(R.id.noti_viewpager);
        setupViewPager(viewPager);

        viewPager.setCurrentItem(1);


        tabLayout= (TabLayout) v.findViewById(R.id.noti_tabs);
        tabLayout.setSmoothScrollingEnabled(true);
        tabLayout.setupWithViewPager(viewPager);


        return v;
    }


    public void setfragment(String title, Fragment fragment) {

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_home, fragment);
        fragmentTransaction.commit();
    }


    public void setupViewPager(ViewPager viewPager){
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new Notification_Following(), "Following");
        adapter.addFragment(new Notification_You(),"You");

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
            Log.e("Paageer",""+position);
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
