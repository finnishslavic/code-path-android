/**
 * File: Tweet.java
 * Created: 2/3/14
 * Author: Viacheslav Panasenko
 */
package com.panasenko.twitterclient.model;

/**
 * Tweet
 * Twitter entry (tweet) model.
 */
public class Tweet {

    private long id;
    private String text;
    private String createdAt;

    /**
     * Default constructor.
     */
    public Tweet() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
