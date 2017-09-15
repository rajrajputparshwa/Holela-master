package com.holela.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.holela.Controller.DialogBox;
import com.holela.Controller.Textview_open_regular;
import com.holela.R;
import com.holela.Utils.Config;
import com.holela.Utils.Connection;
import com.holela.Utils.Pref_Master;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    RelativeLayout rr_signup_email;
    Textview_open_regular signin;
    LoginButton loginButton;
    CallbackManager callbackManager;
    RelativeLayout ll_fb;
    String email, name, gender, rid, device_id, ipaddress;
    Pref_Master pref;
    public static String facebookid;
    String image;
    User user;

    @Override
    public void onBackPressed() {


        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    protected void onResume() {
        super.onResume();


        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setReadPermissions("public_profile", "email", "user_friends");


        ll_fb = (RelativeLayout) findViewById(R.id.ll_fb);
        ll_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //progressDialog.show();

                loginButton.performClick();

                loginButton.setPressed(true);

                loginButton.invalidate();

                loginButton.registerCallback(callbackManager, mCallBack);

                loginButton.setPressed(false);

                loginButton.invalidate();

            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            finish();
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signin = (Textview_open_regular) findViewById(R.id.textsignin);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        ll_fb = (RelativeLayout) findViewById(R.id.ll_fb);
        pref = new Pref_Master(getApplicationContext());
        loginButton.setReadPermissions("public_profile", "email", "user_friends");
        device_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        rid = FirebaseInstanceId.getInstance().getToken();
        getAddreess();

        signin.setText(Html.fromHtml(getString(R.string.Sign_in)));


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.holela",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Signin_Activity.class);
                startActivity(i);
            }
        });


        rr_signup_email = (RelativeLayout) findViewById(R.id.rr_signup_email);
        rr_signup_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(i);
            }
        });
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


    private void SubmitInfo() {
        String url = Config.baseurl + Config.loginuser;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("useremail", email);
            jobj_row.put("userpass", "");
            jobj_row.put("devicetype", "2");
            jobj_row.put("devicetoken", rid);
            jobj_row.put("logintype", "2");
            jobj_row.put("socialuniqueid", facebookid);
            jobj_row.put("ipaddress", ipaddress);


            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);


            Log.e("USersendData", "" + jarray_loginuser);


            jobj_loginuser.put("loginuser", jarray_loginuser);
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

                    Log.e("Response", res_flag);

                    if (res_flag.equals("200")) {
                        JSONArray jarray = new JSONArray(jobj.getString("data"));

                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jobj1 = jarray.getJSONObject(i);

                            String userid = jobj1.getString("userid");
                            String urll = jobj1.getString("url");
                            String fullname = jobj1.getString("fullname");
                            String mobile = jobj1.getString("mobile");
                            String email = jobj1.getString("email");
                            String profileiamge = jobj1.getString("profileiamge");

                            Log.e("Myid", "" + userid);

                            pref.setUID(userid);
                            pref.setUserURl(urll);

                            Intent ii = new Intent(getApplicationContext(), MainActivity.class);
                            ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(ii);
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


    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {


            // App code
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {

                            Log.e("response: ", response + "");
                            try {
                                user = new User();
                                //user.pic=object.getString("public_profile").toString();

                                facebookid = object.getString("id").toString();
                                //"https://graph.facebook.com/
                                // Log.e("Profile", "https://graph.facebook.com/" + object.getString("id").toString() + object.getString("/picture"));
                                URL image_value = new URL("http://graph.facebook.com/" + facebookid + "/picture?type=large");

                                image = (String.valueOf(image_value));
                                pref.setFbImage(image);
                                Log.e("Image", "" + image);
                                Log.e("Email", object.getString("email").toString());
                                email = object.getString("email").toString();
                                name = object.getString("name").toString();
                                gender = object.getString("gender").toString();


                                pref.setLogin_Flag("login");
                                pref.setFullname(name);
                                pref.setEmail(email);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //Toast.makeText(Login.this, "welcome " + user.name, Toast.LENGTH_LONG).show();

                            SubmitInfo();

                        }

                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday,picture.type(large)");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };


}
