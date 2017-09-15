package com.holela.Models;

import java.util.ArrayList;

/**
 * Created by admin on 6/10/2017.
 */

public class RepostModel {

    String userid,posttype,postfilename,caption;

    private ArrayList<RepostImagemodel> imageModels;


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPosttype() {
        return posttype;
    }

    public void setPosttype(String posttype) {
        this.posttype = posttype;
    }

    public String getPostfilename() {
        return postfilename;
    }

    public void setPostfilename(String postfilename) {
        this.postfilename = postfilename;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public ArrayList<RepostImagemodel> getImageModels() {
        return imageModels;
    }

    public void setImageModels(ArrayList<RepostImagemodel> imageModels) {
        this.imageModels = imageModels;
    }
}
