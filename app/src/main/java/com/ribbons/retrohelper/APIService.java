package com.ribbons.retrohelper;


import com.ribbons.helper.SharedHelper;
import com.ribbons.modeldatas.BrandDataModel;
import com.ribbons.modeldatas.BrandDetailsDataModel;
import com.ribbons.modeldatas.HomeDataModel;
import com.ribbons.modeldatas.HomeDetailDataModel;
import com.ribbons.modeldatas.LastVisitDataModel;
import com.ribbons.modeldatas.LocationModel;
import com.ribbons.modeldatas.LoginDataModel;
import com.ribbons.modeldatas.MyRibbonDataModel;
import com.ribbons.modeldatas.ResponceDataModel;
import com.ribbons.modeldatas.RibbonsDataModel;
import com.ribbons.modeldatas.SearchDataModel;
import com.ribbons.modeldatas.SendVoucherDataModel;
import com.ribbons.modeldatas.SocialDataModel;
import com.ribbons.modeldatas.UnTieRibbonDataModel;
import com.ribbons.retromodels.OtpDataModel;
import com.ribbons.retromodels.SignupDataModel;
import com.ribbons.retromodels.UserProfileModelData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by User on 13-Feb-17.
 */

public interface APIService {

    @GET("api/signup")
    Call<LocationModel> locationGet();

    @GET("api/allbrands")
    Call<BrandDataModel> getBrand(@Header("Accept") String accept,
                                  @Header("Authorization") String Authorization);

    @POST("api/signup")
    @FormUrlEncoded
    Call<SignupDataModel> signupPost(@Field("firstname") String firstname,
                                     @Field("lastname")
                                             String lastname,
                                     @Field("email")
                                             String email,
                                     @Field("password")
                                             String password,
                                     @Field("dob")
                                             String dob,
                                     @Field("phonenumber")
                                             String phonenumber,
                                     @Field("gender")
                                             String gender,
                                     @Field("location")
                                             String location,
                                     @Field("type")
                                             String type,
                                     @Field("socialid")
                                             String socialid,
                                     @Field("ribbonpin")
                                             String ribbonpin);

    @POST("api/signup")
    @FormUrlEncoded
    Call<SignupDataModel> signupPostWithoutMobile(@Field("firstname") String firstname,
                                                  @Field("lastname")
                                                          String lastname,
                                                  @Field("email")
                                                          String email,
                                                  @Field("password")
                                                          String password,
                                                  @Field("dob")
                                                          String dob,
                                                  @Field("gender")
                                                          String gender,
                                                  @Field("location")
                                                          String location,
                                                  @Field("type")
                                                          String type,
                                                  @Field("socialid")
                                                          String socialid,
                                                  @Field("ribbonpin")
                                                          String ribbonpin);

    @FormUrlEncoded
    @POST("oauth/token")
    Call<LoginDataModel> loginPost(@Field("client_id") String client_id,
                                   @Field("client_secret") String client_secret,
                                   @Field("grant_type") String grant_type,
                                   @Field("username") String username,
                                   @Field("scope") String scope,
                                   @Field("password") String password);

    @FormUrlEncoded
    @POST("api/otpsend")
    Call<OtpDataModel> otp(@Field("phonenumber") String phonenumber);


    @FormUrlEncoded
    @POST("api/myhomeapi")
    Call<HomeDataModel> getHomeData(@Header("Accept") String accept,
                                    @Header("Authorization") String Authorization,
                                    @Field("lat") String lat,
                                    @Field("long") String lng);

    @FormUrlEncoded
    @POST("api/ribbons")
    Call<RibbonsDataModel> getRibbonData(@Header("Accept") String accept,
                                         @Header("Authorization") String Authorization,
                                         @Field("lat") String lat,
                                         @Field("long") String lng);

    @FormUrlEncoded
    @POST("api/ribbondetails")
    Call<HomeDetailDataModel> getHomeDetails(@Header("Accept") String accept,
                                             @Header("Authorization") String Authorization,
                                             @Field("ribbonid") String ribbonid);


    @FormUrlEncoded
    @POST("api/detailbrands")
    Call<BrandDetailsDataModel> getBrandDetails(@Header("Accept") String accept,
                                                @Header("Authorization") String Authorization,
                                                @Field("brandid") String brandid);

    @FormUrlEncoded
    @POST("api/showmyprofile")
    Call<UserProfileModelData> getUserProfile(@Header("Accept") String accept,
                                              @Header("Authorization") String Authorization,
                                              @Field("profile") String profile);

    @FormUrlEncoded
    @POST("api/getribbons")
    Call<UnTieRibbonDataModel> getUntieRibbon(@Header("Accept") String accept,
                                              @Header("Authorization") String Authorization,
                                              @Field("ribbonid") String ribbonid);

    @FormUrlEncoded
    @POST("api/myribbons")
    Call<MyRibbonDataModel> getMyRibbonData(@Header("Accept") String accept,
                                            @Header("Authorization") String Authorization,
                                            @Field("ribbonid") String ribbonid);

    @FormUrlEncoded
    @POST("api/search")
    Call<SearchDataModel> getSerachData(@Header("Accept") String accept,
                                          @Header("Authorization") String Authorization,
                                          @Field("keyword") String keyword);

    @FormUrlEncoded
    @POST("api/changepassword")
    Call<ResponceDataModel> getChangePasswordData(@Header("Accept") String accept,
                                                  @Header("Authorization") String Authorization,
                                                  @Field("oldpassword") String oldpassword,
                                                  @Field("newpassword") String newpassword,
                                                  @Field("cnfpassword") String cnfpassword);

    @FormUrlEncoded
    @POST("api/transferribbons")
    Call<SendVoucherDataModel> getSendVoucher(@Header("Accept") String accept,
                                              @Header("Authorization") String Authorization,
                                              @Field("ribbonpin") String ribbonpin,
                                              @Field("ribbonid") String ribbonid);

    @FormUrlEncoded
    @POST("api/socialauth")
    Call<SocialDataModel> getSocial(@Field("socialid") String socialid);

    @FormUrlEncoded
    @POST("api/lastvisit")
    Call<LastVisitDataModel> getLastVisit(@Header("Accept") String accept,
                                           @Header("Authorization") String Authorization,
                                           @Field("keyword") String keyword);

}
