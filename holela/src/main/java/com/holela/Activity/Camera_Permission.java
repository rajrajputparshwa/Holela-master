package com.holela.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.SensorManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.holela.Controller.DialogBox;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

public class Camera_Permission extends AppCompatActivity {

    TextView postee;
    private int orientation;
    ImageView postimage, storyimage;
    private SensorManager sensorManager = null;
    Pref_Master pref_master;
    private int degrees = -1;
    TextView cancelll;
    ProgressBar progressBar3;
    String value;
    TextView status;
    Bitmap bitmap;
    String dir;
    Bitmap risized;
    private ExifInterface exif;
    private TransferUtility transferUtility;
    Context context = this;
    int upload = 0;
    String api, gallery;
    LinearLayout progresslinerlayout;
    Bitmap myBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera__permission);
        dir = "/holela/Camera/";
        postimage = (ImageView) findViewById(R.id.postimage);
        postee = (TextView) findViewById(R.id.postee);
        cancelll = (TextView) findViewById(R.id.cancelll);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        transferUtility = Util.getTransferUtility(this);
        pref_master = new Pref_Master(Camera_Permission.this);
        progressBar3 = (ProgressBar) findViewById(R.id.progressBar3);
        storyimage = (ImageView) findViewById(R.id.storyimage);
        status = (TextView) findViewById(R.id.statusssss);
        progresslinerlayout = (LinearLayout) findViewById(R.id.progresslinerlayout);


        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            value = extras.getString("camera");
            api = extras.getString("api");
            gallery = extras.getString("camera");
        }

        //Drawable d = new BitmapDrawable(getResources(), myBitmap);
        if (api.equals("1")) {
            myBitmap = decodeSampledBitmapFromFile(value, 1000, 700);

            try {
                risized = rotateImage(myBitmap, 90);
                risized.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(value));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } else if (api.equals("0")) {
            myBitmap = decodeSampledBitmapFromFile(value, 1000, 700);

            try {
                myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(value));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


        if (api.equals("1")) {
            postimage.setVisibility(View.VISIBLE);
            postimage.setImageBitmap(risized);
            upload = 1;
        } else {
            storyimage.setVisibility(View.VISIBLE);
            storyimage.setImageBitmap(myBitmap);
            upload = 2;
        }


        cancelll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });


        postee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (upload == 1) {
                    upload = 0;
                    beginUpload(value);
                } else if (upload == 2) {
                    upload = 0;
                    beginUpload(gallery);
                }
            }
        });

        /*Toast.makeText(getApplicationContext(), Camera_Activity.newfile, Toast.LENGTH_LONG).show();*/


    }

    private RotateAnimation getRotateAnimation(float toDegrees) {
        float compensation = 0;

        if (Math.abs(degrees - toDegrees) > 180) {
            compensation = 360;
        }

        // When the device is being held on the left side (default position for
        // a camera) we need to add, not subtract from the toDegrees.
        if (toDegrees == 0) {
            compensation = -compensation;
        }

        // Creating the animation and the RELATIVE_TO_SELF means that he image
        // will rotate on it center instead of a corner.
        RotateAnimation animation = new RotateAnimation(degrees, toDegrees - compensation, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        // Adding the time needed to rotate the image
        animation.setDuration(250);

        // Set the animation to stop after reaching the desired position. With
        // out this it would return to the original state.
        animation.setFillAfter(true);

        return animation;
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

        final TransferObserver observer = transferUtility.upload(pref_master.getUserurl() + "/story/images", file.getName(), file, new ObjectMetadata());

        /*
         * Note that usually we set the transfer listener after initializing the
         * transfer. However it isn't required in this sample app. The flow is
         * click upload button -> start an activity for image selection
         * startActivityForResult -> onActivityResult -> beginUpload -> onResume
         * -> set listeners to in progress transfers.
         */
        // observer.setTransferListener(new UploadListener());

        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (state.COMPLETED.equals(observer.getState())) {

                    Toast.makeText(Camera_Permission.this, "File Upload Complete", Toast.LENGTH_SHORT).show();
                    UploadPost();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);

                }
                if (state.IN_PROGRESS.equals(observer.getState())) {
                    progresslinerlayout.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

                long _bytesCurrent = bytesCurrent;
                long _bytesTotal = bytesTotal;

                double percentage = ((double) _bytesCurrent / (double) _bytesTotal * 100);
                Log.d("percentage", "" + percentage);
                percentage = (int) Math.round(percentage * 100) / (double) 100;

                progressBar3.setProgress((int) percentage);
                status.setText(percentage + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                Toast.makeText(Camera_Permission.this, "" + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void UploadPost() {


        String url = Config.baseurl + Config.poststory;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("userid", pref_master.getUID());
            jobj_row.put("imagename", value.substring(value.lastIndexOf("/") + 1));
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


    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }
        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
