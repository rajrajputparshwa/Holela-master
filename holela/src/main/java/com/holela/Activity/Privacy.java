package com.holela.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.holela.Controller.Textview_open_light;
import com.holela.R;
import com.holela.Utils.Config;
import com.holela.Utils.Connection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Privacy extends AppCompatActivity {

    Textview_open_light Des;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        Des = (Textview_open_light) findViewById(R.id.des);
        back = (ImageView) findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(getApplicationContext(), Tab_Profile.class);
                intent.putExtra("fragmentcode", Config.Fragment_ID.MainFragment);
                intent.putExtra("scroll", 3);
                startActivity(intent);*/

               finish();
            }
        });
        getPRivacy();

    }


    public void getPRivacy() {


        String url = Config.baseurl + Config.cmspages + "/" + "4";
        String json = "";


        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);

        Map<String, String> header = new HashMap<>();
        header.put("apikey", Config.headkey);
        header.put("username", Config.headunm);

//header.put(Config.Language, "en");
//header.put(Config.Language, pref.getLanguage());


        Response.Listener<String> lis_res = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);


                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    if (res_flag.equals("200")) {

                        JSONArray jarray = new JSONArray(jobj.getString("data"));

                        for (int i = 0; i < jarray.length(); i++) {

                            JSONObject jsonObject = jarray.getJSONObject(i);
                            Spanned htmlAsSpanned = Html.fromHtml(jsonObject.getString("content"));
                            Des.setText(htmlAsSpanned);
                        }

                    } else if (res_flag.equals("400")) {
                        /*DialogBox.setPOP(context, jobj.getString("msg").toString());*/


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        Response.ErrorListener lis_error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
// signup.setClickable(true);
                    /*obj_dialog.dismiss();*/
            }
        };
        Connection.getconnectionVolley(url, param, Config.getHeaderParam(getApplicationContext()), getApplicationContext(), lis_res, lis_error);

    }
}
