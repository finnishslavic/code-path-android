package com.panasenko.imagesearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class SearchActivity extends FragmentActivity {

    private static final int REQUEST_SEARCH_SETTINGS = 100500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent getSettings = new Intent(this, SettingsActivity.class);
                startActivityForResult(getSettings, REQUEST_SEARCH_SETTINGS);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
