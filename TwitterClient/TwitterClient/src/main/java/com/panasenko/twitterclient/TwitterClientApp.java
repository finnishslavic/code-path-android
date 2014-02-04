/**
 * File: TwitterClientApp.java
 * Created: 2/3/14
 * Author: Viacheslav Panasenko
 */
package com.panasenko.twitterclient;

import android.app.Application;
import android.content.Context;
import com.activeandroid.ActiveAndroid;
import com.panasenko.twitterclient.service.RestClient;

/**
 * TwitterClientApp
 * The application class that exists during the application life time.
 */
public class TwitterClientApp extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        ActiveAndroid.initialize(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }

    public static RestClient getRestClient() {
        return (RestClient) RestClient.getInstance(RestClient.class, context);
    }

}
