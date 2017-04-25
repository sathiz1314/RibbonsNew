
package com.ribbons.modeldatas;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Success {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("locationname")
    @Expose
    private String locationname;
    @SerializedName("brandname")
    @Expose
    private String brandname;
    @SerializedName("brandbanner")
    @Expose
    private String brandbanner;
    @SerializedName("address")
    @Expose
    private String address;
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

    @SerializedName("userpoints")
    @Expose
    private Userpoints userpoints;
    @SerializedName("userribbons")
    @Expose
    private Userribbons userribbons;
    @SerializedName("sliderribbon")
    @Expose
    private List<Sliderribbon> sliderribbon = null;
    @SerializedName("ribbonobjects")
    @Expose
    private List<Ribbonobject> ribbonobjects = null;

    public String getOutletname() {
        return outletname;
    }

    public void setOutletname(String outletname) {
        this.outletname = outletname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocationname() {
        return locationname;
    }

    public void setLocationname(String locationname) {
        this.locationname = locationname;
    }

    public Userpoints getUserpoints() {
        return userpoints;
    }

    public void setUserpoints(Userpoints userpoints) {
        this.userpoints = userpoints;
    }

    public Userribbons getUserribbons() {
        return userribbons;
    }

    public void setUserribbons(Userribbons userribbons) {
        this.userribbons = userribbons;
    }

    public List<Sliderribbon> getSliderribbon() {
        return sliderribbon;
    }

    public void setSliderribbon(List<Sliderribbon> sliderribbon) {
        this.sliderribbon = sliderribbon;
    }

    public List<Ribbonobject> getRibbonobjects() {
        return ribbonobjects;
    }

    public void setRibbonobjects(List<Ribbonobject> ribbonobjects) {
        this.ribbonobjects = ribbonobjects;
    }

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname;
    }

    public String getBrandbanner() {
        return brandbanner;
    }

    public void setBrandbanner(String brandbanner) {
        this.brandbanner = brandbanner;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
