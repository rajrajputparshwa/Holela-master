package com.holela.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.holela.Activity.ForgotPasswordOtp;
import com.holela.Activity.MainActivity;
import com.holela.Activity.PostDetail;
import com.holela.Activity.Signin_Activity;
import com.holela.Activity.Tabs;
import com.holela.Fragment.Home;
import com.holela.Fragment.OtherUser;
import com.holela.Models.CommentModel;
import com.holela.R;
import com.holela.Utils.Config;
import com.holela.Utils.Pref_Master;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by admin on 5/16/2017.
 */

public class DialogBox extends Activity {


    public static void SetShare(final Context context, final String postid, final int position) {

        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.alert_share, null);

        final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();

        Window window = alert.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.y = 100; // Here is the param to set your dialog position. Same with params.x
        alert.getWindow().setAttributes(wlp);

        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.setCancelable(false);

        Textview_open_regular cancel = (Textview_open_regular) v.findViewById(R.id.cancel);
        Textview_open_regular sharetofacebook = (Textview_open_regular) v.findViewById(R.id.sharetofacebook);
        Textview_open_regular copyurl = (Textview_open_regular) v.findViewById(R.id.copyurl);
        Textview_open_regular flag = (Textview_open_regular) v.findViewById(R.id.flag);


        flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Home.Flag(context, postid, position);
                alert.dismiss();
            }
        });

        copyurl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Home.CopyUrl(context, postid);
                alert.dismiss();
            }

        });


        sharetofacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Home.GetFacebookUrl(context, postid);
                alert.dismiss();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });


    }

    public static void SetDelete(final Context context, final String postid, final int position) {

        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.post_delete_alert, null);

        final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();

        Window window = alert.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.y = 100; // Here is the param to set your dialog position. Same with params.x
        alert.getWindow().setAttributes(wlp);

        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.setCancelable(false);

        Textview_open_regular cancel = (Textview_open_regular) v.findViewById(R.id.cancel);
        Textview_open_regular delete = (Textview_open_regular) v.findViewById(R.id.delete_post);
        final Textview_open_regular copyurl = (Textview_open_regular) v.findViewById(R.id.copyurl);
        Textview_open_regular sharee = (Textview_open_regular) v.findViewById(R.id.shareee);


        copyurl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Home.CopyUrl(context, postid);
                alert.dismiss();
            }
        });

        sharee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Home.GetPostUrl(context, postid);
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Home.Delete_Post(context, postid, position);

                alert.dismiss();

                Log.e("Delete", "Delete");

            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });


    }


    public static void SetPOstDetailDelete(final Context context, final String postid) {

        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.post_delete_alert, null);

        final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();

        final PostDetail postDetail = new PostDetail();
        Window window = alert.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.y = 100; // Here is the param to set your dialog position. Same with params.x
        alert.getWindow().setAttributes(wlp);

        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.setCancelable(false);

        Textview_open_regular cancel = (Textview_open_regular) v.findViewById(R.id.cancel);
        Textview_open_regular delete = (Textview_open_regular) v.findViewById(R.id.delete_post);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                postDetail.Delete_Post(context, postid);
                alert.dismiss();


                Log.e("Delete", "Delete");

            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });


    }


    public static void SetOtherUSer(final Context context, final String userid, final String blockerid) {

        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.otheruseralert, null);

        final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();

        Window window = alert.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.y = 100; // Here is the param to set your dialog position. Same with params.x
        alert.getWindow().setAttributes(wlp);

        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.setCancelable(false);

        Textview_open_regular cancel = (Textview_open_regular) v.findViewById(R.id.cancel);
        Textview_open_regular block = (Textview_open_regular) v.findViewById(R.id.block);

        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogBox.setBlock(context, userid, blockerid);
                alert.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });


    }


    public static void setConfirmDone(final Context context, final Runnable GO_Back, String msg) {

        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.alert, null);
        final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.setCancelable(false);

        Textview_open_semi_bold cancel = (Textview_open_semi_bold) v.findViewById(R.id.con_ok);
        Textview_open_semi_bold tv_msg = (Textview_open_semi_bold) v.findViewById(R.id.tv_msg);
        tv_msg.setText("" + msg);
        FragmentManager manager = null;

        final FragmentManager finalManager = manager;
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GO_Back.run();
                alert.dismiss();
/*
                Intent i = new Intent(context, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("fragmentcode", Config.Fragment_ID.Wallet);
                context.startActivity(i);
                ((Activity) context).finish();*/
            }
        });

    }


    public static void setConfirm(final Context context, final Runnable GO_Back, String msg) {

        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.alert, null);
        final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.setCancelable(false);

        Textview_open_semi_bold cancel = (Textview_open_semi_bold) v.findViewById(R.id.con_ok);
        Textview_open_semi_bold tv_msg = (Textview_open_semi_bold) v.findViewById(R.id.tv_msg);
        tv_msg.setText("" + msg);
        FragmentManager manager = null;

        final FragmentManager finalManager = manager;
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GO_Back.run();
                alert.dismiss();
/*
                Intent i = new Intent(context, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("fragmentcode", Config.Fragment_ID.Wallet);
                context.startActivity(i);
                ((Activity) context).finish();*/
            }
        });

    }

    public static void setResetDone(final Context context, String msg) {

        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.alert, null);
        final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.setCancelable(false);

        Textview_open_semi_bold cancel = (Textview_open_semi_bold) v.findViewById(R.id.con_ok);
        Textview_open_semi_bold tv_msg = (Textview_open_semi_bold) v.findViewById(R.id.tv_msg);
        tv_msg.setText("" + msg);
        FragmentManager manager = null;

        final FragmentManager finalManager = manager;
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();

                Intent i = new Intent(context, Signin_Activity.class);
                context.startActivity(i);
/*
                Intent i = new Intent(context, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("fragmentcode", Config.Fragment_ID.Wallet);
                context.startActivity(i);
                ((Activity) context).finish();*/
            }
        });

    }


    public static void setDeleteComment(final Context context, String msg, final String userid, final String postid, final String commentid, final int position, final ArrayList<CommentModel> arrayList) {

        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.alertdelete, null);
        final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.setCancelable(false);

        Textview_open_semi_bold ok = (Textview_open_semi_bold) v.findViewById(R.id.con_ok);
        Textview_open_semi_bold cancel = (Textview_open_semi_bold) v.findViewById(R.id.con_cancel);
        Textview_open_semi_bold tv_msg = (Textview_open_semi_bold) v.findViewById(R.id.tv_msg);
        tv_msg.setText("" + msg);
        FragmentManager manager = null;

        final FragmentManager finalManager = manager;
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostDetail.DeleteComment(context, userid, postid, commentid, position, arrayList);
                alert.dismiss();

/*
                Intent i = new Intent(context, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("fragmentcode", Config.Fragment_ID.Wallet);
                context.startActivity(i);
                ((Activity) context).finish();*/
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });

    }


    public static void setPOP(final Context context, String msg) {

        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.alert, null);
        final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.setCancelable(false);

        Textview_open_semi_bold cancel = (Textview_open_semi_bold) v.findViewById(R.id.con_ok);
        Textview_open_semi_bold tv_msg = (Textview_open_semi_bold) v.findViewById(R.id.tv_msg);
        tv_msg.setText("" + msg);
        FragmentManager manager = null;

        final FragmentManager finalManager = manager;
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
/*
                Intent i = new Intent(context, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("fragmentcode", Config.Fragment_ID.Wallet);
                context.startActivity(i);
                ((Activity) context).finish();*/
            }
        });

    }


    public static void setChangeLanguage(final Context context, final Pref_Master pref) {

        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.dialog_language, null);
        final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.setCancelable(false);

        final ArrayList<LinearLayout> Arr_ll_cat = new ArrayList<>();
        final ArrayList<ImageView> Arr_Chk_cat = new ArrayList<>();

        Arr_ll_cat.add(0, (LinearLayout) v.findViewById(R.id.ll_l_English));
        Arr_ll_cat.add(1, (LinearLayout) v.findViewById(R.id.ll_l_Arabic));

        Arr_Chk_cat.add(0, (ImageView) v.findViewById(R.id.chk_l_english));
        Arr_Chk_cat.add(1, (ImageView) v.findViewById(R.id.chk_l_arabic));

        Textview okbtn = (Textview) v.findViewById(R.id.okbtn);
        Textview cancel = (Textview) v.findViewById(R.id.cancel);

        final String[] pos_lang = {"0"};
        if (pref.getLanguage() == 1) {
            Select_Status(Arr_ll_cat, 1, Arr_Chk_cat);
            pos_lang[0] = "1";
        } else {
            Select_Status(Arr_ll_cat, 0, Arr_Chk_cat);
            pos_lang[0] = "0";
        }
        Arr_ll_cat.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Select_Status(Arr_ll_cat, 0, Arr_Chk_cat);
                pos_lang[0] = "0";
            }
        });
        Arr_ll_cat.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Select_Status(Arr_ll_cat, 1, Arr_Chk_cat);
                pos_lang[0] = "1";
            }
        });

        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String languageToLoad = "en";
                if (pos_lang[0].equals("1")) {
                    languageToLoad = "ar";
                    pref.setLanguage(1);
                } else {
                    languageToLoad = "en";
                    pref.setLanguage(0);
                }
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
                alert.dismiss();
                Intent refresh = new Intent(context, MainActivity.class);

                context.startActivity(refresh);
                ((Activity) context).finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });

    }

    public static void Select_Status(ArrayList<LinearLayout> ll, int pos, ArrayList<ImageView> Arr_Chk_cat) {
        for (int i = 0; i < ll.size(); i++) {
            Arr_Chk_cat.get(i).setImageResource((i == pos) ? R.drawable.radio_selected : R.drawable.radio_unselected);
        }

    }


    public static void setForgotPOP(final Context context, String msg, final String otp, final String email) {

        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.alert, null);
        final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.setCancelable(false);

        Textview_open_semi_bold cancel = (Textview_open_semi_bold) v.findViewById(R.id.con_ok);
        Textview_open_semi_bold tv_msg = (Textview_open_semi_bold) v.findViewById(R.id.tv_msg);
        tv_msg.setText("" + msg);
        FragmentManager manager = null;

        final FragmentManager finalManager = manager;
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert.dismiss();

                Intent i = new Intent(context, ForgotPasswordOtp.class);
                i.putExtra("forgototp", otp);
                i.putExtra("forgotemail", email);
                context.startActivity(i);

/*
                Intent i = new Intent(context, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("fragmentcode", Config.Fragment_ID.Wallet);
                context.startActivity(i);
                ((Activity) context).finish();*/
            }
        });

    }


    public static void setChangepop(final FragmentActivity fragmentActivity, final Context context, String msg) {

        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.alert, null);
        final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.setCancelable(false);

        Textview_open_semi_bold cancel = (Textview_open_semi_bold) v.findViewById(R.id.con_ok);
        Textview_open_semi_bold tv_msg = (Textview_open_semi_bold) v.findViewById(R.id.tv_msg);
        tv_msg.setText("" + msg);
        FragmentManager manager = null;

        final FragmentManager finalManager = manager;
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                fragmentActivity.finish();
/*
                Intent i = new Intent(context, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("fragmentcode", Config.Fragment_ID.Wallet);
                context.startActivity(i);
                ((Activity) context).finish();*/
            }
        });

    }


    public static void setSetting(final Context context) {

        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.fragment_setting, null);
        final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        alert.setCancelable(false);

        TextView Logout = (TextView) v.findViewById(R.id.Logout);
        final Pref_Master pref_master;
        FragmentManager manager = null;

        final FragmentManager finalManager = manager;
        pref_master = new Pref_Master(context);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref_master.clear_pref();
                Intent i = new Intent(context, Signin_Activity.class);
                context.startActivity(i);
            }
        });

    }


    public static void setRepost(final Context context, final String postid, final String postuserid, final FragmentManager manager) {

        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.repost_alert, null);
        final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.setCancelable(false);

        Textview_open_semi_bold ok = (Textview_open_semi_bold) v.findViewById(R.id.con_ok);
        Textview_open_semi_bold cancel = (Textview_open_semi_bold) v.findViewById(R.id.con_cancel);
        Textview_open_semi_bold tv_msg = (Textview_open_semi_bold) v.findViewById(R.id.tv_msg);

        final FragmentManager finalManager = manager;
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Home.Repost(context, postid, postuserid, manager);
                alert.dismiss();


/*
                Intent i = new Intent(context, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("fragmentcode", Config.Fragment_ID.Wallet);
                context.startActivity(i);
                ((Activity) context).finish();*/
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();

            }
        });

    }


    public static void setBlock(final Context context, final String userid, final String blockerid) {

        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.setblock, null);
        final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.setCancelable(false);

        Textview_open_semi_bold ok = (Textview_open_semi_bold) v.findViewById(R.id.con_ok);
        Textview_open_semi_bold cancel = (Textview_open_semi_bold) v.findViewById(R.id.con_cancel);
        Textview_open_semi_bold tv_msg = (Textview_open_semi_bold) v.findViewById(R.id.tv_msg);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "User blocked", Toast.LENGTH_LONG).show();
                OtherUser.Block(context);
                alert.dismiss();


/*
                Intent i = new Intent(context, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("fragmentcode", Config.Fragment_ID.Wallet);
                context.startActivity(i);
                ((Activity) context).finish();*/
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();

            }
        });

    }





}
