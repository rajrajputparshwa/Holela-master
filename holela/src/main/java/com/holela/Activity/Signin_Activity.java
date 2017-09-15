package com.holela.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.holela.Controller.DialogBox;
import com.holela.Controller.Edittext_open_semi_bold;
import com.holela.Controller.Textview_open_semi_bold;
import com.holela.R;
import com.holela.Utils.Config;

import static com.holela.Utils.Config.isValidEmailAddress;

import com.holela.Utils.Connection;
import com.holela.Utils.Pref_Master;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class Signin_Activity extends AppCompatActivity {

    Textview_open_semi_bold Login, Forgot_Text;
    Edittext_open_semi_bold Username, Password;
    String getpassword, getusername;
    Context context = this;
    ProgressDialog pd;
    String device_id, rid, ipaddress;
    Pref_Master pref;
    String logout = "";


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_);


        Username = (Edittext_open_semi_bold) findViewById(R.id.signusername);
        Password = (Edittext_open_semi_bold) findViewById(R.id.sign_password);
        pref = new Pref_Master(context);

        device_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        rid = FirebaseInstanceId.getInstance().getToken();

        getAddreess();

        Login = (Textview_open_semi_bold) findViewById(R.id.Login);
        Forgot_Text = (Textview_open_semi_bold) findViewById(R.id.forgot_text);


        Forgot_Text.setText(Html.fromHtml(getString(R.string.Forgot_Password)));

        Forgot_Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ForgotPasswordEmail.class);
                startActivity(i);
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SendData();

            }
        });
    }


    Runnable Sign_In = new Runnable() {
        @Override
        public void run() {

        }
    };


    private void SendData() {
        if (Username.getText().toString().trim().length()==0) {
            DialogBox.setConfirm(context, Sign_In, getString(R.string.EnterUserName));
        } else if (Password.getText().toString().trim().length() == 0) {
            DialogBox.setConfirm(context, Sign_In, getString(R.string.ValidPassword));
        } else {

            if (Connection.isconnection(context)) {
                //      Send_OTP();
                SubmitInfo();
            } else {
                Toast.makeText(context, "Internet not available !", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void clear() {
        Username.setText("");
        Password.setText("");
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


        final ProgressDialog progressDialog = new ProgressDialog(context , R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("useremail", Username.getText().toString());
            jobj_row.put("userpass", Password.getText().toString());
            jobj_row.put("devicetype", "2");
            jobj_row.put("devicetoken", rid);
            jobj_row.put("logintype", "1");
            jobj_row.put("socialuniqueid", Username.getText().toString());
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

                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

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

                            pref.setLogin_Flag("login");
                            pref.setUID(userid);
                            pref.setUserURl(urll);
                            pref.setFullname(fullname);
                            pref.setMobile(mobile);
                            pref.setEmail(email);
                            pref.setImage(profileiamge);


                            startActivity(new Intent(Signin_Activity.this, MainActivity.class));
                            clear();
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


}
