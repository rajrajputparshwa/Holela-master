package com.holela.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Pref_Master {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private String loginFlag = "loginflag";
    private String d_login_flag = "0";

    private String str_user_id = "uid";
    private String user_id = "0";

    private String str_other_user_id = "uotherid";
    private String other_user_id = "0";

    private String str_user_url = "u_url";
    private String user_url = "0";

    private String str_user_fullname = "u_fullname";
    private String user_fullname = "0";

    private String str_user_mobile = "u_mobile";
    private String user_mobile = "0";

    private String str_user_email = "u_email";
    private String user_email = "0";


    private String str_user_posttime = "time";
    private String str_posttime = "0";


    private String str_login_type = "type";
    private String login_type = "0";

    private String str_private = "private";
    private int publicc = 0;

    private String str_Status_Sp = "sp_status";
    private String StatsuSp = "0";

    private String stre_spiid = "sp_id";
    private String sp_id = "0";

    private String str_language = "language";
    private int language = 3;
    private String str_image = "image";
    private String image = "jp";

    private String fb_str_image = "fb_image";
    private String fb_image = "jp";


    public String getStr_user_BIO() {
        return pref.getString(str_user_BIO, user_BIO);
    }


    public void setStr_user_BIO(String bio) {
        editor = pref.edit();
        editor.putString(str_user_BIO, bio);
        editor.apply();

    }

    private String str_user_BIO = "aboutme";
    private String user_BIO = "0";




    // 0 = English , 1 = Arabic , 3 = first
    Context context;

    public Pref_Master(Context context) {
        this.context = context;
        pref = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public String getUID() {
        return pref.getString(str_user_id, user_id);
    }

    public String getOtheruserid() {
        return pref.getString(str_other_user_id, other_user_id);
    }

    public String getUserurl() {
        return pref.getString(str_user_url, user_url);
    }

    public String getFullname() {
        return pref.getString(str_user_fullname, user_url);
    }


    public String getMobile() {
        return pref.getString(str_user_mobile, user_url);
    }


    public String getEmail() {
        return pref.getString(str_user_email, user_url);
    }

    public String getImage() {
        return pref.getString(str_image, user_url);
    }

    public String getFbImage() {
        return pref.getString(fb_str_image, user_url);
    }

    public int getLanguage() {
        return pref.getInt(str_language, language);
    }

    public int getPrivate() {
        return pref.getInt(str_private, publicc);
    }


    public String getStr_login_type() {
        return pref.getString(str_login_type, login_type);
    }


    public String getStr_user_posttime() {
        return pref.getString(str_user_posttime, str_posttime);
    }

    public void setStr_user_posttime(String name) {
        editor = pref.edit();
        editor.putString(str_user_posttime,name);
        editor.apply();
    }




    public void setLanguage(int name) {
        editor = pref.edit();
        editor.putInt(str_language, name);
        editor.apply();
    }

    public void setPrivate(int name) {
        editor = pref.edit();
        editor.putInt(str_private, name);
        editor.apply();
    }


    public void setLogin_Flag(String name) {
        editor = pref.edit();
        editor.putString(loginFlag, name);
        editor.apply();
    }

    public void set_Status_Sp(String name) {
        editor = pref.edit();
        editor.putString(str_Status_Sp, name);
        editor.apply();
    }

    public void setUID(String name) {
        editor = pref.edit();
        editor.putString(str_user_id, name);
        editor.apply();
    }

    public void setOtherUserid(String name) {
        editor = pref.edit();
        editor.putString(str_other_user_id, name);
        editor.apply();
    }

    public void setUserURl(String name) {
        editor = pref.edit();
        editor.putString(str_user_url, name);
        editor.apply();
    }


    public void setSP_ID(String name) {
        editor = pref.edit();
        editor.putString(stre_spiid, name);
        editor.apply();
    }

    public void setLogin_type(String name) {
        editor = pref.edit();
        editor.putString(str_login_type, name);
        editor.apply();
    }

    public void setImage(String name) {
        editor = pref.edit();
        editor.putString(str_image, name);
        editor.apply();
    }

    public void setFbImage(String name) {
        editor = pref.edit();
        editor.putString(fb_str_image, name);
        editor.apply();
    }

    public void setFullname(String name) {
        editor = pref.edit();
        editor.putString(str_user_fullname, name);
        editor.apply();
    }


    public void setMobile(String name) {
        editor = pref.edit();
        editor.putString(str_user_mobile, name);
        editor.apply();
    }


    public void setEmail(String name) {
        editor = pref.edit();
        editor.putString(str_user_email, name);
        editor.apply();
    }




    public void clear_pref() {
        pref.edit().clear().apply();
    }
}
