package com.holela.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.holela.R;
import com.holela.Utils.Pref_Master;

import java.util.Locale;

public class Choose_language extends AppCompatActivity {

    RelativeLayout rr_english, rr_arabic;
    String languageToLoad;
    Context context = this;
    Pref_Master pref_master;

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_language);

        rr_arabic = (RelativeLayout) findViewById(R.id.rr_arabic);
        rr_english = (RelativeLayout) findViewById(R.id.rr_english);
        pref_master = new Pref_Master(context);

        rr_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                languageToLoad = "en";
                pref_master.setLanguage(0);
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
                Intent i = new Intent(Choose_language.this, LoginActivity.class);
                startActivity(i);
            }
        });

        rr_arabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                languageToLoad = "ar";
                pref_master.setLanguage(1);
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
                Intent i = new Intent(Choose_language.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }
}
