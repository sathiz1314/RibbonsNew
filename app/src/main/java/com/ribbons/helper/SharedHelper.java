package com.ribbons.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedHelper {

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    public void putKey(Context context, String Key, String Value) {
        sharedPreferences = context.getSharedPreferences("Truber", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(Key, Value);
        editor.commit();

    }

    public String getKey(Context contextGetKey, String Key) {
        sharedPreferences = contextGetKey.getSharedPreferences("Truber", Context.MODE_PRIVATE);
        String Value = sharedPreferences.getString(Key, "");
        return Value;

    }

    public void setSignUserId(Context context , String key , String value){
        SharedPreferences.Editor editor = context.getSharedPreferences("Binder", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getSignUserId(Context context, String key){
        SharedPreferences prefs = context.getSharedPreferences("Binder",	Context.MODE_PRIVATE);
        return prefs.getString(key, "");

    }

    public void setSignUserToken(Context context , String key , String value){
        SharedPreferences.Editor editor = context.getSharedPreferences("Binder", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getSignUserToken(Context context, String key){
        SharedPreferences prefs = context.getSharedPreferences("Binder",	Context.MODE_PRIVATE);
        return prefs.getString(key, "");

    }



}
