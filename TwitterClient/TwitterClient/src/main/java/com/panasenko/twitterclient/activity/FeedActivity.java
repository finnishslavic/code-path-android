package com.panasenko.twitterclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.panasenko.twitterclient.R;
import com.panasenko.twitterclient.TwitterClientApp;
import com.panasenko.twitterclient.adapter.EndlessScrollListener;
import com.panasenko.twitterclient.adapter.TimelineAdapter;
import com.panasenko.twitterclient.model.Tweet;
import com.panasenko.twitterclient.service.RestClient;
import eu.erikw.PullToRefreshListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * FeedActivity
 * Twitter feed activity.
 */
public class FeedActivity extends Activity {

    private static final int REQUEST_COMPOSE_TWEET = 100500;
    private static final int MAX_TWEET_PER_PAGE = 20;

    private PullToRefreshListView timelineList;
    private List<Tweet> feedData;
    private TimelineAdapter timelineAdapter;

    private int lastLoadedPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_activity);

        initViews();
//        loadTweets();
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
            case R.id.action_compose:
                Intent compose = new Intent(this, ComposeTweetActivity.class);
                startActivityForResult(compose, REQUEST_COMPOSE_TWEET);
                return true;
            case R.id.action_refresh:
                refreshTimeline();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_COMPOSE_TWEET && resultCode == RESULT_OK) {
            Tweet twt = (Tweet) data.getSerializableExtra(ComposeTweetActivity.EXTRA_TWEET);

            if (feedData != null && timelineAdapter != null) {
                feedData.add(0, twt);
                timelineAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * Initializes views.
     */
    private void initViews() {
        timelineList = (PullToRefreshListView) findViewById(android.R.id.list);
        timelineList.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (totalItemsCount < MAX_TWEET_PER_PAGE) {
                    return;
                }

//                loadTweets(page);
                loadTweetsFromFile();
            }
        });

        timelineList.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTimeline();
            }
        });

        refreshTimeline();
    }

    /**
     * Loads tweets from the Internet.
     * @param page Page to be used for loading more tweets.
     */
    private void loadTweets(int page) {
        RestClient client = TwitterClientApp.getRestClient();
        client.getHomeTimeline(page, new JsonHttpResponseHandler() {
            public void onSuccess(JSONArray json) {
                // Parse and save data in background
                new AsyncParseAndUpdateTweets().execute(json);
            }

            @Override
            public void onFailure(Throwable throwable, JSONArray jsonArray) {
                super.onFailure(throwable, jsonArray);
                Log.e("FeedActivity", "Error retrieving tweets", throwable);
            }

            @Override
            public void onFailure(Throwable throwable, JSONObject jsonObject) {
                super.onFailure(throwable, jsonObject);
                Log.e("FeedActivity", "Error retrieving tweets", throwable);
            }

            @Override
            protected void handleMessage(Message message) {
                Log.e("FeedActivity", "Message received: " + message.toString());
                super.handleMessage(message);
            }
        });
    }

    /**
     * Refreshes timeline by reiniting data set.
     */
    private void refreshTimeline() {
        feedData = new ArrayList<Tweet>(MAX_TWEET_PER_PAGE);
        timelineAdapter = new TimelineAdapter(this, feedData);
        timelineList.setAdapter(timelineAdapter);

//        loadTweets(0);
        loadTweetsFromFile();
    }

    /**
     * Loads tweets from file.
     */
    private void loadTweetsFromFile() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                List<Tweet> tweets;
                try {
                    StringBuilder buf = new StringBuilder();
                    InputStream json = getAssets().open("tweet.json");
                    BufferedReader in = new BufferedReader(new InputStreamReader(json));
                    String str;

                    while ((str = in.readLine()) != null) {
                        buf.append(str);
                    }

                    in.close();

                    JSONArray array = new JSONArray(buf.toString());
                    tweets = Tweet.fromJson(array);
                    feedData.addAll(tweets);
                    Collections.sort(feedData, new Tweet.TweetTimestampComparator());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                timelineAdapter.notifyDataSetChanged();
                timelineList.onRefreshComplete();
            }
        }.execute();
    }

    /**
     * AsyncParseAndUpdateTweets
     * Async task that parses tweets from JSON and updates them in the list.
     */
    private class AsyncParseAndUpdateTweets extends AsyncTask<JSONArray, Void, Void> {

        @Override
        protected Void doInBackground(JSONArray... jsonArrays) {
            if (jsonArrays == null || jsonArrays.length < 1) {
                throw new IllegalArgumentException("JSONArray for parsing is empty");
            }

            feedData.addAll(Tweet.fromJson(jsonArrays[0]));
            Collections.sort(feedData, new Tweet.TweetTimestampComparator());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            timelineAdapter.notifyDataSetChanged();
            timelineList.onRefreshComplete();
        }
    }

}
