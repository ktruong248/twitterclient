package com.codepath.apps.restclienttemplate.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.RestApplication;
import com.codepath.apps.restclienttemplate.RestClient;
import com.codepath.apps.restclienttemplate.handler.JSONObjectHttpErrorHandler;
import com.codepath.apps.restclienttemplate.listener.EndlessScrollListener;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by ktruong on 3/15/15.
 */
public class MentionTimeLineFragment extends TweetsListFragment {
    public static final int COUNT_PER_PAGE = 25;

    private RestClient restClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        restClient = RestApplication.getRestClient();
        getMentionAndPopulate(1, COUNT_PER_PAGE);

//        populateTimeLine(1, COUNT_PER_PAGE);
//        // only need to set fragment if it not already created
        if (savedInstanceState == null) {
            setOnScrollListener(new EndlessScrollListener() {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    // Triggered only when new data needs to be appended to the list
                    // Add whatever code is needed to append new items to your AdapterView
                    getMentionAndPopulate(page, totalItemsCount);
                }
            });
            setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getMentionAndPopulate(1, COUNT_PER_PAGE);
                }
            });
        }
    }

    public void getMentionAndPopulate(int page, int countPerPage) {
        restClient.getMentionsTimeLine(page, countPerPage, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, JSONArray response) {
                Log.i(this.getClass().getSimpleName(), response.toString());
                List<Tweet> newTweets = Tweet.fromArrayJSON(response);
                if (newTweets.isEmpty()) {
                    showToastText("No Mentions", Toast.LENGTH_SHORT);
                } else {
                    addAll(newTweets);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i(this.getClass().getSimpleName(), "failed " + errorResponse.toString(), throwable);
                JSONObjectHttpErrorHandler handler = new JSONObjectHttpErrorHandler();
                handler.handle(statusCode, headers, throwable, errorResponse, getActivity());
            }
        });
    }

}
