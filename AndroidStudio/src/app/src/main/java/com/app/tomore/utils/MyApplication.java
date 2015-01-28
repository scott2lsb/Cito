package com.app.tomore.utils;

/**
 * Created by Gavin on 26/01/2015.
 */
import android.app.Application;
import com.aviary.android.feather.sdk.IAviaryClientCredentials;

public class MyApplication extends Application implements IAviaryClientCredentials {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public String getBillingKey() {
        return ""; // leave it blank
    }

    @Override
    public String getClientID() {
        return "YOUR_API_KEY";
    }

    @Override
    public String getClientSecret() {
        return "YOUR_API_SECRET";
    }
}
