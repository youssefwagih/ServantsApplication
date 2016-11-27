package com.example.youssefwagih.servantsapplication.UI;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.firebase.client.Firebase;

public class App extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
