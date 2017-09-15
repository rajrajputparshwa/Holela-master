package com.holela.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.holela.Activity.MainActivity;
import com.holela.Adapter.SearchAdapter;
import com.holela.Controller.DialogBox;
import com.holela.Controller.Edittext_open_semi_bold;
import com.holela.Models.SearchMOdel;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class Searchview extends Fragment {

    Edittext_open_semi_bold searchedittext;
    ArrayList<SearchMOdel> search_list = new ArrayList<>();
    RecyclerView recyclerView;
    SearchAdapter searchAdapter;
    Pref_Master pref_master;
    Context context;
    ImageView back;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_searchview, container, false);

        context = getActivity();
        searchedittext = (Edittext_open_semi_bold) view.findViewById(R.id.searchedittext);
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerrr);
        back = (ImageView) view.findViewById(R.id.back);
        pref_master = new Pref_Master(context);


        searchedittext.requestFocus();
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchedittext.getWindowToken(), 0);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
            }
        });


        LinearLayoutManager postmanger = new LinearLayoutManager(getActivity());
        postmanger.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(postmanger);


        searchedittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Sendsearch(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return view;
    }


    private void Sendsearch(CharSequence charSequence) {

        String url = Config.baseurl + Config.searchuser;
        JSONObject jobj_loginuser = new JSONObject();
        //  obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("searchkey", charSequence);
            jobj_row.put("myid", pref_master.getUID());

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("searchuser", jarray_loginuser);
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
                        search_list.clear();

                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jobj1 = jarray.getJSONObject(i);
                            SearchMOdel searchMOdel = new SearchMOdel();
                            searchMOdel.setUserid(jobj1.getString("userid"));
                            searchMOdel.setUsernamename(jobj1.getString("fullname"));
                            searchMOdel.setImage(jobj1.getString("profilepic"));
                            search_list.add(searchMOdel);
                        }

                    } else {
                        DialogBox.setPOP(getActivity(), jobj.getString("msg").toString());
                    }

                    searchAdapter = new SearchAdapter(getActivity(), search_list, getFragmentManager());
                    recyclerView.setAdapter(searchAdapter);

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
