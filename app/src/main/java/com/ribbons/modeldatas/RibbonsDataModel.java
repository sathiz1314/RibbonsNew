
package com.ribbons.modeldatas;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RibbonsDataModel {

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("ribbons")
    @Expose
    private List<Ribbon> ribbons = null;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Ribbon> getRibbons() {
        return ribbons;
    }

    public void setRibbons(List<Ribbon> ribbons) {
        this.ribbons = ribbons;
    }

}
