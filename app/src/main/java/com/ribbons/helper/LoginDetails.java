package com.ribbons.helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.ribbons.modeldatas.LoginDataModel;
import com.ribbons.retrohelper.APIService;
import com.ribbons.retrohelper.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 03-Apr-17.
 */

public class LoginDetails {

    APIService apiService = ApiUtils.getAPIService();
    private static final String TAG = "LoginDetails";
    private String responceResult="";
    private Context context;
    public LoginDetails() {

    }

    public String generalLogin(String mUsername, String mPassword) {

        try {
            apiService.loginPost("2","FrANVMXkWE5qf1nuS6RB3St0Cc3KKY3wUsBZA2sR","password", mUsername,"*", mPassword).enqueue(new Callback<LoginDataModel>() {
                @Override
                public void onResponse(Call<LoginDataModel> call, Response<LoginDataModel> response) {
                    if (response.isSuccessful()) {
                        Log.e(TAG, "Login_Responce" + response.body().getAccessToken());
                        responceResult ="true";
                    }
                }
                @Override
                public void onFailure(Call<LoginDataModel> call, Throwable t) {
                    Log.e(TAG, "Error_Login_Responce" + t.getLocalizedMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Something went wrong try again !", Toast.LENGTH_SHORT).show();

        }
        return responceResult;
    }

    public void socialLogin() {

    }
}
