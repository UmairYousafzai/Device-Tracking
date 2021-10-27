package com.example.devicetracker.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferenceHelper {

    private SharedPreferenceHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    private static final String SHARED_PREFERENCE_NAME = "SharedPreference";


    public static final String Login_State = "LoginState";
    private static com.example.devicetracker.utils.SharedPreferenceHelper helperInstance = null;
    private final SharedPreferences sharedPreferences;






    public static com.example.devicetracker.utils.SharedPreferenceHelper getInstance(Context context) {
        if (helperInstance == null) {
            helperInstance = new com.example.devicetracker.utils.SharedPreferenceHelper(context);
        }
        return helperInstance;
    }



    public Boolean getLogin_State() {
        return sharedPreferences.getBoolean(Login_State, false);
    }
    public Boolean setLogin_State(boolean state)
    {
        sharedPreferences.edit().putBoolean(Login_State, state).apply();
        return state;
    }

}
