package com.holela.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.holela.Controller.DialogBox;
import com.holela.Controller.Edittext_open_semi_bold;
import com.holela.Controller.Textview_open_light;
import com.holela.Controller.Textview_open_semi_bold;
import com.holela.Models.USerMOdel;
import com.holela.R;
import com.holela.Utils.Config;
import com.holela.Utils.Connection;
import com.holela.Utils.Pref_Master;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    Edittext_open_semi_bold email, password, fullname, mobile, confirmpassword, username;
    Textview_open_semi_bold submit;
    JSONObject jsonObject;
    String getmail, getpass, getfullname, getmobile, getconfirmpassword, getusername;
    ArrayList<USerMOdel> U_Dteais = new ArrayList<USerMOdel>();
    Pref_Master pref;
    String device_id, rid;
    Textview_open_light termsandcondition;
    Spanned Text;

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        if (getIntent().getExtras() != null) {
            getmail = getIntent().getExtras().getString("email");
        }

        // email= (Edittext_open_semi_bold) findViewById(R.id.username_signup);
        password = (Edittext_open_semi_bold) findViewById(R.id.password_signup);
        fullname = (Edittext_open_semi_bold) findViewById(R.id.fullnamesignup);
        mobile = (Edittext_open_semi_bold) findViewById(R.id.mobile_signup);
        submit = (Textview_open_semi_bold) findViewById(R.id.submit_signup);
        confirmpassword = (Edittext_open_semi_bold) findViewById(R.id.confirmpassword_signup);
        username = (Edittext_open_semi_bold) findViewById(R.id.username_user);
        termsandcondition = (Textview_open_light) findViewById(R.id.termsandcondition);
        termsandcondition.setMovementMethod(LinkMovementMethod.getInstance());

        password.requestFocus();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(password.getWindowToken(), 0);


        pref = new Pref_Master(context);
        device_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        rid = FirebaseInstanceId.getInstance().getToken();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // getmail = email.getText().toString();

                getpass = password.getText().toString();
                getfullname = fullname.getText().toString();
                getmobile = mobile.getText().toString();
                getconfirmpassword = confirmpassword.getText().toString();
                getusername = username.getText().toString();
                addData();
            }
        });

    }

    Runnable GO_None = new Runnable() {
        @Override
        public void run() {

        }
    };


    public void addData() {

          if (confirmpassword.getText().toString().trim().length() == 0) {
            DialogBox.setPOP(SignUp.this, getString(R.string.EnterAlldata));
        } else if (password.getText().toString().trim().length() == 0) {
            DialogBox.setPOP(SignUp.this, getString(R.string.Blankdata));
        } else if (password.getText().toString().trim().length() < 6) {
            DialogBox.setPOP(SignUp.this, getString(R.string.Entersix));
        } else if (confirmpassword.getText().toString().trim().length() < 6) {
            DialogBox.setPOP(SignUp.this, getString(R.string.confirmpass));
        }else if (!confirmpassword.getText().toString().equals(getpass)) {
            confirmpassword.setError("Enter Valid Password");
        } else if (fullname.getText().toString().trim().length() == 0) {
            fullname.setError("Enter Fullname");
        } else if (username.getText().toString().trim().length() == 0) {
            username.setError("Enter Username");
        } else if (mobile.getText().toString().trim().length() < 10 && !(mobile.getText().toString().trim().length() == 0)) {
            mobile.setError(getString(R.string.mobileempty));
        } else {
            USerMOdel model_user = new USerMOdel();
            model_user.setName(getfullname);
            model_user.setMobile(getmobile);
            model_user.setPassword(getpass);
            model_user.setEmail(getmail);
            model_user.setUsername(getusername);
            U_Dteais.add(model_user);


            pref.setFullname(getfullname);
            pref.setMobile(getmobile);
            pref.setEmail(getmail);

            if (Connection.isconnection(context)) {
                //      Send_OTP();
                SubmitInfo();
            } else {
                Toast.makeText(context, "Internet not available !", Toast.LENGTH_LONG).show();
            }

        }

    }

    private void SubmitInfo() {

        String url = Config.baseurl + Config.registeruser;
        JSONObject jobj_loginuser = new JSONObject();


        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("useremail", getmail);
            jobj_row.put("userpass", getpass);
            jobj_row.put("fullname", getfullname);
            jobj_row.put("username", getusername);
            jobj_row.put("mobile", getmobile);
            jobj_row.put("language", "en");
            jobj_row.put("logintype", "1");
            jobj_row.put("deviceid", device_id);
            jobj_row.put("devicetype", "2");
            jobj_row.put("devicetoken", rid);

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);


            Log.e("USersendData", "" + jarray_loginuser);

            Log.e("USersendMail", "" + getmail);

            jobj_loginuser.put("registeruser", jarray_loginuser);
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

                if (progressDialog.isShowing()) {
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
                            String userurl = jobj1.getString("url");

                            pref.setUID(userid);
                            pref.setUserURl(userurl);

                            Log.e("UserId", pref.getUID());
                            Log.e("Userurl", pref.getUserurl());

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

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
