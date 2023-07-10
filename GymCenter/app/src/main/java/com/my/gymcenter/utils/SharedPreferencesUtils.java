package com.my.gymcenter.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.my.gymcenter.entity.User;

public class SharedPreferencesUtils {

    public static boolean saveUserInfo(Context context, User user) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
            Editor editor = sharedPreferences.edit();
            editor.putString("role", user.getRole());
            editor.putInt("userId", user.getUserId());
            editor.putString("username", user.getUsername());
            editor.putString("name", user.getName());
            editor.putString("password", user.getPassword());
            editor.apply();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static User getUserInfo(Context context) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
            int userId = sharedPreferences.getInt("userId", 0);
            String password = sharedPreferences.getString("password", "");
            String username = sharedPreferences.getString("username", "");
            String role = sharedPreferences.getString("role", "user");
            String name = sharedPreferences.getString("name", "");
            User user = new User(username,password);
            user.setUserId(userId);
            user.setRole(role);
            user.setName(name);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return new User();
        }
    }

    public static String getServerUrl(Context context) {
        try {
            return "https://mekuri.top:20809/FitnessServer/";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
