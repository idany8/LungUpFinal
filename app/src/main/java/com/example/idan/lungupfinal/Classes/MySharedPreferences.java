package com.example.idan.lungupfinal.Classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MySharedPreferences {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    public MySharedPreferences(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public String getStringFromSharedPrefernces(String sp, String def) {
        return prefs.getString(sp, def);
    }
    public void putStringIntoSharedPrefernces(String sp, String value) {
        editor = prefs.edit();
        editor.putString(sp, value);
        editor.commit();
    }


    public int getIntFromSharedPrefernces(String sp, int def) {
        return prefs.getInt(sp, def);
    }
    public void putIntIntoSharedPrefernces(String sp, int value) {
        editor = prefs.edit();
        editor.putInt(sp, value);
        editor.commit();
    }

    public long getLongFromSharedPreferences(String sp, long def) {
        return prefs.getLong(sp, def);
    }
    public void putLongIntoSharedPrefernces(String sp, long value) {
        editor = prefs.edit();
        editor.putLong(sp, value);
        editor.commit();
    }


    public boolean getBooleanFromSharedPreferences(String sp, boolean def) {
        return prefs.getBoolean(sp, def);
    }
    public void putBooleanIntoSharedPrefernces(String sp, boolean value) {
        editor = prefs.edit();
        editor.putBoolean(sp, value);
        editor.commit();
    }
}
