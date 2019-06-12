package com.example.cristian.findgreenplaces;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class FindGreenPlacesApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppEventsLogger.activateApp(this);
    }
}
