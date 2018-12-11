package com.example.android.quizzy.api;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Mahmoud on 9/5/2018.
 */
public class SharedManager {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SharedManager(Context context) {
        sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setUserUID(String uid) {
        editor.putString("user_uid", uid).apply();
    }

    public String getUserUUID() {
        return sharedPreferences.getString("user_uid", "");
    }


    public String getUserName() {
        return sharedPreferences.getString("name", "");
    }

    public void setUserName(String userName) {
        editor.putString("name", userName).apply();
    }

    public void clearUsername() {
        editor.putString("name", "");
    }

    public void clearUID() {
        editor.putString("user_uid", "").apply();
    }

    public void clear() {
        editor.clear().apply();
    }

}
