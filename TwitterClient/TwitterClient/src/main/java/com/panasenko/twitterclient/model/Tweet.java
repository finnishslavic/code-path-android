/**
 * File: Tweet.java
 * Created: 2/3/14
 * Author: Viacheslav Panasenko
 */
package com.panasenko.twitterclient.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Tweet
 * Twitter entry (tweet) model.
 */
@Table(name = "Tweets")
public class Tweet extends Model implements Serializable {

    private static final long serialVersionUID = 402320935823454514L;

    @Column(name = "tweetId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    String tweetId;
    @Column(name = "username")
    String username;
    @Column(name = "userHandle")
    String userHandle;
    @Column(name = "avatar")
    String avatar;
    @Column(name = "timestamp")
    String timestamp;
    @Column(name = "body")
    String body;

    /**
     * Default constructor.
     */
    public Tweet() {
        super();
    }

    public Tweet(JSONObject object){
        super();

        try {
            tweetId = object.getString("id_str");
            timestamp = object.getString("created_at");
            body = object.getString("text");

            JSONObject user = object.getJSONObject("user");
            username = user.getString("name");
            userHandle = user.getString("screen_name");
            avatar = user.getString("profile_image_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Tweet tweet = new Tweet(tweetJson);
            tweet.save();
            tweets.add(tweet);
        }

        return tweets;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTweetId() {
        return tweetId;
    }

    public void setTweetId(String tweetId) {
        this.tweetId = tweetId;
    }

    public String getUserHandle() {
        return userHandle;
    }

    public void setUserHandle(String userHandle) {
        this.userHandle = userHandle;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return body;
    }

    /**
     * Converts timestamp to milliseconds.
     * @return
     * @throws ParseException
     */
    public long getTimestampMillis() throws ParseException {
        final String TWITTER = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(TWITTER);
        sf.setLenient(true);
        return sf.parse(timestamp).getTime();
    }

    /**
     * TweetTimestampComparator
     * Compares tweeter objects based on their creation dates.
     */
    public static class TweetTimestampComparator implements Comparator<Tweet> {

        @Override
        public int compare(Tweet tweet, Tweet tweet2) {
            try {
                return (int) (tweet2.getTimestampMillis() - tweet.getTimestampMillis());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return 0;
        }
    }
}
