package com.holela.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.iid.FirebaseInstanceId;
import com.holela.Fragment.Camera;
import com.holela.Fragment.EditProfile;
import com.holela.Fragment.Followerslist;
import com.holela.Fragment.FollowingList;
import com.holela.Fragment.Home;
import com.holela.Fragment.MainFragment;
import com.holela.Fragment.Notification;
import com.holela.Fragment.OtherUser;
import com.holela.Fragment.Profile;
import com.holela.Fragment.Search;
import com.holela.Fragment.Searchview;
import com.holela.Fragment.Setting;
import com.holela.R;
import com.holela.Utils.Config;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    FrameLayout frame_home;
    ImageView chat_icon;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    LinearLayout header;
    private AudioManager audio;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 29;
    CircleImageView eye;
    int fragmentcode = 0;
    String getfo;
    String id;
    int value = 0;
    int getvalue = 0;
    boolean doubleBackToExitPressedOnce = false;
    int start, position;
    private static final int STORAGE_PERMISSION_CODE = 123;

    private static final int STORAGE_PERMISSION_CODE_Write = 2245;
    private static final int STORAGE_PERMISSION_CODE_Writeaudio = 2232;

    private static final int STORAGE_PERMISSION_CODE_Writevideo = 22;

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 55;

    TabLayout tabLayout;

    private int[] tabIcons = {
            R.drawable.home_icon,
            R.drawable.search_white_icon,
            R.drawable.camera_white_icon,
            R.drawable.notification_icon,
            R.drawable.user_white_icon
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                audio.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                audio.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast

            } else {
                //Displaying another toast if permission is not granted

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frame_home = (FrameLayout) findViewById(R.id.frame_home);
        eye = (CircleImageView) findViewById(R.id.eye);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        checkAndRequestPermissions();
        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        Bundle bundle = getIntent().getExtras();


        if (getIntent().getExtras() != null) {
            fragmentcode = getIntent().getIntExtra("fragmentcode", 1);
            getvalue = getIntent().getIntExtra("scroll", value);
            getfo = bundle.getString("fo");


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

                case Config.Fragment_ID.Search:
                    fragmentcode = 1;
                    setfragment("", new Searchview());
                    break;

                case Config.Fragment_ID.Option:
                    fragmentcode = 1;
                    setfragment("", new Setting());
                    break;

                case Config.Fragment_ID.Followerslist:
                    fragmentcode = 1;
                    setfragment("", new Followerslist());
                    break;

                case Config.Fragment_ID.FollowingList:
                    fragmentcode = 1;
                    setfragment("", new FollowingList());
                    break;
            }
        } else {
            setfragment("", new MainFragment());
        }



      /*  if (!Settings.canDrawOverlays(this)) {
            Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            myIntent.setData(Uri.parse("package: " + getPackageName()));
            startActivityForResult(myIntent, 101);
        }*/
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Mytoken", refreshedToken);

       /* tabLayout= (TabLayout) findViewById(R.id.tabsmain);
        viewPager= (ViewPager) findViewById(R.id.viewpagermain);
        header= (LinearLayout) findViewById(R.id.headeree);
*/


      /*  setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


                viewPager.setCurrentItem(tab.getPosition());

                position = tab.getPosition();

                Log.e("Tabsss",""+position);

                if (position==4 || position==2){
                    header.setVisibility(View.GONE);
                }else {
                    header.setVisibility(View.VISIBLE);
                }


                if (position==2){
                    tabLayout.setVisibility(View.GONE);
                }else {
                    tabLayout.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.e("Tabs Unselected", ""+tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        createTabIcons();
*/

        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Eye.class);
                i.putExtra("api", "0");
                startActivity(i);
            }
        });


        setfragment("", new MainFragment());


    }


    public void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Home(), "");
        adapter.addFragment(new Search(), "");
        adapter.addFragment(new Camera(), "");
        adapter.addFragment(new Notification(), "");
        adapter.addFragment(new Profile(), "");
        viewPager.setAdapter(adapter);
    }


    private void createTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
    }


    public void setfragment(String title, Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_home, fragment);
        fragmentTransaction.commit();
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

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, STORAGE_PERMISSION_CODE);
    }


    private void requestWritePermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE_Write);
    }

    private void overlay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Show alert dialog to the user saying a separate permission is needed
            // Launch the settings activity if the user prefers
            Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            startActivity(myIntent);
        }
    }

    private void requestVideo() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAPTURE_VIDEO_OUTPUT) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAPTURE_VIDEO_OUTPUT)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAPTURE_VIDEO_OUTPUT}, STORAGE_PERMISSION_CODE_Writevideo);
    }

    private void requestCam() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, STORAGE_PERMISSION_CODE_Writevideo);
    }

    private void requestAudio() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, STORAGE_PERMISSION_CODE_Writeaudio);
    }


    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }


        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAPTURE_VIDEO_OUTPUT);
        }


        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }


        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


}
