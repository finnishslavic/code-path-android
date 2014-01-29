/**
 * File: SearchImagesIntentService.java
 * Created: 1/25/14
 * Author: Viacheslav Panasenko
 */
package com.panasenko.imagesearch.service;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import com.github.kevinsawicki.http.HttpRequest;
import com.panasenko.imagesearch.model.SearchFilter;
import com.panasenko.imagesearch.util.FileWriterUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

/**
 * SearchImagesIntentService
 * Performs background search images request and parses data.
 */
public class SearchImagesIntentService extends IntentService {

    private static final String TAG = SearchImagesIntentService.class.getSimpleName();
    private static final String SEARCH_ENDPOINT = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=";

    public static final String EXTRA_SEARCH_TERM = "com.panasenko.imagesearch.EXTRA_SEARCH_TERM";
    public static final String EXTRA_ADVANCED_SEARCH = "com.panasenko.imagesearch.EXTRA_ADVANCED_SEARCH";
    public static final String EXTRA_OFFSET = "com.panasenko.imagesearch.EXTRA_OFFSET";
    public static final String ADVANCED_SETTINGS_FORMAT = "&imgsz=%s&imgcolor=%s&imgtype=%s&as_sitesearch=%s";

    public static final String ACTION_SEARCH_FINISHED = "com.panasenko.imagesearch.ACTION_SEARCH_FINISHED";

    private static final int MAX_RESULTS = 8;

    /**
     * Default constructor.
     */
    public SearchImagesIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String searchTerm = intent.getStringExtra(EXTRA_SEARCH_TERM);
        String advancedSearch = null;
        if (intent.hasExtra(EXTRA_ADVANCED_SEARCH)) {
            SearchFilter filter = (SearchFilter) intent.getSerializableExtra(EXTRA_ADVANCED_SEARCH);
            advancedSearch = convertToAdvancedSerachUrl(filter);
        }

        int offset = intent.getIntExtra(EXTRA_OFFSET, 0);
        String responseJson = searchImages(searchTerm, advancedSearch, offset);
        try {
            List<String> imageUrls = parseImages(responseJson);
            FileWriterUtil.writeStringsToFile(this, imageUrls);
            notifyCompletion();
        } catch (JSONException e) {
            Log.d(TAG, "Parsing failed", e);
        }
    }

    /**
     * Searches for images from google by given search term and advanced settings if present.
     * @param searchTerm Terms to be used for image search.
     * @param advancedSettings Advanced search settings.
     * @return String containing JSON response.
     */
    private String searchImages(String searchTerm, String advancedSettings, int offset) {
        if (TextUtils.isEmpty(searchTerm))
        {
            throw new IllegalArgumentException("Search query should not be empty");
        }

        StringBuilder url = new StringBuilder(SEARCH_ENDPOINT + URLEncoder.encode(searchTerm));
        url.append("&rsz=" + MAX_RESULTS);
        url.append("&start=" + offset);
        if (!TextUtils.isEmpty(advancedSettings)) {
            Log.d(TAG, "Adavanced search params:" + advancedSettings);
            url.append(advancedSettings);
        }

        HttpRequest imgSearchRequest = HttpRequest.get(url.toString());
        if (imgSearchRequest.ok()) {
            return imgSearchRequest.body();
        } else {
            // TODO: think how to handle errors properly
            return "";
        }
    }

    /**
     * Parses image URLs from the given JSON string.
     * @param searchResult JSON string containing image search results.
     * @return List of URL string to images.
     * @throws JSONException If parsing error occurs.
     */
    private List<String> parseImages(String searchResult) throws JSONException {
        JSONObject responseBody = new JSONObject(searchResult);
        JSONArray images = responseBody.getJSONObject("responseData").getJSONArray("results");
        List<String> parsedData = new LinkedList<String>();
        for (int i = 0; i < images.length(); i++) {
            JSONObject imageJson = images.getJSONObject(i);
            String url = imageJson.getString("url");
            parsedData.add(url);
            Log.d(TAG, "Parsed image with URL: " + url);
            // TODO: parse the rest of the data, like 'Title', '
        }

        return parsedData;
    }

    /**
     * Converts filter object into an URL parameters string.
     * @param filter Image search filter object.
     * @return String containing key=value parameters to be used in the search request URL.
     */
    private String convertToAdvancedSerachUrl(SearchFilter filter) {
        return String.format(ADVANCED_SETTINGS_FORMAT, URLEncoder.encode(filter.getImageSize()),
                URLEncoder.encode(filter.getImageColor()), URLEncoder.encode(filter.getImageType()),
                URLEncoder.encode(filter.getSiteFilter()));
    }

    /**
     * Notifies about search completion via broadcast.
     */
    private void notifyCompletion() {
        Intent searchComplete = new Intent(ACTION_SEARCH_FINISHED);
        sendBroadcast(searchComplete);
    }
}
