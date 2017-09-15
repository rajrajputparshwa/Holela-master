package com.holela.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.holela.Adapter.SearchAdapter;
import com.holela.Controller.Edittext_open_semi_bold;
import com.holela.Models.SearchMOdel;
import com.holela.R;
import com.holela.Utils.Pref_Master;

import java.util.ArrayList;

public class FollowingList extends AppCompatActivity {
    Edittext_open_semi_bold searchedittext;
    ArrayList<SearchMOdel> search_list = new ArrayList<>();
    RecyclerView recyclerView;
    SearchAdapter searchAdapter;
    Pref_Master pref_master;
    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_list2);
        pref_master = new Pref_Master(context);
    }
}
