
package com.ribbons.modeldatas;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Userribbons {

    @SerializedName("ribboncount")
    @Expose
    private Integer ribboncount;

    public Integer getRibboncount() {
        return ribboncount;
    }

    public void setRibboncount(Integer ribboncount) {
        this.ribboncount = ribboncount;
    }

}
