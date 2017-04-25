package com.ribbons.retrohelper;

/**
 * Created by User on 13-Feb-17.
 */

public class ApiUtils {
    private ApiUtils() {}

    public static final String BASE_URL = "http://139.59.25.20/Ribbon/public/";

    public static APIService getAPIService() {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
