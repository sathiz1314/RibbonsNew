package com.ribbons.modeldatas;

/**
 * Created by User on 03-Mar-17.
 */

public class HomeModelData {
    private int image;
    private String price;
    private String title;

    public HomeModelData() {
    }

    public HomeModelData(int image) {
        this.image = image;
    }

    public HomeModelData(int image, String price, String title) {
        this.image = image;
        this.price = price;
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
