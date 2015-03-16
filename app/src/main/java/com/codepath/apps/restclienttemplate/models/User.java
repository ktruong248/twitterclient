package com.codepath.apps.restclienttemplate.models;

import org.json.JSONObject;

/**
 * Created by ktruong on 3/8/15.
 */
public class User {
    private String name;
    private long id;
    private boolean following;
    private long favouriteCount;
    private String createdAt;
    private long followersCount;
    private long friendCount;
    private String profileImgUrl;
    private String description;
    private String screenName;
    private long tweetCount;

    public long getTweetCount() {
        return tweetCount;
    }

    public void setTweetCount(long tweetCount) {
        this.tweetCount = tweetCount;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public boolean isFollowing() {
        return following;
    }

    public long getFavouriteCount() {
        return favouriteCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public long getFollowersCount() {
        return followersCount;
    }

    public long getFriendCount() {
        return friendCount;
    }

    public String getProfileImgUrl() {
        return profileImgUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getScreenName() {
        return "@" + screenName;
    }

    public static User fromJSON(JSONObject jsonObject) {
        User user = new User();
        try {
            user.id = jsonObject.getLong("id");
            user.name = jsonObject.getString("name");
            user.description = jsonObject.getString("description");
            user.screenName = jsonObject.getString("screen_name");
            user.following = jsonObject.getBoolean("following");
            user.favouriteCount = jsonObject.getLong("favourites_count");
            user.createdAt = jsonObject.getString("created_at");
            user.followersCount = jsonObject.getLong("followers_count");
            user.friendCount = jsonObject.getLong("friends_count");
            user.profileImgUrl = jsonObject.getString("profile_image_url");
            user.tweetCount = jsonObject.getLong("statuses_count");
            
            return user;
        }catch (Exception e) {
            throw new RuntimeException("failed to parse user", e);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("name='").append(name).append('\'');
        sb.append(", id=").append(id);
        sb.append(", following=").append(following);
        sb.append(", favouriteCount=").append(favouriteCount);
        sb.append(", createdAt='").append(createdAt).append('\'');
        sb.append(", followersCount=").append(followersCount);
        sb.append(", friendCount=").append(friendCount);
        sb.append(", profileImgUrl='").append(profileImgUrl).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", screenName='").append(screenName).append('\'');
        sb.append(", tweetCount='").append(tweetCount).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
