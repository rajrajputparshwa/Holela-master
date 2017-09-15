package com.holela.Models;

/**
 * Created by admin on 6/9/2017.
 */

public class UserMediaImageModel {
    String image,thumb;
    public UserMediaImageModel(){

    }

    public UserMediaImageModel(String image , String thumb) {
        this.image = image;
        this.thumb = thumb;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
