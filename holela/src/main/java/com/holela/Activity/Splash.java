package com.holela.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.holela.Controller.Textview_open_regular;
import com.holela.R;
import com.holela.Utils.Config;
import com.holela.Utils.Pref_Master;

import java.util.Locale;

public class Splash extends AppCompatActivity {

    private int SPLASH_DISPLAY_LENGTH =3000;
    Pref_Master pref;
    ImageView eye,Holela;
    Textview_open_regular Print,Tell;
    Context context = this;
    int abc = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        eye= (ImageView) findViewById(R.id.eye);
        Holela= (ImageView) findViewById(R.id.holela_icon);
        Print = (Textview_open_regular) findViewById(R.id.text1);
        Tell = (Textview_open_regular) findViewById(R.id.text2);
        pref = new Pref_Master(context);
        zoom();
        moveleft();


        if (pref.getLanguage() == 3) {
            if (Locale.getDefault().getLanguage().equals("en")) {

            } else {

            }
        } else if (pref.getLanguage() == 1) {

        } else if (pref.getLanguage() == 0) {

        } else {

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pref.getLanguage() == 3) {
                    Log.e("abc", "" + pref.getLanguage());
                    if(Locale.getDefault().getLanguage().equals("en")){
                        pref.setLanguage(0);
                        abc = (pref.getLanguage());
                    }
                    else {
                        pref.setLanguage(1);
                        abc = (pref.getLanguage());
                    }
                } else {
                    abc = (pref.getLanguage());
                    Log.e("def", "" + pref.getLanguage());
                    new Config().Change_Language(context, pref.getLanguage() == 1 ? "1" : "0");
                }





                if (pref.getUID().equals("0")) {
                    Intent intent = new Intent(Splash.this, Choose_language.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }


    public void zoom(){

        ImageView eye = (ImageView)findViewById(R.id.eyes);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        eye.startAnimation(animation1);
    }

    public void moveleft(){
        ImageView image = (ImageView)findViewById(R.id.holela_icon);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        image.startAnimation(animation1);
    }


}
