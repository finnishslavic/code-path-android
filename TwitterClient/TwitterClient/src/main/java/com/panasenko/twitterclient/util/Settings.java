/**
 * File: Settings.java
 * Created: 1/11/14
 * Author: Viacheslav Panasenko
 */
package com.panasenko.twitterclient.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Settings
 * Utility class provides an easy access to user settings.
 */
public class Settings
{

    private static final String PREFERENCES_NAME = "twtw_client_prefs";

    private static final String KEY_AUTH_TOKEN = "auth_token";

    /**
     * Returns auth token or empty string.
     * @param ctx Context used to access SharedPreferences.
     * @return Empty string if not authenticated, auth token otherwise.
     */
    public static String getAuthToken(Context ctx) {
        return getSharedPreferences(ctx).getString(KEY_AUTH_TOKEN, "");
    }

    /**
     * Sets a new authentication token or empty string to reset 'cookies'.
     * @param ctx Context used to access SharedPreferences.
     * @param authToken A new authentication token or empty string to reset 'cookies'.
     */
    public static void setAuthToken(Context ctx, String authToken) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(KEY_AUTH_TOKEN, authToken);
        editor.commit();
    }

    /**
     * Returns shared preferences instance used for storing important user and application data.
     * @param ctx Context used to access SharedPreferences.
     * @return shared preferences instance used for storing important user and application data.
     */
    private static SharedPreferences getSharedPreferences(Context ctx) {
        return ctx.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }
}
