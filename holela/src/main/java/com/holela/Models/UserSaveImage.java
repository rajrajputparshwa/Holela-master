package com.holela.Models;

/**
 * Created by Raj on 6/29/2017.
 */

public class UserSaveImage {

    String image, thumb;

    public UserSaveImage() {

    }

    public UserSaveImage(String image, String thumb) {
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
