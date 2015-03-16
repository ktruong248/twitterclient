package com.codepath.apps.restclienttemplate.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.RestApplication;
import com.codepath.apps.restclienttemplate.RestClient;
import com.codepath.apps.restclienttemplate.adapter.TweetArrayAdapter;
import com.codepath.apps.restclienttemplate.adapter.TweetsPagerAdapter;
import com.codepath.apps.restclienttemplate.fragment.HomeTimeLineFragment;
import com.codepath.apps.restclienttemplate.fragment.MentionTimeLineFragment;
import com.codepath.apps.restclienttemplate.fragment.TweetsListFragment;
import com.codepath.apps.restclienttemplate.fragment.UserTimeLineFragment;
import com.codepath.apps.restclienttemplate.listener.EndlessScrollListener;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends ActionBarActivity {

    private static final int TWEET_REQUEST_CODE = 20;
    //    private HomeTimeLineFragment homeTimeLineFragment;
    private RestClient restClient;
    private TweetsPagerAdapter tweetsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        restClient = RestApplication.getRestClient();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        tweetsPagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tweetsPagerAdapter);

        PagerSlidingTabStrip pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pagerSlidingTabStrip.setViewPager(viewPager);
        pagerSlidingTabStrip.setTextColorResource(R.color.actionbar_background);
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

    public void onProfileView(MenuItem menuItem) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TWEET_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                final String tweetMsg = data.getStringExtra("tweetMsg");
                Log.i(this.getClass().getCanonicalName(), tweetMsg);

                if (!tweetMsg.isEmpty()) {
                    restClient.createTweet(tweetMsg, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, JSONObject response) {
                            Log.i(this.getClass().getSimpleName(), response.toString());

                            Tweet createdTweet = Tweet.fromJSON(response);

                            List<Fragment> fragments = getSupportFragmentManager().getFragments();
                            for (Fragment fragment : fragments) {
                                if(fragment instanceof HomeTimeLineFragment) {
                                    HomeTimeLineFragment homeTimeLineFragment = (HomeTimeLineFragment) fragment;
                                    homeTimeLineFragment.addTweet(createdTweet);
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.i(this.getClass().getSimpleName(), "failed " + errorResponse.toString(), throwable);
                        }

                    });
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            Log.i(this.getClass().getCanonicalName(), "canceled");
        }
    }
}
