package com.holela.Activity;

import android.Manifest;
import android.app.NotificationManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.ViewFlipper;

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
import com.holela.file.FileUtils;
import com.holela.video.MediaController;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.github.rockerhieu.emojicon.EmojiconEditText;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
import static com.holela.Activity.UploadActivity.isDownloadsDocument;
import static com.holela.Activity.UploadActivity.isExternalStorageDocument;
import static com.holela.Activity.UploadActivity.isMediaDocument;

public class Eye extends AppCompatActivity {

    EmojiconEditText editText;
    TextView status, statusssss1;
    ImageView Gallary, Postimg1, Postimg2, Postimg3, Postimg4, VideoBtn, camera;
    VideoView videoView;
    private NotificationManager mNotifyManager;
    int postchecker = 0;
    private ProgressBar progressBar, progressBar3, progressBar31;
    String videothumbnailpath;
    private NotificationCompat.Builder mBuilder;
    public static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1777;
    String getpostone = "";
    private static final String TAG = "PostUpload";
    int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 03;
    int id = 1;
    Bitmap bitmap;
    String copyimagepath;
    int videobitmap;
    private Pref_Master pref_master;
    TextView Postbtn, textss;
    String copythumb;
    String selectedpath;
    String api = "vhgkkv";
    JSONArray jsonArray = new JSONArray();
    ImageView close1, close2, close3, close4;
    private int PICK_IMAGE_REQUEST = 1;
    private int REQUEST_TAKE_GALLERY_VIDEO = 2;
    HashMap<String, String> map;
    Bitmap risized;
    String Image_String_one = "";
    String Image_String_two = "";
    public static String pathee;
    LinearLayout progresslinerlayout, progresslinerlayout1;
    ArrayList<String> arrayList = new ArrayList<String>();
    String Image_String_three = "";
    private TransferUtility transferUtility;
    String Image_String_four = "";
    int i_img_one = 0;
    int videooint = 0;
    Uri fileUri, uri;
    Context context = this;
    ViewFlipper TruitonFlipper;
    float initialX;
    int i_img_two = 0;
    int i_img_three = 0;
    File tempFile;
    int i_img_four = 0;
    String path, ipaddress;
    private static final int STORAGE_PERMISSION_CODE = 123;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        deleteTempFile();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

         /*   uri = data.getData();
            try {
                path = getPath(uri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                Bitmap bitmap = decodeSampledBitmapFromFile(path,720,480);
                try {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(path));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                SetImage(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
*/

            uri = data.getData();

            try {
                path = getPath(uri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            File source = new File(path);

            File folder = new File(Environment.getExternalStorageDirectory().toString() + "/holela/GalleryImage");
            folder.mkdirs();
            //Save the path as a string value
            String extStorageDirectory = folder.toString();
            //Create New file and name it Image2.PNG
            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();

            File file = new File(extStorageDirectory, ts + ".jpg");
            String pathh = file.getAbsolutePath();


            File f = new File(pathh);
            if (!f.exists()) {
                try {
                    f.createNewFile();
                    copyFile(source, f);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            //Drawable d = new BitmapDrawable(getResources(), myBitmap);
            Bitmap myBitmap = decodeSampledBitmapFromFile(f.getAbsolutePath(), 1000, 700);

            try {
                myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(f.getAbsolutePath()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            copyimagepath = f.getAbsolutePath();


            SetImage(myBitmap, copyimagepath);


        }


        if (requestCode == REQUEST_TAKE_GALLERY_VIDEO && resultCode == RESULT_OK && data != null && data.getData() != null) {


            Uri uri = data.getData();


            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                if (uri != null) {
                    Cursor cursor = getContentResolver().query(uri, null, null, null, null, null);

                    try {
                        if (cursor != null && cursor.moveToFirst()) {

                            String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            Log.e(TAG, "Display Name: " + displayName);

                            int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                            String size = null;
                            if (!cursor.isNull(sizeIndex)) {
                                size = cursor.getString(sizeIndex);
                            } else {
                                size = "Unknown";
                            }
                            Log.i(TAG, "Size: " + size);

                            tempFile = FileUtils.saveTempFile(displayName, this, uri);

                            long length = tempFile.length();
                            length = length / 1024;

                            if (length >= 100000) {
                                Toast.makeText(getApplicationContext(), "File Size is Too High", Toast.LENGTH_LONG).show();
                            } else {
                                new VideoCompressor().execute();
                            }
                        }
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
           /* case MotionEvent.ACTION_DOWN:
                initialX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float finalX = event.getX();
                if (initialX > finalX) {
                    if (TruitonFlipper.getDisplayedChild() == 1)
                        break;

				*//*TruitonFlipper.setInAnimation(this, R.anim.in_right);
                TruitonFlipper.setOutAnimation(this, R.anim.out_left);*//*
                    TruitonFlipper.showNext();
                } else {
                    if (TruitonFlipper.getDisplayedChild() == 0)
                        break;

				*//*TruitonFlipper.setInAnimation(this, R.anim.in_left);
                TruitonFlipper.setOutAnimation(this, R.anim.out_right);*//*

                    TruitonFlipper.showPrevious();
                }
                break;*/
        }
        return false;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteTempFile();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eye);

        requestStoragePermission();
        getAddreess();
        editText = (EmojiconEditText) findViewById(R.id.what);
        editText.setCursorVisible(true);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Postimg1.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Postimg1.setVisibility(View.VISIBLE);
            }
        });

        camera = (ImageView) findViewById(R.id.camera);
        Gallary = (ImageView) findViewById(R.id.gallery);
        Postimg1 = (ImageView) findViewById(R.id.postimg1);
        Postimg2 = (ImageView) findViewById(R.id.postimg2);
        Postimg3 = (ImageView) findViewById(R.id.postimg3);
        Postimg4 = (ImageView) findViewById(R.id.postimg4);
        videoView = (VideoView) findViewById(R.id.videopost);
        textss = (TextView) findViewById(R.id.textss);
        close1 = (ImageView) findViewById(R.id.close1);
        close2 = (ImageView) findViewById(R.id.close2);
        close3 = (ImageView) findViewById(R.id.close3);
        close4 = (ImageView) findViewById(R.id.close4);
        Postbtn = (TextView) findViewById(R.id.postbtn);
        VideoBtn = (ImageView) findViewById(R.id.videoo);
        progressBar = (ProgressBar) findViewById(R.id.loading);
        status = (TextView) findViewById(R.id.statusssss);
        progresslinerlayout = (LinearLayout) findViewById(R.id.progresslinerlayout);
        progressBar3 = (ProgressBar) findViewById(R.id.progressBar3);

        statusssss1 = (TextView) findViewById(R.id.statusssss1);
        progresslinerlayout1 = (LinearLayout) findViewById(R.id.progresslinerlayout1);
        progressBar31 = (ProgressBar) findViewById(R.id.progressBar31);


        pref_master = new Pref_Master(context);
        transferUtility = Util.getTransferUtility(this);


        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            selectedpath = extras.getString("camera");
            api = extras.getString("api");
        }


        Intent intent = getIntent();
        // Get the action of the intent
        String action = intent.getAction();
        // Get the type of intent (Text or Image)
        String type = intent.getType();


        if (Intent.ACTION_SEND.equals(action) && type != null) {
            // When tyoe is 'text/plain'
            if (type.startsWith("image/")) { // When type is 'image/*'
                handleSendImage(intent); // Handle single image being sent
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent); // Handle multiple images being sent
            }
        } else {
            // Handle other intents, such as being started from the home screen

            if (api.equals("1")) {

                File file = new File(selectedpath);
                Log.e("File", "File");
                Bitmap bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 1000, 700);


                try {
                    risized = rotateImage(bitmap, 90);
                    risized.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(selectedpath));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                SetCameraImage(risized, selectedpath);
            } else if (api.equals("2")) {

                Intent ii = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                // create a file to save the video
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

                // set the image file name
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                // set the video image quality to high
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

                // start the Video Capture Intent
                startActivityForResult(ii, REQUEST_TAKE_GALLERY_VIDEO);
            }
        }


     /*   TruitonFlipper = (ViewFlipper) findViewById(R.id.flipper);
        TruitonFlipper.setInAnimation(this, android.R.anim.fade_in);
        TruitonFlipper.setOutAnimation(this, android.R.anim.fade_out);*/


        VideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videooint = 1;
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);


            }
        });


        Gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i_img_one == 0 || i_img_two == 0 || i_img_three == 0 || i_img_four == 0)

                    requestStoragePermission();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PostCameraViewpager.class);
                startActivity(i);
            }
        });

        Postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager) view.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                if (videooint == 1) {
                    beginUpload2(MediaController.load);
                    uploadvideothumb(copythumb);

                } else if (videooint == 0 && !Image_String_one.equals("") || !Image_String_two.equals("") || !Image_String_three.equals("") || !Image_String_four.equals("")) {
                    mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mBuilder = new NotificationCompat.Builder(Eye.this);
                    validate();
                } else if (videooint == 0 && !editText.getText().toString().equals("") && !(editText.getText().toString().trim().length() == 0)) {
                    UploadPost();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }
            }
        });


        close1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Postimg1.setVisibility(View.GONE);
                i_img_one = 1;
                Image_String_one = "";
                close1.setVisibility(View.GONE);
                jsonArray.remove(0);
            }
        });


        close2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Postimg2.setVisibility(View.GONE);
                i_img_two = 1;
                Image_String_two = "";
                close2.setVisibility(View.GONE);
                jsonArray.remove(1);
            }
        });


        close3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Postimg3.setVisibility(View.GONE);
                i_img_three = 1;
                Image_String_three = "";
                close3.setVisibility(View.GONE);
                jsonArray.remove(2);
            }
        });


        close4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Postimg4.setVisibility(View.GONE);
                i_img_four = 1;
                Image_String_four = "";
                close4.setVisibility(View.GONE);
                jsonArray.remove(3);
            }
        });


    }

    //method to get the file path from uri
    private String getPath(Uri uri) throws URISyntaxException {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
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

    private Uri getOutputMediaFileUri(int type) {

        return FileProvider.getUriForFile(Eye.this, "com.holela.fileprovider",
                getOutputMediaFile(type));
    }


    public void SetCameraImage(Bitmap bitmapff, String s) {
        BitmapDrawable bdrawable = new BitmapDrawable(context.getResources(), bitmapff);

        if (i_img_one == 0) {
            i_img_one = 1;
           /* img_one.setVisibility(View.VISIBLE);
            img_remove_one.setVisibility(View.VISIBLE);*/

            Postimg1.setVisibility(View.VISIBLE);
            Image_String_one = "";
            Image_String_one = s;

            //  File oldFile = new File(Image_String_one);

            // String is = newFile.getPath();

            //      Toast.makeText(getApplicationContext(),is,Toast.LENGTH_SHORT).show();

           /* Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString() + ".jpg";
*/
            /*String newFile = Image_String_one.replace(Image_String_one.substring(Image_String_one.lastIndexOf("/") + 1), ts);*/
            map = new HashMap<String, String>();
            JSONObject imageone = new JSONObject();
            try {
                imageone.put("image", Image_String_one.substring(Image_String_one.lastIndexOf("/") + 1));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(imageone);
            Log.e("Image1", "" + arrayList.toString());
            //     Toast.makeText(getApplicationContext(), Image_String_one, Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                Postimg1.setBackground(bdrawable);
            }
        } else if (i_img_two == 0) {
            i_img_two = 1;
           /* img_two.setVisibility(View.VISIBLE);
            img_remove_two.setVisibility(View.VISIBLE);*/
            Postimg2.setVisibility(View.VISIBLE);
            Image_String_two = "";
            Image_String_two = s;
            JSONObject imagetwo = new JSONObject();
            try {
                imagetwo.put("image", Image_String_two.substring(Image_String_two.lastIndexOf("/") + 1));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(imagetwo);
            Log.e("Image2", "" + arrayList.toString());
            //  Toast.makeText(getApplicationContext(), Image_String_two, Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                Postimg2.setBackground(bdrawable);
            }
        } else if (i_img_three == 0) {
            i_img_three = 1;
           /* img_three.setVisibility(View.VISIBLE);
            img_remove_three.setVisibility(View.VISIBLE);*/
            Postimg3.setVisibility(View.VISIBLE);
            Image_String_three = "";
            Image_String_three = s;
            JSONObject imagethree = new JSONObject();
            try {
                imagethree.put("image", Image_String_three.substring(Image_String_three.lastIndexOf("/") + 1));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(imagethree);
            Log.e("Image3", "" + arrayList.toString());
            //  Toast.makeText(getApplicationContext(), Image_String_three, Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                Postimg3.setBackground(bdrawable);
            }
        } else if (i_img_four == 0) {
            i_img_four = 1;
           /* img_three.setVisibility(View.VISIBLE);
            img_remove_three.setVisibility(View.VISIBLE);*/
            Postimg4.setVisibility(View.VISIBLE);
            Image_String_four = "";
            Image_String_four = s;
            JSONObject imagefour = new JSONObject();
            try {
                imagefour.put("image", Image_String_four.substring(Image_String_four.lastIndexOf("/") + 1));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(imagefour);
            Log.e("Image4", "" + arrayList.toString());
            //  Toast.makeText(getApplicationContext(), Image_String_four, Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "You Reached Maximum Limit to Select Image", Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                Postimg4.setBackground(bdrawable);
            }
        }
    }


    public void SetImage(Bitmap bitmap1, String copypath) {
        BitmapDrawable bdrawable = new BitmapDrawable(context.getResources(), bitmap1);

        if (i_img_one == 0) {
            i_img_one = 1;
           /* img_one.setVisibility(View.VISIBLE);
            img_remove_one.setVisibility(View.VISIBLE);*/
            close1.setVisibility(View.VISIBLE);
            Postimg1.setVisibility(View.VISIBLE);
            Image_String_one = "";
            Image_String_one = copypath;


            //  File oldFile = new File(Image_String_one);

            // String is = newFile.getPath();

            //      Toast.makeText(getApplicationContext(),is,Toast.LENGTH_SHORT).show();

           /* Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString() + ".jpg";
*/
            /*String newFile = Image_String_one.replace(Image_String_one.substring(Image_String_one.lastIndexOf("/") + 1), ts);*/
            map = new HashMap<String, String>();
            JSONObject imageone = new JSONObject();
            try {
                imageone.put("image", Image_String_one.substring(Image_String_one.lastIndexOf("/") + 1));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(imageone);
            Log.e("Image1", "" + arrayList.toString());
            //     Toast.makeText(getApplicationContext(), Image_String_one, Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                Postimg1.setBackground(bdrawable);
            }
        } else if (i_img_two == 0) {
            i_img_two = 1;
           /* img_two.setVisibility(View.VISIBLE);
            img_remove_two.setVisibility(View.VISIBLE);*/
            Postimg2.setVisibility(View.VISIBLE);
            Image_String_two = "";
            close2.setVisibility(View.VISIBLE);
            Image_String_two = copypath;

            JSONObject imagetwo = new JSONObject();
            try {
                imagetwo.put("image", Image_String_two.substring(Image_String_two.lastIndexOf("/") + 1));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(imagetwo);
            Log.e("Image2", "" + arrayList.toString());
            //  Toast.makeText(getApplicationContext(), Image_String_two, Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                Postimg2.setBackground(bdrawable);
            }
        } else if (i_img_three == 0) {
            i_img_three = 1;
           /* img_three.setVisibility(View.VISIBLE);
            img_remove_three.setVisibility(View.VISIBLE);*/
            Postimg3.setVisibility(View.VISIBLE);
            Image_String_three = "";
            close3.setVisibility(View.VISIBLE);
            Image_String_three = copypath;

            JSONObject imagethree = new JSONObject();
            try {
                imagethree.put("image", Image_String_three.substring(Image_String_three.lastIndexOf("/") + 1));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(imagethree);
            Log.e("Image3", "" + arrayList.toString());
            //  Toast.makeText(getApplicationContext(), Image_String_three, Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                Postimg3.setBackground(bdrawable);
            }
        } else if (i_img_four == 0) {
            i_img_four = 1;
           /* img_three.setVisibility(View.VISIBLE);
            img_remove_three.setVisibility(View.VISIBLE);*/
            Postimg4.setVisibility(View.VISIBLE);
            Image_String_four = "";
            close4.setVisibility(View.VISIBLE);
            Image_String_four = copypath;

            JSONObject imagefour = new JSONObject();
            try {
                imagefour.put("image", Image_String_four.substring(Image_String_four.lastIndexOf("/") + 1));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(imagefour);
            Log.e("Image4", "" + arrayList.toString());
            //  Toast.makeText(getApplicationContext(), Image_String_four, Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "You Reached Maximum Limit to Select Image", Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                Postimg4.setBackground(bdrawable);
            }
        }


    }


    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
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


        getpostone = file.getName();
//        Toast.makeText(getApplicationContext(),file.getName(), Toast.LENGTH_LONG).show();
        Log.e("Url", "" + pref_master.getUserurl());
        final TransferObserver observer = transferUtility.upload(pref_master.getUserurl() + "/post/images", file.getName(), file, new ObjectMetadata());

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

                    Toast.makeText(Eye.this, "File Upload Complete", Toast.LENGTH_SHORT).show();
                    validate();

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
                Toast.makeText(Eye.this, "" + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void Setvideo(String path) {

        videooint = 1;

        Postimg1.setVisibility(View.GONE);
        Postimg2.setVisibility(View.GONE);
        Postimg3.setVisibility(View.GONE);
        Postimg4.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);

        videoView.setVideoPath(path);

        videoView.start();


        JSONObject video = new JSONObject();
        try {
            video.put("image", path.substring(path.lastIndexOf("/") + 1));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(video);
    }


    private void beginUpload2(String filePath) {

        CannedAccessControlList cannedAccessControlList;
        cannedAccessControlList = CannedAccessControlList.PublicRead;

        if (filePath == null) {
            Toast.makeText(this, "Could not find the filepath of the selected file",
                    Toast.LENGTH_LONG).show();
            return;
        }
        File file = new File(filePath);


        getpostone = file.getName();
//        Toast.makeText(getApplicationContext(),file.getName(), Toast.LENGTH_LONG).show();
        Log.e("Url", "" + pref_master.getUserurl());
        final TransferObserver observer = transferUtility.upload(pref_master.getUserurl() + "/post/videos", file.getName(), file, new ObjectMetadata());

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

                    Toast.makeText(Eye.this, "File Upload Complete", Toast.LENGTH_SHORT).show();
                    UploadPost();

                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);

                    videooint = 0;

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

                percentage = Double.parseDouble(new DecimalFormat("##.#").format(percentage));

                progressBar3.setProgress((int) percentage);
                status.setText(percentage + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                Toast.makeText(Eye.this, "" + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


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

    private static File getOutputMediaFile(int type) {

        // Check that the SDCard is mounted
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().toString() + "/holela/StoryVideo");


        // Create the storage directory(MyCameraVideo) if it does not exist
        if (!mediaStorageDir.exists()) {

            if (!mediaStorageDir.mkdirs()) {


                Log.d("MyCameraVideo", "Failed to create directory MyCameraVideo.");
                return null;
            }
        }


        // Create a media file name

        // For unique file name appending current timeStamp with file name
        java.util.Date date = new java.util.Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(date.getTime());

        File mediaFile;

        if (type == MEDIA_TYPE_VIDEO) {

            // For unique video file name appending current timeStamp with file name
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
            pathee = mediaFile.getAbsolutePath();

        } else {
            return null;
        }

        return mediaFile;
    }

    private void deleteTempFile() {
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }


    public void validate() {

        if (!Image_String_one.equals("") && i_img_one == 1) {
            if (Connection.isconnection(context)) {
                //Upload_Images("1");
                // i = 1;
                textss.setText("1");
                beginUpload(Image_String_one);


                Image_String_one = "";

            } else {
                Toast.makeText(context, R.string.no_internet, Toast.LENGTH_LONG).show();
            }

        } else if (!Image_String_two.equals("") && i_img_two == 1) {
            if (Connection.isconnection(context)) {
                //   Upload_Images("2");
                //  i = 1;
                textss.setText("2");
                beginUpload(Image_String_two);

                Image_String_two = "";
            } else {
                Toast.makeText(context, R.string.no_internet, Toast.LENGTH_LONG).show();
            }

        } else if (!Image_String_three.equals("") && i_img_three == 1) {
            if (Connection.isconnection(context)) {
                //Upload_Images("3");
                //   i = 1;
                textss.setText("3");
                beginUpload(Image_String_three);

                Image_String_three = "";
            } else {
                Toast.makeText(context, R.string.no_internet, Toast.LENGTH_LONG).show();
            }

        } else if (!Image_String_four.equals("") && i_img_four == 1) {
            if (Connection.isconnection(context)) {
                //Upload_Images("3");
                //   i = 1;
                textss.setText("4");
                beginUpload(Image_String_four);

                Image_String_four = "";

            } else {
                Toast.makeText(context, R.string.no_internet, Toast.LENGTH_LONG).show();
            }

        } else {
            UploadPost();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }


    }

    public String getAddreess() {

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    //if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress() ) {
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        ipaddress = inetAddress.getHostAddress();


                    }
                }
            }
        } catch (SocketException ex) {
            /*Log.d(TAG, ex.toString());*/
        }


        return null;
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


    private void UploadPost() {

        if (!editText.getText().toString().equals("")) {
            postchecker = 4;
        }
        if (!editText.getText().toString().equals("") && i_img_one == 1 || i_img_two == 1 || i_img_three == 1 || i_img_four == 1) {
            postchecker = 1;
        }
        if (editText.getText().toString().equals("") && i_img_one == 1 || i_img_two == 1 || i_img_three == 1 || i_img_four == 1) {
            postchecker = 1;
        }
        if (!editText.getText().toString().equals("") && videooint == 1) {
            postchecker = 2;
        }
        if (editText.getText().toString().equals("") && videooint == 1) {
            postchecker = 2;
        }


        String url = Config.baseurl + Config.adduserpost;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("userid", pref_master.getUID());
            jobj_row.put("posttype", postchecker);

            JSONArray postname = new JSONArray();
            postname.put(arrayList);

          /*  JSONObject postarray = new JSONObject();
            postarray.put("image",jsonArray);*/

            if (jsonArray.equals(null)) {
                jsonArray.put("");
            }

            jobj_row.put("postfilename", jsonArray);

            String first = editText.getText().toString();

            StringEscapeUtils.escapeJava(first);

            String two = StringEscapeUtils.escapeSql(first);

            Log.e("TWOOOOO", two);

            jobj_row.put("caption", editText.getText().toString());
            jobj_row.put("ipaadr", ipaddress);

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("adduserpost", jarray_loginuser);
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

    private String getRealPathFromURI(Uri contentUri) {

        String[] proj = {MediaStore.Video.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    class VideoCompressor extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            Log.d(TAG, "Start video compression");
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Log.e("Compress", tempFile.getPath());
            return MediaController.getInstance().convertVideo(tempFile.getPath());
        }

        @Override
        protected void onPostExecute(Boolean compressed) {
            super.onPostExecute(compressed);
            progressBar.setVisibility(View.GONE);
            if (compressed) {
                Log.d(TAG, "Compression successfully!");
                Setvideo(MediaController.load);
                Bitmap bmThumbnail;
                // MICRO_KIND: 96 x 96 thumbnail
                bmThumbnail = ThumbnailUtils.createVideoThumbnail(MediaController.load, MediaStore.Images.Thumbnails.MINI_KIND);
                Uri uri = getImageUri(getApplicationContext(), bmThumbnail);
                try {
                    videothumbnailpath = getPath(uri);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                File source = new File(videothumbnailpath);
                File folder = new File(Environment.getExternalStorageDirectory().toString() + "/holela/videothumb");
                folder.mkdirs();
                //Save the path as a string value
                String extStorageDirectory = folder.toString();
                //Create New file and name it Image2.PNG

                File file = new File(extStorageDirectory, MediaController.name + ".jpg");
                String pathh = file.getAbsolutePath();
                File f = new File(pathh);
                if (!f.exists()) {
                    try {
                        f.createNewFile();
                        copyFile(source, f);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                //Drawable d = new BitmapDrawable(getResources(), myBitmap);
                copythumb = f.getAbsolutePath();

            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    private void uploadvideothumb(String filePath) {

        CannedAccessControlList cannedAccessControlList;
        cannedAccessControlList = CannedAccessControlList.PublicRead;

        if (filePath == null) {
            Toast.makeText(this, "Could not find the filepath of the selected file",
                    Toast.LENGTH_LONG).show();
            return;
        }
        File file = new File(filePath);


        getpostone = file.getName();
//        Toast.makeText(getApplicationContext(),file.getName(), Toast.LENGTH_LONG).show();
        Log.e("Url", "" + pref_master.getUserurl());
        TransferObserver observer = transferUtility.upload(pref_master.getUserurl() + "/post/videos/thumb", file.getName(), file, new ObjectMetadata());

        /*
         * Note that usually we set the transfer listener after initializing the
         * transfer. However it isn't required in this sample app. The flow is
         * click upload button -> start an activity for image selection
         * startActivityForResult -> onActivityResult -> beginUpload -> onResume
         * -> set listeners to in progress transfers.
         */
        // observer.setTransferListener(new UploadListener());


    }

    private void handleSendImage(Intent intent) {
        // Get the image URI from intent
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        // When image URI is not null
        if (imageUri != null) {
            // Update UI to reflect image being shared
            uri = imageUri;

            try {
                path = getPath(uri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            File source = new File(path);

            File folder = new File(Environment.getExternalStorageDirectory().toString() + "/holela/GalleryImage");
            folder.mkdirs();
            //Save the path as a string value
            String extStorageDirectory = folder.toString();
            //Create New file and name it Image2.PNG
            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();

            File file = new File(extStorageDirectory, ts + ".jpg");
            String pathh = file.getAbsolutePath();


            File f = new File(pathh);
            if (!f.exists()) {
                try {
                    f.createNewFile();
                    copyFile(source, f);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            //Drawable d = new BitmapDrawable(getResources(), myBitmap);
            Bitmap myBitmap = decodeSampledBitmapFromFile(f.getAbsolutePath(), 1000, 700);

            try {
                myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(f.getAbsolutePath()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            copyimagepath = f.getAbsolutePath();


            SetImage(myBitmap, copyimagepath);


        } else {
            Toast.makeText(this, "Error occured, URI is invalid", Toast.LENGTH_LONG).show();
        }
    }

    void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);

        if (imageUris != null) {
            // Update UI to reflect multiple images being sharedString completeString = new String();
            String completeString = new String();
            for (Uri uri : imageUris) {

                Log.e("Imagesss", "" + uri);

                try {
                    path = getPath(uri);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                File source = new File(path);

                File folder = new File(Environment.getExternalStorageDirectory().toString() + "/holela/GalleryImage");
                folder.mkdirs();
                //Save the path as a string value
                String extStorageDirectory = folder.toString();
                //Create New file and name it Image2.PNG
                Long tsLong = System.currentTimeMillis() / 1000;
                String ts = tsLong.toString();

                File file = new File(extStorageDirectory, ts + ".jpg");
                String pathh = file.getAbsolutePath();


                File f = new File(pathh);
                if (!f.exists()) {
                    try {
                        f.createNewFile();
                        copyFile(source, f);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                //Drawable d = new BitmapDrawable(getResources(), myBitmap);
                Bitmap myBitmap = decodeSampledBitmapFromFile(f.getAbsolutePath(), 1000, 700);

                try {
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(f.getAbsolutePath()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                copyimagepath = f.getAbsolutePath();


                SetImage(myBitmap, copyimagepath);


            }



        }


    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}
