package com.holela.Activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.carlosmuvi.segmentedprogressbar.SegmentedProgressBar;
import com.holela.Adapter.GetUserStory_Adapter;
import com.holela.Controller.DialogBox;
import com.holela.Fragment.Home;
import com.holela.Models.GetUserStoryModel;
import com.holela.R;
import com.holela.Utils.Config;
import com.holela.Utils.Connection;
import com.holela.Utils.Pref_Master;
import com.holela.Utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class StoryActivity extends AppCompatActivity {

    public static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1777;
    ImageView imageHolder;
    private TransferUtility transferUtility;
    private NotificationManager mNotifyManager;
    Context context = this;
    private NotificationCompat.Builder mBuilder;
    TextView post;
    String picurl;
    String selectedpath;
    static RecyclerView autoScrollViewPager;

    RelativeLayout relative;
    public static int value = 0;
    ArrayList<GetUserStoryModel> userstory_list = new ArrayList<>();
    Pref_Master pref_master;
    static GetUserStory_Adapter getUserStory_adapter;
    private static final String TAG = "Story Upload";
    static final int speedScroll = 5000;
    public static SegmentedProgressBar segmented_progressbar;
    public static final Handler handler = new Handler();
    static CountDownTimer timer;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_story);
        pref_master = new Pref_Master(context);
        post = (TextView) findViewById(R.id.next);
        imageHolder = (ImageView) findViewById(R.id.captured_photo);
        autoScrollViewPager = (RecyclerView) findViewById(R.id.auto_view_pager);
        relative = (RelativeLayout) findViewById(R.id.relative);
        transferUtility = Util.getTransferUtility(this);
        segmented_progressbar = (SegmentedProgressBar) findViewById(R.id.segmented_progressbar);


        autoScrollViewPager.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        autoScrollViewPager.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                // true: consume touch event
                // false: dispatch touch event
/*

                if (GetUserStory_Adapter.pos == GetUserStory_Adapter.lastindex) {


                  */
/*  Log.e("Run a","run");
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);*//*

                } else {
                    Log.e("Run b","run");
                    autoScrollViewPager.scrollToPosition(GetUserStory_Adapter.pos + 1);
                    Log.e("POSssssss", String.valueOf(GetUserStory_Adapter.pos + 1));
                    segmented_progressbar.setCompletedSegments(GetUserStory_Adapter.pos);
                    timer.cancel();

                }
*/


                return true;
            }
        });


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beginUpload(picurl);
            }
        });

        if (Home.story == 1) {
            Home.story = 0;
            Intent i = new Intent(getApplicationContext(), CameraViewpager.class);
            startActivity(i);
        } else {

            getStoryPost();

        }
// start default camera

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE == requestCode && resultCode == RESULT_OK) {
            relative.setVisibility(View.VISIBLE);
            File file = new File(selectedpath);

            Bitmap bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 1000, 700);
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file.getAbsolutePath()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            picurl = file.getAbsolutePath();

            imageHolder.setImageBitmap(bitmap);
        } else {
            finish();
        }
    }


    public void getStoryPost() {

        Log.e("ArraySize", "" + userstory_list.size());
        String url = Config.baseurl + Config.getuserstory + "/" + pref_master.getUID();
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
                        autoScrollViewPager.setVisibility(View.VISIBLE);
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

                    } else if (res_flag.equals("400")) {
                        /*DialogBox.setPOP(context, jobj.getString("msg").toString());*/


                    }

                    getUserStory_adapter = new GetUserStory_Adapter(context, userstory_list);
                    autoScrollViewPager.setAdapter(getUserStory_adapter);
                    autoScrollViewPager.smoothScrollToPosition(getUserStory_adapter.getItemCount());

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


    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) { // BEST QUALITY MATCH

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    protected String getSaltString() {
        String SALTCHARS = "abcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 12) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }

        return salt.toString();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    private void beginUpload(String filePath) {

        CannedAccessControlList cannedAccessControlList;
        cannedAccessControlList = CannedAccessControlList.PublicRead;

        if (filePath == null) {
            Toast.makeText(this, "Could not find the filepath of the selected file",
                    Toast.LENGTH_LONG).show();
            return;
        }
        File file = new File(filePath);


//        Toast.makeText(getApplicationContext(),file.getName(), Toast.LENGTH_LONG).show();

        TransferObserver observer = transferUtility.upload(pref_master.getUserurl() + "/story/images", file.getName(), file, new ObjectMetadata());

        /*
         * Note that usually we set the transfer listener after initializing the
         * transfer. However it isn't required in this sample app. The flow is
         * click upload button -> start an activity for image selection
         * startActivityForResult -> onActivityResult -> beginUpload -> onResume
         * -> set listeners to in progress transfers.
         */
        // observer.setTransferListener(new UploadListener());

        observer.setTransferListener(new UploadListener());

    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private class UploadListener implements TransferListener {

        // Simply updates the UI list when notified.
        @Override
        public void onError(int id, Exception e) {
            Log.e(TAG, "Error during upload: " + id, e);
            //  updateList();
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            Log.d(TAG, String.format("onProgressChanged: %d, total: %d, current: %d",
                    id, bytesTotal, bytesCurrent));
            //  updateList();


        }

        @Override
        public void onStateChanged(int id, TransferState newState) {
            Log.d(TAG, "onStateChanged: " + id + ", " + newState);
            // updateList();

            mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(StoryActivity.this);

            mBuilder = new NotificationCompat.Builder(getBaseContext())
                    .setContentTitle("Upload")
                    .setContentText("Uploading")
                    .setSmallIcon(R.drawable.text_app_icon)
                    .setOngoing(false)
                    .setProgress(0, 0, true);

            mNotifyManager.notify(id, mBuilder.build());


            if (newState.equals(TransferState.COMPLETED)) {
                Toast.makeText(getApplicationContext(), "Completed", Toast.LENGTH_SHORT).show();
                mBuilder.setContentText("Upload Completed");
                // Removes the progress bar
                UploadPost();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

                mBuilder.setProgress(0, 0, false);
                mNotifyManager.notify(id, mBuilder.build());


            } else if (newState.equals(TransferState.FAILED)) {
                Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
            } else if (newState.equals(TransferState.IN_PROGRESS)) {
                Toast.makeText(getApplicationContext(), "Progress", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Runnable run = new Runnable() {
        @Override
        public void run() {

            if (GetUserStory_Adapter.pos == GetUserStory_Adapter.lastindex) {


                Log.e("Run a", "run");
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            } else {
                Log.e("Run b", "run");
                autoScrollViewPager.scrollToPosition(GetUserStory_Adapter.pos + 1);
            }

        }
    };


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (GetUserStory_Adapter.pos == GetUserStory_Adapter.lastindex) {


                Log.e("Run a", "run");
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            } else {
                Log.e("Run b", "run");
                autoScrollViewPager.scrollToPosition(GetUserStory_Adapter.pos + 1);
                segmented_progressbar.setCompletedSegments(GetUserStory_Adapter.pos);

            }


        }
    };


    private void UploadPost() {
        String url = Config.baseurl + Config.poststory;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("userid", pref_master.getUID());
            jobj_row.put("imagename", picurl.substring(picurl.lastIndexOf("/") + 1));
            jobj_row.put("filetype", "1");

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("poststory", jarray_loginuser);
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


    public void setProgress() {

        segmented_progressbar.setSegmentCount(GetUserStory_Adapter.size);
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


        segmented_progressbar.setSegmentCount(GetUserStory_Adapter.size);
        segmented_progressbar.setVisibility(View.VISIBLE);
        segmented_progressbar.playSegment(GetUserStory_Adapter.videoduration);

        timer = new CountDownTimer(GetUserStory_Adapter.videoduration, GetUserStory_Adapter.videoduration) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                run.run();
            }
        };
        timer.start();
    }


}
