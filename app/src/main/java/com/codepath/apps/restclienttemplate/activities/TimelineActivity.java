package com.codepath.apps.restclienttemplate.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.RestApplication;
import com.codepath.apps.restclienttemplate.RestClient;
import com.codepath.apps.restclienttemplate.adapter.TweetArrayAdapter;
import com.codepath.apps.restclienttemplate.listener.EndlessScrollListener;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity {

    public static final int COUNT_PER_PAGE = 25;
    private static final int TWEET_REQUEST_CODE = 20;
    private RestClient restClient;
    private ArrayList<Tweet> tweets;
    private TweetArrayAdapter tweetAdapter;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        restClient = RestApplication.getRestClient();

        ListView tweetsView = (ListView) findViewById(R.id.lvTweet);

        tweets = new ArrayList<>();

        tweetAdapter = new TweetArrayAdapter(this, tweets);
        tweetsView.setAdapter(tweetAdapter);
        tweetsView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                populateTimeLine(page, totalItemsCount);
            }
        });

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                tweets.clear();
                populateTimeLine(1, COUNT_PER_PAGE);
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        populateTimeLine(1, COUNT_PER_PAGE);
    }

    public void populateTimeLine(int page, int countPerPage) {
        restClient.getTimeLine(page, countPerPage, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, JSONArray response) {
                Log.i(this.getClass().getSimpleName(), response.toString());
                tweets.clear();
                tweetAdapter.addAll(Tweet.fromArrayJSON(response));
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int i, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i(this.getClass().getSimpleName(), "failed " + errorResponse.toString(), throwable);
                JSONArray errors = null;
                try {
                    errors = errorResponse.getJSONArray("errors");
                    if(errors != null && errors.length() > 0) {
                        Toast.makeText(TimelineActivity.this, errors.getJSONObject(0).getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e(this.getClass().getName(), "failed to get json errors", e);
                }

                swipeContainer.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.composeTweet) {
            // INTENTS
            // 1. construct the intent
            // 2. pass the bundle (query string)
            // 3. execute the intent
//            getIntent().getSerializableExtra()
            Intent intent = new Intent(this, TweetActivity.class);
            startActivityForResult(intent, TWEET_REQUEST_CODE); // fire expect result back
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TWEET_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String tweetMsg = data.getStringExtra("tweetMsg");
                Log.i(this.getClass().getCanonicalName(), tweetMsg);

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
            }else if(resultCode == RESULT_CANCELED) {
                Log.i(this.getClass().getCanonicalName(), "canceled");
            }
        }
    }
}
