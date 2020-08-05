package com.acra.tlm.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.DisplayContext;
import android.preference.PreferenceManager;

import com.acra.tlm.model.TargetModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ManageSharedPreferences {
    public static String TARGET_NAME = "targetname";
    public static String TARGET_URL = "targeturl";
    public static String TOKEN = "token";
    public static String USER_NAME = "user_name";
    public static String CHECKBOX = "checkbox";
    public static String USER_PASSWORD = "password";

    private static ManageSharedPreferences mManageSharedPreferences = null;



    private ManageSharedPreferences() {

    }

    public static synchronized ManageSharedPreferences newInstance() {
        if (mManageSharedPreferences == null) {
            mManageSharedPreferences = new ManageSharedPreferences();
        }

        return mManageSharedPreferences;
    }

    public void saveString(Context context, String text, String key) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(key, MODE_PRIVATE); //1
        editor = settings.edit(); //2
        editor.putString(key, text); //3
        //  editor.commit(); //4
        editor.apply(); //4
    }


    /**
     * Method to get the string value.
     *
     * @param context
     * @return
     */
    public String getString(Context context, String key) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(key, MODE_PRIVATE); //1
        text = settings.getString(key, null); //2
        return text;
    }
    public void saveArrayList(ArrayList<TargetModel> list, String key, Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
       // editor.commit();
        editor.apply();     // This line is IMPORTANT !!!
    }

    public ArrayList<TargetModel> getArrayList(String key,Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<TargetModel>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public void saveBoolean(Context context, boolean value, String key) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(key, MODE_PRIVATE); //1
        editor = settings.edit(); //2
        editor.putBoolean(key, value); //3
        editor.commit(); //4
    }


    /**
     * Method to get the string value.
     *
     * @param context
     * @return
     */
    public boolean getBoolean(Context context, String key) {
        SharedPreferences settings;
        settings = context.getSharedPreferences(key, MODE_PRIVATE); //1
        return settings.getBoolean(key, false); //2;
    }

}
