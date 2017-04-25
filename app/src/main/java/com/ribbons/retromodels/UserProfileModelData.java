
package com.ribbons.retromodels;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserProfileModelData {

    @SerializedName("userdata")
    @Expose
    private List<Userdatum> userdata = null;
    @SerializedName("location")
    @Expose
    private List<Location> location = null;

    public List<Userdatum> getUserdata() {
        return userdata;
    }

    public void setUserdata(List<Userdatum> userdata) {
        this.userdata = userdata;
    }

    public List<Location> getLocation() {
        return location;
    }

    public void setLocation(List<Location> location) {
        this.location = location;
    }

}
