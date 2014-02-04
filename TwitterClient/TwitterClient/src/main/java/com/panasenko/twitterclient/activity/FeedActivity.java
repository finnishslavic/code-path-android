package com.panasenko.twitterclient.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.panasenko.twitterclient.R;

/**
 * FeedActivity
 * Twitter feed activity.
 */
public class FeedActivity extends Activity {

    private static final String TWT_CONSUMER_KEY = "";
    private static final String TWT_CONSUMER_SECRET = "";

    private static final String TWT_ACCESS_TOKEN = "15425304-LDt221kj92tSzhDGxNhJry9pOf02Z7uBiW5FJGGCz";
    private static final String TWT_TOKEN_SECRET = "TRvUuFSSZyTBIHAAmoIFOsRbBeOmoUWdMvtXhImGqG9ze";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_activity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
