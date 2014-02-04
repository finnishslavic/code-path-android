package com.panasenko.twitterclient.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.panasenko.twitterclient.R;
import com.panasenko.twitterclient.TwitterClientApp;
import com.panasenko.twitterclient.adapter.TimelineAdapter;
import com.panasenko.twitterclient.model.Tweet;
import com.panasenko.twitterclient.service.RestClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * FeedActivity
 * Twitter feed activity.
 */
public class FeedActivity extends Activity {

    private ListView feedList;
    private List<Tweet> feedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_activity);

        initViews();
//        loadTweets();
        loadTweetsFromFile();
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

    private void initViews() {
        feedList = (ListView) findViewById(android.R.id.list);
    }

    private void loadTweets() {
        RestClient client = TwitterClientApp.getRestClient();
        client.getHomeTimeline(1, new JsonHttpResponseHandler() {
            public void onSuccess(JSONArray json) {
                ArrayList<Tweet> tweets = Tweet.fromJson(json);
                TimelineAdapter adapter = new TimelineAdapter(FeedActivity.this, tweets);
                feedList.setAdapter(adapter);
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

    private void loadTweetsFromFile() {
        new AsyncTask<Void, Void, List<Tweet>>() {

            @Override
            protected List<Tweet> doInBackground(Void... voids) {
                List<Tweet> tweets = new ArrayList<Tweet>(25);
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
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return tweets;
            }

            @Override
            protected void onPostExecute(List<Tweet> tweets) {
//                ArrayAdapter<Tweet> adapter = new ArrayAdapter<Tweet>(FeedActivity.this,
//                        android.R.layout.simple_list_item_1, tweets);
                TimelineAdapter adapter = new TimelineAdapter(FeedActivity.this, tweets);
                feedList.setAdapter(adapter);
            }
        }.execute();
    }

}
