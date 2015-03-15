package com.codepath.apps.restclienttemplate.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.RestApplication;
import com.codepath.apps.restclienttemplate.RestClient;
import com.codepath.apps.restclienttemplate.activities.TimelineActivity;
import com.codepath.apps.restclienttemplate.adapter.TweetArrayAdapter;
import com.codepath.apps.restclienttemplate.listener.EndlessScrollListener;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TweetsListFragment extends Fragment {

    protected ArrayList<Tweet> tweets;
    protected TweetArrayAdapter tweetAdapter;
    protected SwipeRefreshLayout swipeContainer;
    private EndlessScrollListener endlessScrollListener;
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener;

    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public void setOnScrollListener(EndlessScrollListener endlessScrollListener) {
        this.endlessScrollListener = endlessScrollListener;
    }

    // inflation

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets_list, parent, false);

        ListView tweetsView = (ListView) view.findViewById(R.id.lvTweet);
        tweetsView.setAdapter(tweetAdapter);
        tweetsView.setOnScrollListener(endlessScrollListener);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(onRefreshListener);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return view;
    }


    /**
     * manage lifecyle of fragment
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<>();
        tweetAdapter = new TweetArrayAdapter(getActivity(), tweets);
    }

    public void addAll(List<Tweet> newTweets) {
        tweets.clear();
        tweetAdapter.addAll(newTweets);
        swipeContainer.setRefreshing(false);
    }

    public void showToastText(String message, int toastLength) {
        Toast.makeText(getActivity(), message, toastLength).show();
        swipeContainer.setRefreshing(false);
    }
}
