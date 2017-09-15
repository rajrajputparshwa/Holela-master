package com.holela.Fragment;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.holela.Activity.Tabs;
import com.holela.Controller.DialogBox;
import com.holela.Controller.Textview_open_semi_bold;
import com.holela.R;
import com.holela.Utils.Config;
import com.holela.Utils.Connection;
import com.holela.Utils.Pref_Master;
import com.holela.Utils.Util;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static com.holela.Activity.UploadActivity.isDownloadsDocument;
import static com.holela.Activity.UploadActivity.isExternalStorageDocument;
import static com.holela.Activity.UploadActivity.isMediaDocument;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfile extends Fragment {


    EditText uname, mobile, email , bio;
    Bitmap bitmap;
    ImageView profileimage, back, save;
    Uri uri;
    private int PICK_IMAGE_REQUEST = 1;
    Pref_Master pref_master;
    ProgressBar progressBar;
    TextView textprofile;
    String Image_String_one;
    private TransferUtility transferUtility;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                Setimage(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        profileimage = (ImageView) v.findViewById(R.id.profileimage);
        save = (ImageView) v.findViewById(R.id.save);

        back = (ImageView) v.findViewById(R.id.back);

        uname = (EditText) v.findViewById(R.id.profile_username);
        mobile = (EditText) v.findViewById(R.id.profilemobile);
        email = (EditText) v.findViewById(R.id.profile_email);
        bio = (EditText) v.findViewById(R.id.bio);
        textprofile = (TextView) v.findViewById(R.id.textprofile);
        progressBar = (ProgressBar) v.findViewById(R.id.loading);
        pref_master = new Pref_Master(getActivity());
        transferUtility = Util.getTransferUtility(getActivity());


        textprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Tabs.class);
                intent.putExtra("fragmentcode", Config.Fragment_ID.Setting);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendInfo();
                beginUpload(Image_String_one);


            }
        });



     /*   back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Tabs.class);
                intent.putExtra("fragmentcode", Config.Fragment_ID.Myprofile);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);
            }
        });*/

        uname.setText(pref_master.getFullname());
        mobile.setText(pref_master.getMobile());
        email.setText(pref_master.getEmail());
        bio.setText(pref_master.getStr_user_BIO());

        String pic = pref_master.getImage();
        if (pic.equals("")) {
            Picasso.with(getActivity())
                    .load(R.drawable.ic_defalult)
                    .placeholder(R.drawable.ic_defalult)
                    .into(profileimage);
        } else {
            Picasso.with(getActivity())
                    .load(pref_master.getImage())
                    .placeholder(R.drawable.ic_defalult)
                    .into(profileimage);

        }


        return v;
    }


    private String getPath(Uri uri) throws URISyntaxException {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(getActivity().getApplicationContext(), uri)) {
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
                cursor = getActivity().getContentResolver()
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


    private void Setimage(Bitmap bitmap1) {
        BitmapDrawable bdrawable = new BitmapDrawable(getActivity().getResources(), bitmap1);
        try {
            Image_String_one = getPath(uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        profileimage.setImageBitmap(bitmap1);
    }

    private void SendInfo() {

        String url = Config.baseurl + Config.edituserprofile;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();
            jobj_row.put("userid", pref_master.getUID());
            jobj_row.put("fullname", uname.getText().toString());
            jobj_row.put("mobile", mobile.getText().toString());
            jobj_row.put("email", email.getText().toString());
            jobj_row.put("aboutme", bio.getText().toString());

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("edituserprofile", jarray_loginuser);
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
                Log.e("login_Response", " : " + response);
                ///     obj_dialog.dismiss();

                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    if (res_flag.equals("200")) {
                        JSONArray jarray = new JSONArray(jobj.getString("data"));
                        setsuccess(getActivity(), jobj.getString("msg").toString());

                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jobj1 = jarray.getJSONObject(i);



                        }

                    } else {
                        DialogBox.setPOP(getActivity(), jobj.getString("msg").toString());
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

        Connection.postconnection(url, params, Config.getHeaderParam(getActivity()), getActivity(), lis_res, lis_error);

    }


    private void SendProfilePic() {

        String url = Config.baseurl + Config.edituserprofileimage;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();
            jobj_row.put("userid", pref_master.getUID());
            jobj_row.put("imagename", Image_String_one.substring(Image_String_one.lastIndexOf("/") + 1));

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("edituserprofileimage", jarray_loginuser);
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
                        DialogBox.setPOP(getActivity(), jobj.getString("msg").toString());
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

        Connection.postconnection(url, params, Config.getHeaderParam(getActivity()), getActivity(), lis_res, lis_error);

    }


    private void beginUpload(String filePath) {

        CannedAccessControlList cannedAccessControlList;
        cannedAccessControlList = CannedAccessControlList.PublicRead;

        if (filePath == null) {
            //   Toast.makeText(getActivity(), "Could not find the filepath of the selected file", Toast.LENGTH_LONG).show();
            return;
        }
        File file = new File(filePath);


//        Toast.makeText(getApplicationContext(),file.getName(), Toast.LENGTH_LONG).show();
        Log.e("Url", "" + pref_master.getUserurl());
        TransferObserver observer = transferUtility.upload(pref_master.getUserurl(), file.getName(), file, new ObjectMetadata());

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


    private class UploadListener implements TransferListener {

        @Override
        public void onStateChanged(int id, TransferState state) {
            if (state.equals(TransferState.COMPLETED)) {
                Toast.makeText(getActivity().getApplicationContext(), "Profile should be successfully updated", Toast.LENGTH_SHORT).show();
                SendInfo();
                SendProfilePic();
                progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onError(int id, Exception ex) {

        }
    }


    public static void setsuccess(final Context context, String msg) {

        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.alert, null);
        final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.setCancelable(false);

        Textview_open_semi_bold cancel = (Textview_open_semi_bold) v.findViewById(R.id.con_ok);
        Textview_open_semi_bold tv_msg = (Textview_open_semi_bold) v.findViewById(R.id.tv_msg);
        tv_msg.setText("" + msg);
        android.support.v4.app.FragmentManager manager = null;

        final android.support.v4.app.FragmentManager finalManager = manager;
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();

                Intent i = new Intent(context, Tabs.class);
                i.putExtra("fragmentcode", Config.Fragment_ID.Myprofile);
                context.startActivity(i);
                ((Activity) context).finish();
            }
        });

    }




}
