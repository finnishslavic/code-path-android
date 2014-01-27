package com.panasenko.imagesearch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends FragmentActivity {

    private static final int REQUEST_SEARCH_SETTINGS = 100500;

    private BroadcastReceiver searchResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateSearchResults();
        }
    };

    private EditText searchInput;
    private GridView imagesGrid;
    private ImagesAdapter adapter;
    private SearchFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        initViews();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(searchResultReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(searchResultReceiver,
                new IntentFilter(SearchImagesIntentService.ACTION_SEARCH_FINISHED));
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

    public void onSearchClicked(View v) {
        String searchTerm = searchInput.getText().toString();
        if (TextUtils.isEmpty(searchTerm)) {
            FileWriterUtil.cleanCache(this);
            Intent searchImages = new Intent(this, SearchImagesIntentService.class);
            searchImages.putExtra(SearchImagesIntentService.EXTRA_SEARCH_TERM, searchTerm);

            if (filter != null) {
                searchImages.putExtra(SearchImagesIntentService.EXTRA_ADVANCED_SEARCH, filter);
            }

            startService(searchImages);
        }
    }

    private void initViews() {
        searchInput = (EditText) findViewById(R.id.search_input);
        imagesGrid = (GridView) findViewById(R.id.result_grid);

        adapter = new ImagesAdapter(this, new ArrayList<String>());
        imagesGrid.setAdapter(adapter);
    }

    private void updateSearchResults() {
        AsyncReadResultsTask readResultsTask = new AsyncReadResultsTask();
        readResultsTask.execute();
    }

    private class AsyncReadResultsTask extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... voids) {
            List<String> result = FileWriterUtil.readStringsFromFile(SearchActivity.this);
            return result;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            if (strings != null && strings.size() > 0) {
                adapter = new ImagesAdapter(SearchActivity.this, strings);
                imagesGrid.setAdapter(adapter);
            }
        }
    }

}
