package com.holela.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.carlosmuvi.segmentedprogressbar.SegmentedProgressBar;
import com.holela.Adapter.GetOtherUserStory;
import com.holela.Controller.DialogBox;
import com.holela.Fragment.Home;
import com.holela.Models.GetUserStoryModel;
import com.holela.R;
import com.holela.Utils.Config;
import com.holela.Utils.Connection;
import com.holela.Utils.Pref_Master;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class GetUserStory extends AppCompatActivity {

    Context context = this;
    RecyclerView autoScrollViewPager;
    String value, turn;
    ArrayList<GetUserStoryModel> userstory_list = new ArrayList<>();
    Pref_Master pref_master;
    GetOtherUserStory getOtherUserStory;
    SegmentedProgressBar segmented_progressbar;
    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_get_user_story);

        autoScrollViewPager = (RecyclerView) findViewById(R.id.auto_view_pager);
        segmented_progressbar = (SegmentedProgressBar) findViewById(R.id.segmented_progressbar);
        autoScrollViewPager.setHasFixedSize(true);
        autoScrollViewPager.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        autoScrollViewPager.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                // true: consume touch event
                // false: dispatch touch event
                return true;
            }
        });

        pref_master = new Pref_Master(context);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("user");
            turn = extras.getString("turn");
//get the value based on the key
        }

        if (turn.equals("0")) {
            getStoryPost();
        } else if (turn.equals("1")) {
            Home.run.run();
            getStoryPost();
        }
    }


    public void getStoryPost() {

        Log.e("ArraySize", "" + userstory_list.size());
        String url = Config.baseurl + Config.getuserstory + "/" + value;
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

                            JSONObject jsonObject = jarray.getJSONObject(i);
                            GetUserStoryModel getUserStoryModel = new GetUserStoryModel();
                            getUserStoryModel.setDatetime(jsonObject.getString("datetime"));
                            getUserStoryModel.setUsername(jsonObject.getString("username"));
                            getUserStoryModel.setProfileimage(jsonObject.getString("profileimage"));
                            getUserStoryModel.setImagepath(jsonObject.getString("image"));
                            getUserStoryModel.setFiletype(jsonObject.getString("filetype"));
                            userstory_list.add(getUserStoryModel);

                        }

                    } else {
                        DialogBox.setPOP(context, jobj.getString("msg").toString());
                    }

                    getOtherUserStory = new GetOtherUserStory(context, userstory_list, run);
                    autoScrollViewPager.setAdapter(getOtherUserStory);
                    autoScrollViewPager.smoothScrollToPosition(getOtherUserStory.getItemCount());
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

    public Runnable run = new Runnable() {
        @Override
        public void run() {

            if (GetOtherUserStory.pos == GetOtherUserStory.lastindex) {
                userstory_list.clear();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("turn", "1");
                startActivity(i);
            } else {
                autoScrollViewPager.scrollToPosition(GetOtherUserStory.pos + 1);
            }

        }
    };


    public void setProgress() {

        segmented_progressbar.setSegmentCount(GetOtherUserStory.size);
        segmented_progressbar.setVisibility(View.VISIBLE);
        segmented_progressbar.playSegment(3000);

        timer = new CountDownTimer(3000, 3000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                run.run();
            }
        };
        timer.start();
    }


    public void setVideoProgress() {

        segmented_progressbar.setSegmentCount(GetOtherUserStory.size);
        segmented_progressbar.setVisibility(View.VISIBLE);
        segmented_progressbar.playSegment(GetOtherUserStory.videoduration);

        timer = new CountDownTimer(GetOtherUserStory.videoduration, GetOtherUserStory.videoduration) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                run.run();
            }
        };
        timer.start();
    }

}
