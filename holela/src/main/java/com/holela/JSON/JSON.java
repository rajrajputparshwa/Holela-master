package com.holela.JSON;

import android.util.Log;

import com.holela.Models.USerMOdel;
import com.holela.Utils.Pref_Master;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Admin on 12/19/2016.
 */

public class JSON {
    public static String add_json(ArrayList<USerMOdel> array_user, Pref_Master pref, String type) {
        JSONObject jobj_data = new JSONObject();

        JSONObject jobj_main = new JSONObject();

        try {
            switch (type) {

                case "reg":
                    for (int i = 0; i < array_user.size(); i++) {
                        USerMOdel model = array_user.get(i);
                        jobj_main.put("name", model.getName());
                        jobj_main.put("mobile", model.getMobile());
                        jobj_main.put("devicetype", model.getDevice_type());
                        jobj_main.put("deviceid", model.getDevice_id());
                        jobj_main.put("devicetoken", model.getDevice_token());
                        jobj_main.put("password", model.getPassword());
                        jobj_main.put("email", model.getEmail());
                        jobj_main.put("referral", model.getRefferelCode());
                    }
                    break;

                case "sendotptouser":
                    for (int i = 0; i < array_user.size(); i++) {
                        USerMOdel model = array_user.get(i);
                        jobj_main.put("email", model.getEmail());
                        jobj_main.put("fname", model.getName());
                        jobj_main.put("lname", model.getName());

                    }
                    break;

            }

        } catch (JSONException e)

        {
            e.printStackTrace();
        }

        Log.i("Json request", jobj_data.toString().

                replaceAll("\\\\", "")

        );

        return jobj_data.toString().

                replaceAll("\\\\", "");
    }

}
