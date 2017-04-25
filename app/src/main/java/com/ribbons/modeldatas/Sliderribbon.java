
package com.ribbons.modeldatas;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sliderribbon {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("brandname")
    @Expose
    private String brandname;
    @SerializedName("brandlogo")
    @Expose
    private String brandlogo;
    @SerializedName("ribbontype")
    @Expose
    private String ribbontype;
    @SerializedName("pointsrequired")
    @Expose
    private String pointsrequired;
    @SerializedName("ribbondescription")
    @Expose
    private String ribbondescription;
    @SerializedName("outletname")
    @Expose
    private String outletname;
    @SerializedName("distance")
    @Expose
    private Double distance;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBrandname() {
        return brandname;
    }

    public String getOutletname() {
        return outletname;
    }

    public void setOutletname(String outletname) {
        this.outletname = outletname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname;
    }

    public String getBrandlogo() {
        return brandlogo;
    }

    public void setBrandlogo(String brandlogo) {
        this.brandlogo = brandlogo;
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

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

}
