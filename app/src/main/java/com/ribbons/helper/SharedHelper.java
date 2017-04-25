package com.ribbons.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedHelper {

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "intro_dialogue-welcome";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public SharedHelper(Context context) {
        this._context = context;
        sharedPreferences = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void putKey(Context context, String Key, String Value) {
        sharedPreferences = context.getSharedPreferences("Ribbons", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(Key, Value);
        editor.commit();

    }

    public String getKey(Context contextGetKey, String Key) {
        sharedPreferences = contextGetKey.getSharedPreferences("Ribbons", Context.MODE_PRIVATE);
        String Value = sharedPreferences.getString(Key, "");
        return Value;

    }

    public void setSignUserId(Context context , String key , String value){
        SharedPreferences.Editor editor = context.getSharedPreferences("Ribbons", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getSignUserId(Context context, String key){
        SharedPreferences prefs = context.getSharedPreferences("Ribbons",	Context.MODE_PRIVATE);
        return prefs.getString(key, "");

    }

    public void setSignUserToken(Context context , String key , String value){
        SharedPreferences.Editor editor = context.getSharedPreferences("Ribbons", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getSignUserToken(Context context, String key){
        SharedPreferences prefs = context.getSharedPreferences("Ribbons",	Context.MODE_PRIVATE);
        return prefs.getString(key, "");

    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return sharedPreferences.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }



}
