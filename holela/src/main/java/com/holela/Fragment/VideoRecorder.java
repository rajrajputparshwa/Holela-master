package com.holela.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.*;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.BuildConfig;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.holela.Activity.CameraViewpager;
import com.holela.Activity.Camera_Permission;
import com.holela.Activity.Eye;
import com.holela.Activity.MainActivity;
import com.holela.Activity.RecordVideo;
import com.holela.Activity.VideoPreview;
import com.holela.Controller.DialogBox;
import com.holela.R;
import com.holela.Utils.Config;
import com.holela.Utils.Connection;
import com.holela.Utils.MediaControllerVideo;
import com.holela.Utils.Pref_Master;
import com.holela.Utils.Util;
import com.holela.file.FileUtils;
import com.holela.video.MediaController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
import static com.holela.Activity.UploadActivity.isDownloadsDocument;
import static com.holela.Activity.UploadActivity.isExternalStorageDocument;
import static com.holela.Activity.UploadActivity.isMediaDocument;

public class VideoRecorder extends Activity {

    int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 03;
    Uri fileUri, uri;
    int video = 0;
    JSONArray jsonArray = new JSONArray();
    public static String path;
    private static final String TAG = "VideoRecorder";
    TextView cancel, postvideo;
    File tempFile;
    String videopostapi="";
    VideoView videoView;
    private TransferUtility transferUtility;
    String copythumb;
    Pref_Master pref_master;
    ProgressBar progressBar;
    ProgressBar progressBar3;
    String uripath;
    TextView status;
    LinearLayout progresslinerlayout;
    String videothumbnailpath;
    String myvideo;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {


            // Video captured and saved to fileUri specified in the Intent

            File source = new File(path);

            File folder = new File(Environment.getExternalStorageDirectory().toString() + "/holela/Temp");
            folder.mkdirs();
            //Save the path as a string value
            String extStorageDirectory = folder.toString();
            //Create New file and name it Image2.PNG
            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();

            File file = new File(extStorageDirectory, ts + ".jpg");
            String pathh = file.getAbsolutePath();


            tempFile = new File(pathh);
            if (!tempFile.exists()) {
                try {
                    tempFile.createNewFile();
                    copyFile(source, tempFile);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }


            new VideoCompressor().execute();


        } else if (resultCode == RESULT_CANCELED) {

            // User cancelled the video capture
            Intent i = new Intent(getApplicationContext(), CameraViewpager.class);
            startActivity(i);
            Toast.makeText(getApplicationContext(), "User cancelled the video capture.", Toast.LENGTH_LONG).show();

        } else {

            // Video capture failed, advise user
            Toast.makeText(getApplicationContext(), "Video capture failed.",
                    Toast.LENGTH_LONG).show();

            Intent i = new Intent(getApplicationContext(), CameraViewpager.class);
            startActivity(i);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), CameraViewpager.class);
        startActivity(i);
        deleteTempFile();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteTempFile();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_video_recorder);


        cancel = (TextView) findViewById(R.id.cancel);
        postvideo = (TextView) findViewById(R.id.postee);
        progressBar = (ProgressBar) findViewById(R.id.loading);
        videoView = (VideoView) findViewById(R.id.postvideo);
        pref_master = new Pref_Master(getApplicationContext());
        progressBar3 = (ProgressBar) findViewById(R.id.progressBar3);
        status = (TextView) findViewById(R.id.statusssss);
        progresslinerlayout = (LinearLayout) findViewById(R.id.progresslinerlayout);
        transferUtility = Util.getTransferUtility(this);
        postvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videopostapi.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Select Video", Toast.LENGTH_LONG).show();
                } else {
                    beginUpload(videopostapi);
                }
            }
        });


        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        // create a file to save the video
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

        // set the image file name
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // set the video image quality to high
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        // start the Video Capture Intent
        startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
    }

    private Uri getOutputMediaFileUri(int type) {

          return  FileProvider.getUriForFile(VideoRecorder.this, "com.holela.fileprovider",
                getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
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
            path = mediaFile.getAbsolutePath();

        } else {
            return null;
        }

        return mediaFile;
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
            return MediaControllerVideo.getInstance().convertVideo(tempFile.getPath());
        }

        @Override
        protected void onPostExecute(Boolean compressed) {
            super.onPostExecute(compressed);
            progressBar.setVisibility(View.GONE);
            if (compressed) {
                Log.d(TAG, "Compression successfully!");
                Setvideo(MediaControllerVideo.load);
                Bitmap bmThumbnail;
                // MICRO_KIND: 96 x 96 thumbnail
                bmThumbnail = ThumbnailUtils.createVideoThumbnail(MediaControllerVideo.load, MediaStore.Images.Thumbnails.MINI_KIND);
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

                File file = new File(extStorageDirectory, MediaControllerVideo.name + ".jpg");
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


    private void Setvideo(String path) {


        videoView.setVisibility(View.VISIBLE);

        videoView.setVideoPath(path);

        videoView.start();

        videopostapi = path;
    }

    private void deleteTempFile() {
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


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

    private void UploadPost() {


        String url = Config.baseurl + Config.poststory;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("userid", pref_master.getUID());
            jobj_row.put("imagename", videopostapi.substring(videopostapi.lastIndexOf("/") + 1));
            jobj_row.put("filetype", "2");

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
                        DialogBox.setPOP(getApplicationContext(), jobj.getString("msg").toString());
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

        Connection.postconnection(url, params, Config.getHeaderParam(getApplicationContext()), getApplicationContext(), lis_res, lis_error);

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

        final TransferObserver observer = transferUtility.upload(pref_master.getUserurl() + "/story/videos", file.getName(), file, new ObjectMetadata());

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

                    Toast.makeText(VideoRecorder.this, "File Upload Complete", Toast.LENGTH_SHORT).show();
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
                percentage = Double.parseDouble(new DecimalFormat("##.#").format(percentage));

                progressBar3.setProgress((int) percentage);
                status.setText(percentage + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                Toast.makeText(VideoRecorder.this, "" + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


}

