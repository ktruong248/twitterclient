package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class Tweet {
    private String body;
    private long id;
    private String createdAt;
    private User user;

    public String getBody() {
        return body;
    }

    public long getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public static Tweet fromJSON(JSONObject tweetJson) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = tweetJson.getString("text");
            tweet.id = tweetJson.getLong("id");
            tweet.createdAt = Utils.formatRelativeTimeSpan(tweetJson.getString("created_at"));
            tweet.user = User.fromJSON(tweetJson.getJSONObject("user"));
            
            return tweet;
        } catch (Exception e) {
            throw new RuntimeException("failed to parse json", e);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Tweet{");
        sb.append("body='").append(body).append('\'');
        sb.append(", id=").append(id);
        sb.append(", createdAt='").append(createdAt).append('\'');
        sb.append(", user=").append(user);
        sb.append('}');
        return sb.toString();
    }

    public static List<Tweet> fromArrayJSON(JSONArray response) {
        if(response != null) {
            int length = response.length();
            List<Tweet> tweets = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                try {
                    tweets.add(Tweet.fromJSON(response.getJSONObject(i)));
                } catch (JSONException e) {
                    throw new RuntimeException("failed to convert tweet", e);
                }
            }
            
            return tweets;
        }
        
        return Collections.EMPTY_LIST;
    }
}
