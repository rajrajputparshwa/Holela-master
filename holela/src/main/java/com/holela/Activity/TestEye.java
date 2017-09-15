package com.holela.Activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.holela.Fragment.MainFragment;
import com.holela.Fragment.TestFrag;
import com.holela.R;

public class TestEye extends AppCompatActivity {
    TabLayout tabLayout;


    private int[] tabIcons = {
            R.drawable.home_icon,

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_eye);





        setfragment("", new TestFrag());







    }

    private void createTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
    }
    public void setfragment(String title, Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_home, fragment);
        fragmentTransaction.commit();
    }
}
