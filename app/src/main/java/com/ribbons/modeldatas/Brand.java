
package com.ribbons.modeldatas;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Brand {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("brandname")
    @Expose
    private String brandname;
    @SerializedName("brandlogo")
    @Expose
    private String brandlogo;
    @SerializedName("brandbanner")
    @Expose
    private String brandbanner;
    @SerializedName("brandtype")
    @Expose
    private String brandtype;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getBrandtype() {
        return brandtype;
    }

    public void setBrandtype(String brandtype) {
        this.brandtype = brandtype;
    }

}
