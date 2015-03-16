package com.codepath.apps.restclienttemplate.fragment;

import android.app.Activity;
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

/**
 *
 */
public class UserTimeLineFragment extends TweetsListFragment {
    public static final int COUNT_PER_PAGE = 25;

    private RestClient restClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restClient = RestApplication.getRestClient();

        final String screenName = getArguments().getString("screenName"); // since this is null twitter default to current user

        getUserTimeLineAndPopulate(1, COUNT_PER_PAGE, screenName);

        if (savedInstanceState == null) {
            setOnScrollListener(new EndlessScrollListener() {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    // Triggered only when new data needs to be appended to the list
                    // Add whatever code is needed to append new items to your AdapterView
                    getUserTimeLineAndPopulate(page, totalItemsCount, screenName);
                }
            });
            setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getUserTimeLineAndPopulate(1, COUNT_PER_PAGE, screenName);
                }
            });
        }
    }

    public static UserTimeLineFragment newInstance(String screenName) {
        UserTimeLineFragment fragment = new UserTimeLineFragment();
        Bundle bundle = new Bundle();
        bundle.putString("screenName", screenName);

        fragment.setArguments(bundle);
        return fragment;
    }

    public void getUserTimeLineAndPopulate(int page, int countPerPage, String screenName) {
        restClient.getUserTimeLine(page, countPerPage, screenName, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                addAll(Tweet.fromArrayJSON(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                JSONObjectHttpErrorHandler handler = new JSONObjectHttpErrorHandler();
                handler.handle(statusCode, headers, throwable, errorResponse, getActivity());
            }
        });
    }
}