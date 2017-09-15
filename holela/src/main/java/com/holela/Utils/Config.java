package com.holela.Utils;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Config {

    public static String baseurl = "https://myholela.com/beta/api/index.php/";
    public static String image_url = "http://app.sendantech.com/test/";

    static String lang;
    public static String headunm = "parshwa";
    public static String headkey = "564565fdgdfgghffg154";
    public static String devicetype = "1";
    public static String sendotptouser = "sendotptouser";
    public static String registeruser = "registeruser";
    public static String loginuser = "loginuser";
    public static String adduserpost = "adduserpost";
    public static String getuserpost = "getuserpost";
    public static String edituserprofile = "edituserprofile";
    public static String edituserprofileimage = "edituserprofileimage";
    public static String getpostdetails = "getpostdetails";
    public static String commentpost = "commentpost";
    public static String getpostcomments = "getpostcomments";
    public static String likepost = "likepost";
    public static String searchuser = "searchuser";
    public static String getuserDetails = "getuserDetails";
    public static String addfollowing = "addfollowing";
    public static String removefollowing = "removefollowing";
    public static String getuserwall = "getuserwall";
    public static String getpostimages = "getpostimages";
    public static String checkfollow = "checkfollow";
    public static String getmylike = "getmylike";
    public static String getrepostdetails = "getrepostdetails";
    public static String copyurl = "copyurl";
    public static String repost = "repost";
    public static String getallmedia = "getallmedia";
    public static String poststory = "poststory";
    public static String followerlist = "followerlist";
    public static String followinglist = "followinglist";
    public static String storyfollowinglist = "storyfollowinglist";
    public static String getuserstory = "getuserstory";
    public static String getnotification = "getnotification";
    public static String changepassword = "changepassword";
    public static String forgotpassword = "forgotpassword";
    public static String getusersave = "getusersave";
    public static String savepost = "savepost";
    public static String deleteuserpost = "deleteuserpost";
    public static String suggestionlist = "suggestionlist";
    public static String blockuser = "blockuser";
    public static String getchatmember = "getchatmember";
    public static String getchatlist = "getchatlist";
    public static String postchat = "postchat";
    public static String getchat = "getchat";
    public static String unblockuser = "unblockuser";
    public static String deletecomment = "deletecomment";
    public static String edituserprivacy = "edituserprivacy";
    public static String cmspages = "cmspages";
    public static String blocklist = "blocklist";
    public static String blockuserpost = "blockuserpost";
    public static String postlikeuserlist = "postlikeuserlist";


    public static Map<String, String> getHeaderParam(Context context) {

        Pref_Master pref_master = new Pref_Master(context);

        Map<String, String> header = new HashMap<>();
        header.put("apikey", Config.headkey);
        header.put("username", Config.headunm);

        if (String.valueOf(pref_master.getLanguage()).equals("0")) {
            lang = "en";
        } else if (String.valueOf(pref_master.getLanguage()).equals("1")) {
            lang = "ar";
        }
        header.put("userlanguage", lang);
        return header;
    }

    public static boolean isValidEmailAddress(String emailAddress) {
        String emailRegEx;
        Pattern pattern;
        // Regex for a valid email address
        emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";
        //emailRegEx = "" + R.string.email_string;
        // Compare the regex with the email address
        pattern = Pattern.compile(emailRegEx);
        Matcher matcher = pattern.matcher(emailAddress);
        if (!matcher.find()) {
            return false;
        }
        return true;
    }

    public static String Check_Language() {
        // 0 = English , 1 = Arabic

        //new

        // 1 = English , 2 = Arabic

        return Locale.getDefault().getDisplayLanguage().equals("Arabic") ? "2" : "1";
    }

    public String BitmapToString(Bitmap image) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public void Change_Language(Context context, String lang) {
        //0 = English 1 = Arabic

        //new

        Locale locale = new Locale(lang.equals("1") ? "ar" : "en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }


    public final static class Fragment_ID {
        public final static int MainFragment = 1;
        public final static int Search = 2;
        public final static int Camera = 3;
        public final static int Notification = 4;
        public final static int Myprofile = 5;
        public final static int UserOwn = 6;
        public final static int Userother = 7;
        public final static int EditProfile = 8;
        public final static int Option = 9;
        public final static int FollowingList = 10;
        public final static int Followerslist = 11;
        public final static int Setting = 12;
        public final static int ChangePassword = 13;
        public final static int Likes = 14;

    }


}
