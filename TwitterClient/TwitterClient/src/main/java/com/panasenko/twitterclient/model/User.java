/**
 * File: User.java
 * Created: 2/3/14
 * Author: Viacheslav Panasenko
 */
package com.panasenko.twitterclient.model;

/**
 * User
 * Twitter user model.
 */
public class User {

    private long id;
    private String name;
    private String screenName;
    private String profileImageUrl;

    /**
     * Default constructor.
     */
    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
