
package com.ribbons.modeldatas;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LastVisitDataModel {

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("lastvisits")
    @Expose
    private List<Lastvisit> lastvisits = null;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Lastvisit> getLastvisits() {
        return lastvisits;
    }

    public void setLastvisits(List<Lastvisit> lastvisits) {
        this.lastvisits = lastvisits;
    }

}
