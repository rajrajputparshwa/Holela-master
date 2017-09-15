package com.holela.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.holela.Activity.Tabs;
import com.holela.Controller.DialogBox;
import com.holela.Controller.Edittext_open_semi_bold;
import com.holela.R;
import com.holela.Utils.Config;
import com.holela.Utils.Connection;
import com.holela.Utils.Pref_Master;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Change_PassWord extends Fragment {

    Edittext_open_semi_bold newpassword, newpasswordagain;
    Pref_Master pref_master;
    com.holela.Controller.Edittext_open_semi_bold currentpassword;
    ImageView back, save;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_change__pass_word, container, false);


        currentpassword = (com.holela.Controller.Edittext_open_semi_bold) v.findViewById(R.id.currentpassword);
        newpassword = (Edittext_open_semi_bold) v.findViewById(R.id.newpassword);
        newpasswordagain = (Edittext_open_semi_bold) v.findViewById(R.id.newpasswordagain);
        back = (ImageView) v.findViewById(R.id.back);
        save = (ImageView) v.findViewById(R.id.save);


        pref_master = new Pref_Master(getActivity());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), Tabs.class);
                intent.putExtra("fragmentcode", Config.Fragment_ID.Setting);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentpassword.getText().toString().trim().length() == 0) {
                    currentpassword.setError("Enter Current Password");
                } else if (newpassword.getText().toString().trim().length() == 0) {
                    newpassword.setError("Enter New Password");
                } else if (newpasswordagain.getText().toString().trim().length() == 0) {
                    newpasswordagain.setError("Enter Confirm Password");
                } else if (!newpassword.getText().toString().equals(newpasswordagain.getText().toString())) {
                    DialogBox.setPOP(getActivity(), "Incorrect Confirm Password");
                } else {

                    if (Connection.isconnection(getActivity())) {
                        ChangePassword();
                    } else {
                        Toast.makeText(getActivity(), "Internet not available !", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });

        return v;

    }


    private void ChangePassword() {

        String url = Config.baseurl + Config.changepassword;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("oldpass", currentpassword.getText().toString());
            jobj_row.put("newpass", newpassword.getText().toString());
            jobj_row.put("userid", pref_master.getUID());

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("changepassword", jarray_loginuser);
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
                            DialogBox.setChangepop(getActivity(), getActivity(), jobj.getString("msg").toString());

                        }

                    } else {
                        DialogBox.setChangepop(getActivity(), getActivity(), jobj.getString("msg").toString());
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

        Connection.postconnection(url, params, Config.getHeaderParam(getActivity()), getActivity(), lis_res, lis_error);

    }


}
