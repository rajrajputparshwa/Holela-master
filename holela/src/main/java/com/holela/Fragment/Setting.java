package com.holela.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.holela.Activity.BlockList;
import com.holela.Activity.LoginActivity;
import com.holela.Activity.MainActivity;
import com.holela.Activity.Privacy;
import com.holela.Activity.Tab_Profile;
import com.holela.Activity.Tabs;
import com.holela.Activity.Terms;
import com.holela.Controller.DialogBox;
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
public class Setting extends Fragment {

    TextView Logout, changepassword, editprofile, changelanguage, privacy, terms, blocklist;
    private int SPLASH_DISPLAY_LENGTH = 3000;
    ProgressDialog progressDialog;
    ImageView back;
    Switch aSwitch;
    Pref_Master pref_master;

    Context context;

    @Override
    public void onResume() {
        super.onResume();


        if (getView() == null) {
            return;
        }
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_BACK) {
// handle back button's click listener

                    Intent ii = new Intent(getActivity(), MainActivity.class);
                    startActivity(ii);


                }
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        context = getActivity();
        Logout = (TextView) view.findViewById(R.id.Logout);
        changepassword = (TextView) view.findViewById(R.id.changepassword);
        editprofile = (TextView) view.findViewById(R.id.editprofile);
        changelanguage = (TextView) view.findViewById(R.id.changelanguage);
        blocklist = (TextView) view.findViewById(R.id.blocklist);
        privacy = (TextView) view.findViewById(R.id.privacy);
        pref_master = new Pref_Master(getActivity());
        aSwitch = (Switch) view.findViewById(R.id.privatepublic);
        terms = (TextView) view.findViewById(R.id.terms);
        back = (ImageView) view.findViewById(R.id.back);


        Log.e("GetPRivate", "" + pref_master.getPrivate());

        if (pref_master.getPrivate() == 2) {
            aSwitch.setChecked(true);
        } else {
            aSwitch.setChecked(false);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Tab_Profile.class);
                intent.putExtra("fragmentcode", Config.Fragment_ID.MainFragment);
                intent.putExtra("scroll", 3);
                context.startActivity(intent);

            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.e("Checked", "" + b);

                if (b) {
                    Private(context, "2");
                    pref_master.setPrivate(2);
                } else {
                    Private(context, "1");
                    pref_master.setPrivate(1);
                }

            }
        });

        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Tabs.class);
                intent.putExtra("fragmentcode", Config.Fragment_ID.ChangePassword);

                getActivity().startActivity(intent);


            }
        });

        blocklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), BlockList.class);
                startActivity(i);
            }
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), Terms.class);
                startActivity(i);

            }
        });


        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), Privacy.class);
                startActivity(i);

            }
        });


        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Tabs.class);
                intent.putExtra("fragmentcode", Config.Fragment_ID.EditProfile);
                getActivity().startActivity(intent);

            }
        });


        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
                progressDialog.setMessage("Please wait ...");
                progressDialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        pref_master.clear_pref();

                    }
                }, SPLASH_DISPLAY_LENGTH);


            }
        });

        changelanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogBox.setChangeLanguage(context, pref_master);
            }
        });

        return view;
    }


    private void Private(final Context context, String privacytype) {

        String url = Config.baseurl + Config.edituserprivacy;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("userid", pref_master.getUID());
            jobj_row.put("privacytype", privacytype);
            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("edituserprivacy", jarray_loginuser);
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
                Log.e("privacy_Response", " : " + response);
                ///     obj_dialog.dismiss();

                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    if (res_flag.equals("200")) {
                        JSONArray jarray = new JSONArray(jobj.getString("data"));

                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jobj1 = jarray.getJSONObject(i);


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
