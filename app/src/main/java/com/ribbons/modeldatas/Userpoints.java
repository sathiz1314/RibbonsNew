
package com.ribbons.modeldatas;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Userpoints {

    @SerializedName("points")
    @Expose
    private String points;

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

}
