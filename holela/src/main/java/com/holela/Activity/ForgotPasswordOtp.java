package com.holela.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.holela.Controller.DialogBox;
import com.holela.Controller.Edittext_open_semi_bold;
import com.holela.Controller.Textview_open_semi_bold;
import com.holela.R;
import com.holela.Utils.Config;
import com.holela.Utils.Connection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordOtp extends AppCompatActivity {

    Edittext_open_semi_bold edotp;
    Textview_open_semi_bold resent;
    String forgototp;
    String forgotemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_otp);

        edotp = (Edittext_open_semi_bold) findViewById(R.id.forgotedotp);
        resent = (Textview_open_semi_bold) findViewById(R.id.resend);
        resent.setText(Html.fromHtml(getString(R.string.resend)));

        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            forgototp = extras.getString("forgototp");
            forgotemail = extras.getString("forgotemail");
            // and get whatever type user account id is
        }

        edotp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() == 4) {
                    if (charSequence.toString().equals(forgototp)) {
                        Log.e("Otp", "Verification Successfull...");

                        Intent ii = new Intent(getApplicationContext(),ForgotPasswordUpdate.class);
                        ii.putExtra("forotp",forgototp);
                        ii.putExtra("foremail",forgotemail);
                        startActivity(ii);


                    } else {
                        edotp.setError("Please Enter Correct OTP");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        resent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendInfo();
            }
        });
    }


    private void SendInfo() {

        String url = Config.baseurl + Config.forgotpassword;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("useremail", forgotemail);
            jobj_row.put("process", "check");
            jobj_row.put("newpass", "");

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("forgotpassword", jarray_loginuser);
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
                Log.e("forgot_Response", " : " + response);
                ///     obj_dialog.dismiss();

                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    if (res_flag.equals("200")) {
                        JSONArray jarray = new JSONArray(jobj.getString("data"));

                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jobj1 = jarray.getJSONObject(i);

                            forgototp = jobj1.getString("otp");
                            String email = jobj1.getString("email");
                            DialogBox.setPOP(ForgotPasswordOtp.this, jobj.getString("msg").toString());

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

}
