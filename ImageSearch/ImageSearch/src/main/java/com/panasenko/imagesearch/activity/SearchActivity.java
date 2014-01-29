package com.panasenko.imagesearch.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.panasenko.imagesearch.adapter.ImagesAdapter;
import com.panasenko.imagesearch.R;
import com.panasenko.imagesearch.model.SearchFilter;
import com.panasenko.imagesearch.service.SearchImagesIntentService;
import com.panasenko.imagesearch.util.FileWriterUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * SearchActivity
 * Search bar and search results for Google Image Search.
 */
public class SearchActivity extends Activity {

    private static final String TAG = SearchActivity.class.getSimpleName();

    private static final int REQUEST_SEARCH_SETTINGS = 100500;

    private static final String EXTRA_SEARCH_INPUT = "com.panasenko.imagesearch.SEARCH_INPUT";

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

    private String lastSearchTerm;

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
        updateSearchResults();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_SEARCH_INPUT, searchInput.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String searchText = savedInstanceState.getString(EXTRA_SEARCH_INPUT);
        searchInput.setText(searchText);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_SEARCH_SETTINGS) {
            filter = (SearchFilter) data.getSerializableExtra(SettingsActivity.EXTRA_FILTER);
        } else if (resultCode == RESULT_CANCELED) {
            filter = null;
        }
    }

    /**
     * Called when 'search' button clicked.
     * @param v Reference to a parent view.
     */
    public void onSearchClicked(View v) {
        lastSearchTerm = searchInput.getText().toString();
        if (!TextUtils.isEmpty(lastSearchTerm)) {
            FileWriterUtil.cleanCache(this);
            requestImageSearch(lastSearchTerm, 0);
        }
    }

    /**
     * Requests image search.
     * @param searchTerm Term used to search for images.
     * @param offset Offset of results.
     */
    private void requestImageSearch(String searchTerm, int offset) {
        Intent searchImages = new Intent(this, SearchImagesIntentService.class);
        searchImages.putExtra(SearchImagesIntentService.EXTRA_SEARCH_TERM, searchTerm);
        searchImages.putExtra(SearchImagesIntentService.EXTRA_OFFSET, offset);

        if (filter != null) {
            searchImages.putExtra(SearchImagesIntentService.EXTRA_ADVANCED_SEARCH, filter);
        }

        startService(searchImages);
    }

    /**
     * Initializes views and sets listeners.
     */
    private void initViews() {
        searchInput = (EditText) findViewById(R.id.search_input);
        imagesGrid = (GridView) findViewById(R.id.result_grid);

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getBaseContext()));

        adapter = new ImagesAdapter(this, new ArrayList<String>());
        imagesGrid.setAdapter(adapter);

        imagesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent showFullImage = new Intent(SearchActivity.this, ImageActivity.class);
                showFullImage.putExtra(ImageActivity.EXTRA_URL,
                        adapter.getItem(i).toString());
                startActivity(showFullImage);
            }
        });

        imagesGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                Uri streamUri = FileWriterUtil.getStreamUri(view);
                if (streamUri != null) {
                    share.putExtra(Intent.EXTRA_STREAM, streamUri);
                    startActivity(share);
                    return true;
                } else {
                    return false;
                }
            }
        });

        imagesGrid.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                requestImageSearch(lastSearchTerm, totalItemsCount);
            }
        });
    }

    /**
     * Updates search results on the grid view.
     */
    private void updateSearchResults() {
        // Update view only if there is an outstanding search
        if (lastSearchTerm != null && lastSearchTerm.length() > 0) {
            AsyncReadResultsTask readResultsTask = new AsyncReadResultsTask();
            readResultsTask.execute();
        }
    }

    /**
     * AsyncReadResultsTask
     * Asynchronously reads results from temporary file.
     */
    private class AsyncReadResultsTask extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... voids) {
            List<String> result = FileWriterUtil.readStringsFromFile(SearchActivity.this);
            return result;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            if (strings != null && strings.size() > 0) {
                adapter.setData(strings);
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * EndlessScrollListener
     * Listens to list view scrolling and requests for more data on update.
     */
    private abstract class EndlessScrollListener implements AbsListView.OnScrollListener {

        // The minimum amount of items to have below your current scroll position
        // before loading more.
        private int visibleThreshold = 5;
        // The current offset index of data you have loaded
        private int currentPage = 0;
        // The total number of items in the dataset after the last load
        private int previousTotalItemCount = 0;
        // True if we are still waiting for the last set of data to load.
        private boolean loading = true;
        // Sets the starting page index
        private int startingPageIndex = 0;

        public EndlessScrollListener() {
        }

        public EndlessScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        public EndlessScrollListener(int visibleThreshold, int startPage) {
            this.visibleThreshold = visibleThreshold;
            this.startingPageIndex = startPage;
            this.currentPage = startPage;
        }

        // This happens many times a second during a scroll, so be wary of the code you place here.
        // We are given a few useful parameters to help us work out if we need to load some more data,
        // but first we check if we are waiting for the previous load to finish.
        @Override
        public void onScroll(AbsListView view,int firstVisibleItem,int visibleItemCount,int totalItemCount)
        {
            // If the total item count is zero and the previous isn't, assume the
            // list is invalidated and should be reset back to initial state
            // If there are no items in the list, assume that initial items are loading
            if (!loading && (totalItemCount < previousTotalItemCount)) {
                this.currentPage = this.startingPageIndex;
                this.previousTotalItemCount = totalItemCount;
                if (totalItemCount == 0) { this.loading = true; }
            }

            // If it’s still loading, we check to see if the dataset count has
            // changed, if so we conclude it has finished loading and update the current page
            // number and total item count.
            if (loading) {
                if (totalItemCount > previousTotalItemCount) {
                    loading = false;
                    previousTotalItemCount = totalItemCount;
                    currentPage++;
                }
            }

            // If it isn’t currently loading, we check to see if we have breached
            // the visibleThreshold and need to reload more data.
            // If we do need to reload some more data, we execute onLoadMore to fetch the data.
            if (!loading && (totalItemCount - visibleItemCount)<=(firstVisibleItem + visibleThreshold))
            {
                onLoadMore(currentPage + 1, totalItemCount);
                loading = true;
            }
        }

        // Defines the process for actually loading more data based on page
        public abstract void onLoadMore(int page, int totalItemsCount);

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // Don't take any action on changed
        }
    }

}
