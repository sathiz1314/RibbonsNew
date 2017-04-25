
package com.ribbons.modeldatas;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BrandDetailsDataModel {

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("branddetails")
    @Expose
    private List<Branddetail> branddetails = null;
    @SerializedName("ribbonslist")
    @Expose
    private List<Ribbonslist> ribbonslist = null;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Branddetail> getBranddetails() {
        return branddetails;
    }

    public void setBranddetails(List<Branddetail> branddetails) {
        this.branddetails = branddetails;
    }

    public List<Ribbonslist> getRibbonslist() {
        return ribbonslist;
    }

    public void setRibbonslist(List<Ribbonslist> ribbonslist) {
        this.ribbonslist = ribbonslist;
    }

}
