
package com.ribbons.modeldatas;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Lastvisit {

    @SerializedName("brandname")
    @Expose
    private String brandname;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("brandbanner")
    @Expose
    private String brandbanner;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("ribbondescription")
    @Expose
    private String ribbondescription;

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getBrandbanner() {
        return brandbanner;
    }

    public void setBrandbanner(String brandbanner) {
        this.brandbanner = brandbanner;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRibbondescription() {
        return ribbondescription;
    }

    public void setRibbondescription(String ribbondescription) {
        this.ribbondescription = ribbondescription;
    }

}
