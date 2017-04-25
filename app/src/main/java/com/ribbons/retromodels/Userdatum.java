
package com.ribbons.retromodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Userdatum {

    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("phonenumber")
    @Expose
    private String phonenumber;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("ribbonpin")
    @Expose
    private String ribbonpin;
    @SerializedName("location")
    @Expose
    private String location;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRibbonpin() {
        return ribbonpin;
    }

    public void setRibbonpin(String ribbonpin) {
        this.ribbonpin = ribbonpin;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
