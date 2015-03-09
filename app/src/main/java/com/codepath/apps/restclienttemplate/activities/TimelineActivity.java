package com.codepath.apps.restclienttemplate.activities;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;

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
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity {

    public static final int COUNT_PER_PAGE = 25;
    private RestClient restClient;
    private ArrayList<Tweet> tweets;
    private TweetArrayAdapter tweetAdapter;

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
                // or customLoadMoreDataFromApi(totalItemsCount);
                
            }
        });
        
        populateTimeLine(1, COUNT_PER_PAGE);
    }

    public void populateTimeLine(int page, int countPerPage) {
        restClient.getTimeLine(page, countPerPage,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, JSONArray response) {
                Log.i(this.getClass().getSimpleName(), response.toString());
                tweetAdapter.addAll(Tweet.fromArrayJSON(response));
            }

            @Override
            public void onFailure(int i, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i(this.getClass().getSimpleName(), "failed " + errorResponse.toString(), throwable);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);

//        ActionBar bar = getActionBar();
//        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00aced")));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
