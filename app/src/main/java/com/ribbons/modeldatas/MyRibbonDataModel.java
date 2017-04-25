
package com.ribbons.modeldatas;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyRibbonDataModel {

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("myribbons")
    @Expose
    private List<Myribbon> myribbons = null;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Myribbon> getMyribbons() {
        return myribbons;
    }

    public void setMyribbons(List<Myribbon> myribbons) {
        this.myribbons = myribbons;
    }

}
