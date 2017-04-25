
package com.ribbons.modeldatas;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Myribbon {

    @SerializedName("ribbontype")
    @Expose
    private String ribbontype;
    @SerializedName("ribbondescription")
    @Expose
    private String ribbondescription;
    @SerializedName("pointsrequired")
    @Expose
    private String pointsrequired;
    @SerializedName("brandname")
    @Expose
    private String brandname;
    @SerializedName("brandlogo")
    @Expose
    private String brandlogo;
    @SerializedName("brandbanner")
    @Expose
    private String brandbanner;
    @SerializedName("users_id")
    @Expose
    private Integer usersId;

    @SerializedName("id")
    @Expose
    private Integer id;

    private boolean isSelected;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getRibbontype() {
        return ribbontype;
    }

    public void setRibbontype(String ribbontype) {
        this.ribbontype = ribbontype;
    }

    public String getRibbondescription() {
        return ribbondescription;
    }

    public void setRibbondescription(String ribbondescription) {
        this.ribbondescription = ribbondescription;
    }

    public String getPointsrequired() {
        return pointsrequired;
    }

    public void setPointsrequired(String pointsrequired) {
        this.pointsrequired = pointsrequired;
    }

    public String getBrandname() {
        return brandname;
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

    public String getBrandbanner() {
        return brandbanner;
    }

    public void setBrandbanner(String brandbanner) {
        this.brandbanner = brandbanner;
    }

    public Integer getUsersId() {
        return usersId;
    }

    public void setUsersId(Integer usersId) {
        this.usersId = usersId;
    }

}
