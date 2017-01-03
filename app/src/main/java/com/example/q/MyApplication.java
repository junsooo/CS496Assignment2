package com.example.q.myapplication;

import android.app.Application;

import com.facebook.CallbackManager;

import java.util.ArrayList;

/**
 * Created by q on 2016-12-31.
 */

public class MyApplication extends Application {
    public static CallbackManager callbackManager;
    public static final ArrayList<String> fb_name_list = new ArrayList<String>();
    public static String query_string="";
    public static String response_string="";
    //public static ArrayList<String> name_list = new ArrayList<String>();
    @Override
    public void onCreate(){
        super.onCreate();
        patchEOFException();
    }

    private void patchEOFException() {
        System.setProperty("http.keepAlive", "false");
    }
}