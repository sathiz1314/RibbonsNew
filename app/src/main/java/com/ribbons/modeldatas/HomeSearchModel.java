package com.ribbons.modeldatas;

/**
 * Created by User on 07-Mar-17.
 */

public class HomeSearchModel {
    private String images;
    private int img;
    private String name;

    public HomeSearchModel() {
    }

    public HomeSearchModel(int img, String name) {
        this.img = img;
        this.name = name;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
