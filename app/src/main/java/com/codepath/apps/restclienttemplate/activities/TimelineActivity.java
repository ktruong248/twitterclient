package com.codepath.apps.restclienttemplate.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
import com.codepath.apps.restclienttemplate.fragment.HomeTimeLineFragment;
import com.codepath.apps.restclienttemplate.fragment.MentionTimeLineFragment;
import com.codepath.apps.restclienttemplate.fragment.TweetsListFragment;
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

    private static final int TWEET_REQUEST_CODE = 20;
    private HomeTimeLineFragment homeTimeLineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));

        PagerSlidingTabStrip pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pagerSlidingTabStrip.setViewPager(viewPager);
        pagerSlidingTabStrip.setTextColorResource(R.color.actionbar_background);
//        if(savedInstanceState == null) {
//            homeTimeLineFragment = (HomeTimeLineFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_timeline);
//        }
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
                String tweetMsg = data.getStringExtra("tweetMsg");
                Log.i(this.getClass().getCanonicalName(), tweetMsg);
                homeTimeLineFragment.createTweetAndReload(tweetMsg);
            } else if (resultCode == RESULT_CANCELED) {
                Log.i(this.getClass().getCanonicalName(), "canceled");
            }
        }
    }

    public class TweetsPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {
        private int tabIcons[] = {R.drawable.twitter_circle_whitebird_128, R.drawable.twitter_mention_128};

        public TweetsPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getPageIconResId(int position) {
            return tabIcons[position];
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return new HomeTimeLineFragment();
            }else if (position == 1) {
                return new  MentionTimeLineFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            return tabIcons.length;
        }
    }
}
