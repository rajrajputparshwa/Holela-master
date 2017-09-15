package com.holela.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import static com.holela.Utils.Config.isValidEmailAddress;

public class ForgotPasswordUpdate extends AppCompatActivity {

    Edittext_open_semi_bold newpassword, confirmnewpassword;
    Button reset;
    String getforgototp, getforgotemail;

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
        setContentView(R.layout.activity_forgot_password_update);

        newpassword = (Edittext_open_semi_bold) findViewById(R.id.forgotnewppassword);
        confirmnewpassword = (Edittext_open_semi_bold) findViewById(R.id.forgotnewppasswordagain);
        reset = (Button) findViewById(R.id.reset);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            getforgototp = extras.getString("forotp");
            getforgotemail = extras.getString("foremail");
            // and get whatever type user account id is
        }


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        if (newpassword.getText().toString().trim().length() == 0) {
            DialogBox.setPOP(ForgotPasswordUpdate.this, getString(R.string.EnterAlldata));
        } else if (newpassword.getText().toString().trim().length() < 6) {
            DialogBox.setPOP(ForgotPasswordUpdate.this, getString(R.string.Entersix));
        } else if (confirmnewpassword.getText().toString().trim().length() == 0) {
            DialogBox.setPOP(ForgotPasswordUpdate.this, getString(R.string.EnterAlldata));
        } else if (confirmnewpassword.getText().toString().trim().length() < 6) {
            DialogBox.setPOP(ForgotPasswordUpdate.this, getString(R.string.Entersix));
        } else if (!newpassword.getText().toString().equals(confirmnewpassword.getText().toString())) {
            DialogBox.setConfirm(ForgotPasswordUpdate.this, GO_None, getString(R.string.ValidPassword));
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

            jobj_row.put("useremail", getforgotemail);
            jobj_row.put("process", "update");
            jobj_row.put("newpass", newpassword.getText().toString());

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
                Log.e("forgotdone_Response", " : " + response);
                ///     obj_dialog.dismiss();

                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    if (res_flag.equals("200")) {
                        Intent ii = new Intent(getApplicationContext(), Signin_Activity.class);
                        startActivity(ii);
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
}
