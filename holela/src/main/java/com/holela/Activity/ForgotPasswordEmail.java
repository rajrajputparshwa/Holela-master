package com.holela.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.holela.Controller.DialogBox;
import com.holela.Controller.Edittext_open_semi_bold;
import com.holela.Controller.Textview_open_semi_bold;
import com.holela.Models.USerMOdel;
import com.holela.R;
import com.holela.Utils.Config;
import com.holela.Utils.Connection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.holela.Utils.Config.isValidEmailAddress;

public class ForgotPasswordEmail extends AppCompatActivity {


    Edittext_open_semi_bold forgotemaillpassword;
    Textview_open_semi_bold back, next;
    String getcheckemail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_email);

        forgotemaillpassword = (Edittext_open_semi_bold) findViewById(R.id.checkforgopasswordemail);
        back = (Textview_open_semi_bold) findViewById(R.id.back);
        next = (Textview_open_semi_bold) findViewById(R.id.next);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getcheckemail = forgotemaillpassword.getText().toString();
                addData();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    Runnable Run_reg = new Runnable() {
        @Override
        public void run() {

        }
    };


    public void addData() {

        if (!isValidEmailAddress(forgotemaillpassword.getText().toString())) {
            DialogBox.setConfirm(ForgotPasswordEmail.this, Run_reg, getString(R.string.ValidMail));
        } else if (forgotemaillpassword.getText().toString().trim().length() == 0) {
            forgotemaillpassword.setError("Enter Email id");
        } else {
            if (Connection.isconnection(getApplicationContext())) {
                SendInfo();
            } else {
                Toast.makeText(getApplicationContext(), "Internet not available !", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void SendInfo() {

        String url = Config.baseurl + Config.forgotpassword;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("useremail", forgotemaillpassword.getText().toString());
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

                            String otp = jobj1.getString("otp");
                            String email = jobj1.getString("email");
                            DialogBox.setForgotPOP(ForgotPasswordEmail.this, jobj.getString("msg").toString(),otp,email);

                        }

                    } else {
                        DialogBox.setPOP(ForgotPasswordEmail.this, getString(R.string.Emailforgot).toString());
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
