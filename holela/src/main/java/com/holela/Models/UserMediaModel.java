package com.holela.Models;

import java.util.ArrayList;

/**
 * Created by admin on 6/9/2017.
 */

public class UserMediaModel {

    String postid,userid,posttype,filename,limit,caption,postdatetime;
    private ArrayList<UserMediaImageModel> imageModels;

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getPostdatetime() {
        return postdatetime;
    }

    public void setPostdatetime(String postdatetime) {
        this.postdatetime = postdatetime;
    }

    public ArrayList<UserMediaImageModel> getImageModels() {
        return imageModels;
    }

    public void setImageModels(ArrayList<UserMediaImageModel> imageModels) {
        this.imageModels = imageModels;
    }
}
