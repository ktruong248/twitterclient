package com.codepath.apps.restclienttemplate.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.RestApplication;
import com.codepath.apps.restclienttemplate.RestClient;
import com.codepath.apps.restclienttemplate.adapter.TweetArrayAdapter;
import com.codepath.apps.restclienttemplate.listener.EndlessScrollListener;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ktruong on 3/15/15.
 */
public class HomeTimeLineFragment extends TweetsListFragment {

    public static final int COUNT_PER_PAGE = 25;
    private static final int TWEET_REQUEST_CODE = 20;

    private RestClient restClient;
//    private TweetArrayAdapter tweetAdapter;
//    private SwipeRefreshLayout swipeContainer;
//    private TweetsListFragment tweetsListFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        restClient = RestApplication.getRestClient();
        populateTimeLine(1, COUNT_PER_PAGE);
        // only need to set fragment if it not already created
        if (savedInstanceState == null) {
//            tweetsListFragment = (TweetsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_timeline);
//            tweetsListFragment.setOnScrollListener(new EndlessScrollListener() {
            setOnScrollListener(new EndlessScrollListener() {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    // Triggered only when new data needs to be appended to the list
                    // Add whatever code is needed to append new items to your AdapterView
                    populateTimeLine(page, totalItemsCount);
                }
            });
//            tweetsListFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // Your code to refresh the list here.
                    // Make sure you call swipeContainer.setRefreshing(false)
                    // once the network request has completed successfully.
//                    tweets.clear();
                    populateTimeLine(1, COUNT_PER_PAGE);
                }
            });
        }
    }

    public void populateTimeLine(int page, int countPerPage) {
        restClient.getTimeLine(page, countPerPage, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, JSONArray response) {
                Log.i(this.getClass().getSimpleName(), response.toString());
//                tweets.clear();
//                tweetAdapter.addAll(Tweet.fromArrayJSON(response));
//                swipeContainer.setRefreshing(false);
//                tweetsListFragment.addAll(Tweet.fromArrayJSON(response));
                addAll(Tweet.fromArrayJSON(response));
            }

            @Override
            public void onFailure(int i, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i(this.getClass().getSimpleName(), "failed " + errorResponse.toString(), throwable);
                JSONArray errors = null;
                try {
                    errors = errorResponse.getJSONArray("errors");
                    if (errors != null && errors.length() > 0) {
//                        Toast.makeText(TimelineActivity.this, errors.getJSONObject(0).getString("message"), Toast.LENGTH_SHORT).show();
//                        tweetsListFragment.showToastText(errors.getJSONObject(0).getString("message"), Toast.LENGTH_SHORT);
                        showToastText(errors.getJSONObject(0).getString("message"), Toast.LENGTH_SHORT);
                    }
                } catch (JSONException e) {
                    Log.e(this.getClass().getName(), "failed to get json errors", e);
                }

//                swipeContainer.setRefreshing(false);
            }
        });
    }

    public void createTweetAndReload(String tweetMsg) {
        if (!tweetMsg.isEmpty()) {
            restClient.createTweet(tweetMsg, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, JSONObject response) {
                    Log.i(this.getClass().getSimpleName(), response.toString());
                    populateTimeLine(1, COUNT_PER_PAGE);
                }

                @Override
                public void onFailure(int i, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.i(this.getClass().getSimpleName(), "failed " + errorResponse.toString(), throwable);
                }

            });
        }
    }
}
