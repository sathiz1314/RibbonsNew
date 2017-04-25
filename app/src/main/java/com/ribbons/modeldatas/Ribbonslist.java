
package com.ribbons.modeldatas;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ribbonslist {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("ribbontype")
    @Expose
    private String ribbontype;
    @SerializedName("pointsrequired")
    @Expose
    private String pointsrequired;
    @SerializedName("ribbondescription")
    @Expose
    private String ribbondescription;
    @SerializedName("brandbanner")
    @Expose
    private String brandbanner;

    public String getBrandbanner() {
        return brandbanner;
    }

    public void setBrandbanner(String brandbanner) {
        this.brandbanner = brandbanner;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRibbontype() {
        return ribbontype;
    }

    public void setRibbontype(String ribbontype) {
        this.ribbontype = ribbontype;
    }

    public String getPointsrequired() {
        return pointsrequired;
    }

    public void setPointsrequired(String pointsrequired) {
        this.pointsrequired = pointsrequired;
    }

    public String getRibbondescription() {
        return ribbondescription;
    }

    public void setRibbondescription(String ribbondescription) {
        this.ribbondescription = ribbondescription;
    }

}
