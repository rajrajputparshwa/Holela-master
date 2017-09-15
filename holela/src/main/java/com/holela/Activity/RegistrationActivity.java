package com.holela.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.holela.Utils.MakeServiceCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.holela.Utils.Config.isValidEmailAddress;

public class RegistrationActivity extends AppCompatActivity {

    Textview_open_semi_bold next, back;
    Edittext_open_semi_bold checkmail;
    LinearLayout ll_mobile;
    RelativeLayout rr_email;
    ArrayList<USerMOdel> U_Dteais = new ArrayList<USerMOdel>();
    Context context = this;
    String getcheckmail;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        ll_mobile = (LinearLayout) findViewById(R.id.ll_mobile);
        rr_email = (RelativeLayout) findViewById(R.id.rr_email);
        checkmail = (Edittext_open_semi_bold) findViewById(R.id.checkmail);
        back = (Textview_open_semi_bold) findViewById(R.id.back);

        //  txt_email = (Textview_open_semi_bold) findViewById(R.id.txt_email);
        //  txt_phone = (Textview_open_semi_bold) findViewById(R.id.txt_phone);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });


        next = (Textview_open_semi_bold) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getcheckmail = checkmail.getText().toString();
                addData();
            }
        });

     /*   txt_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_email.setTextColor(getResources().getColor(R.color.next));
                txt_phone.setTextColor(getResources().getColor(R.color.Black));
                ll_mobile.setVisibility(View.GONE);
                rr_email.setVisibility(View.VISIBLE);
            }
        });*/

   /*     txt_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_email.setTextColor(getResources().getColor(R.color.Black));
                txt_phone.setTextColor(getResources().getColor(R.color.next));
                ll_mobile.setVisibility(View.VISIBLE);
                rr_email.setVisibility(View.GONE);
            }
        });*/


    }

    Runnable Run_reg = new Runnable() {
        @Override
        public void run() {

        }
    };


    public void addData() {

        if (!isValidEmailAddress(checkmail.getText().toString())) {
            checkmail.setError(getString(R.string.Emailempty));
        } else if (checkmail.getText().toString().trim().length() == 0) {
            checkmail.setError("Enter Email id");
        } else {
            USerMOdel model_user = new USerMOdel();
            model_user.setEmail(getcheckmail);
            U_Dteais.add(model_user);
            if (Connection.isconnection(context)) {
                Send_OTP();
               new Loginget().execute();
            } else {
                Toast.makeText(context, "Internet not available !", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void Send_OTP() {

        String url = Config.baseurl + Config.sendotptouser;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("email", checkmail.getText().toString());

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
                            String otp = (jobj1.getString("otp"));
                            Log.e("otp_check", "Enter" + otp);

                            Intent intent = new Intent(getApplicationContext(), Otp_Activity.class);
                            intent.putExtra("sendotp", otp);
                            intent.putExtra("getmail", getcheckmail);
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


    private class Loginget extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(RegistrationActivity.this);
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
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


            final HashMap<String, String> param = new HashMap<>();
            param.put("data", "" + jobj_loginuser.toString().replaceAll("\\\\", ""));


            Log.e("Put", "" + param.toString());
            Log.e("Post", "" + new MakeServiceCall().getPostData(Config.baseurl + Config.sendotptouser, param, Config.getHeaderParam(context)));
            return new MakeServiceCall().getPostData(Config.baseurl + Config.sendotptouser, param, Config.getHeaderParam(context));

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("My", "" + s.toString());
            if (pd.isShowing()) {
                pd.dismiss();
            }
            try {
                JSONObject object = new JSONObject(s.toString());
                if (object.getString("status").equalsIgnoreCase("200")) {
                    JSONArray jarray = new JSONArray(object.getString("data"));

                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject jobj1 = jarray.getJSONObject(i);
                        String otp = (jobj1.getString("otp"));
                        Log.e("otp_check", "Enter" + otp);

                        Intent intent = new Intent(getApplicationContext(), Otp_Activity.class);
                        intent.putExtra("sendotp", otp);
                        intent.putExtra("getmail", getcheckmail);
                        startActivity(intent);

                    }


                } else {
                    Toast.makeText(RegistrationActivity.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
