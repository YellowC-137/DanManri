package com.example.dku_lf.database;

import android.content.Context;
import android.app.Application;

public class UserAppliaction extends Application {

    public  static String user_id; //이메일
    public  static String user_name;
    public  static String temp;
    public  static String uid;  //uid
    public  static String title;
    public  static String mtitle;


    @Override
    public void onCreate() {
        super.onCreate();
    }
}
