package com.holela.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

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

public class Otp_Activity extends AppCompatActivity {

    Textview_open_semi_bold Next;
    Edittext_open_semi_bold edotp;
    String getcheckmail,otp;
    Textview_open_semi_bold resent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_);

        if (getIntent().getExtras() != null) {
            otp = getIntent().getExtras().getString("sendotp");
            getcheckmail=getIntent().getExtras().getString("getmail");
        }


      //  Next = (Textview_open_semi_bold) findViewById(R.id.next_from_otp);
        edotp= (Edittext_open_semi_bold) findViewById(R.id.edotp);
        resent = (Textview_open_semi_bold) findViewById(R.id.resend);
        resent.setText(Html.fromHtml(getString(R.string.resend)));

        edotp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() == 4) {
                    if (charSequence.toString().equals(otp)) {
                        Log.e("Otp", "Verification Successfull...");

                        Intent intent = new Intent(getApplicationContext(),SignUp.class);
                        intent.putExtra("otp",otp);
                        intent.putExtra("email",getcheckmail);
                        startActivity(intent);

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
                Send_OTP();
            }
        });

/*
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });*/
    }



    private void Send_OTP() {

        String url = Config.baseurl + Config.sendotptouser;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("email", getcheckmail);

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("sendotptouser", jarray_loginuser);
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

                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jobj1 = jarray.getJSONObject(i);
                            otp = jobj1.getString("otp");
                            DialogBox.setPOP(Otp_Activity.this, getString(R.string.otpmsg).toString());
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
