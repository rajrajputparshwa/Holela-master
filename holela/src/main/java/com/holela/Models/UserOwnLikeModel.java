package com.holela.Models;

import java.util.ArrayList;

/**
 * Created by admin on 6/9/2017.
 */

public class UserOwnLikeModel {
    String postid,userid,posttype,profileiamge,mylike,postfilename,caption,fullname,username,email,postdatetime,like,comment,share,repost,limit ;
    private ArrayList<UserOWnLikeImage> imageModels;

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

    public String getProfileiamge() {
        return profileiamge;
    }

    public void setProfileiamge(String profileiamge) {
        this.profileiamge = profileiamge;
    }

    public String getMylike() {
        return mylike;
    }

    public void setMylike(String mylike) {
        this.mylike = mylike;
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPostdatetime() {
        return postdatetime;
    }

    public void setPostdatetime(String postdatetime) {
        this.postdatetime = postdatetime;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getRepost() {
        return repost;
    }

    public void setRepost(String repost) {
        this.repost = repost;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public ArrayList<UserOWnLikeImage> getImageModels() {
        return imageModels;
    }

    public void setImageModels(ArrayList<UserOWnLikeImage> imageModels) {
        this.imageModels = imageModels;
    }
}
