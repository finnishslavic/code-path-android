/**
 * File: ComposeTweetActivity.java
 * Created: 1/30/14
 * Author: Viacheslav Panasenko
 */
package com.panasenko.twitterclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.panasenko.twitterclient.R;
import com.panasenko.twitterclient.TwitterClientApp;
import com.panasenko.twitterclient.model.Tweet;
import com.panasenko.twitterclient.service.RestClient;
import org.json.JSONObject;

/**
 * ComposeTweetActivity
 * Compose tweets activity.
 */
public class ComposeTweetActivity extends Activity {

    public static final String EXTRA_TWEET = "com.panasenko.twitterclient.EXTRA_TWEET";
    public static final String EXTRA_TWEET_TEXT = "com.panasenko.twitterclient.EXTRA_TWEET_TEXT";

    private static final int MAX_CHAR_COUNT = 140;
    private static final int COLOR_GRAY = android.R.color.darker_gray;
    private static final int COLOR_RED = android.R.color.holo_red_light;

    private TextView charCounter;
    private EditText tweetInput;
    private Button btnTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compose_tweet_activity);

        initViews();
    }

    /**
     * Called when 'Tweet' button is pressed.
     * @param v Reference to a caller view (Tweet button).
     */
    public void onTweetClicked(View v) {
        String tweetText = tweetInput.getText().toString();
        RestClient client = TwitterClientApp.getRestClient();
        client.postTweet(tweetText, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                // Parse tweet and finish activity
                Tweet twt = new Tweet(jsonObject);
                twt.save();
                Intent result = new Intent();
                result.putExtra(EXTRA_TWEET, twt);
                setResult(RESULT_OK, result);
                finish();
            }

            @Override
            public void onFailure(Throwable throwable, JSONObject jsonObject) {
                super.onFailure(throwable, jsonObject);
                Log.e("ComposeTweetActivity", "Error occurred");
                finish();
            }

        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_TWEET_TEXT, tweetInput.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String tweetText = savedInstanceState.getString(EXTRA_TWEET_TEXT);
        tweetInput.setText(tweetText);
        tweetInput.setSelection(tweetText.length());
        updateViews();
    }

    /**
     * Initializes UI views and assigns listeners for them.
     */
    private void initViews() {
        charCounter = (TextView) findViewById(R.id.tv_char_counter);
        tweetInput = (EditText) findViewById(R.id.et_tweet_input);
        btnTweet = (Button) findViewById(R.id.btn_tweet);

        tweetInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateViews();
            }
        });
    }

    /**
     * Updates view states on user input.
     */
    private void updateViews() {
        String twtText = tweetInput.getText().toString();
        int elapsedLength = MAX_CHAR_COUNT - twtText.length();
        if (elapsedLength >= 0 && elapsedLength < MAX_CHAR_COUNT) {
            btnTweet.setEnabled(true);
            charCounter.setTextColor(getResources().getColor(COLOR_GRAY));
        } else {
            btnTweet.setEnabled(false);
            charCounter.setTextColor(getResources().getColor(COLOR_RED));
        }

        charCounter.setText("" + elapsedLength);
    }
}
