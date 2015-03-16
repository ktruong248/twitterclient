package com.codepath.apps.restclienttemplate.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.RestApplication;
import com.codepath.apps.restclienttemplate.RestClient;
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
        getUserTimeLineAndPopulate(1, COUNT_PER_PAGE);

    }

    public static UserTimeLineFragment newInstance(String screenName) {
        UserTimeLineFragment fragment = new UserTimeLineFragment();
        Bundle bundle = new Bundle();
        bundle.putString("screenName", screenName);

        fragment.setArguments(bundle);
        return fragment;
    }

    public void getUserTimeLineAndPopulate(int page, int countPerPage) {
        String screenName = getArguments().getString("screenName"); // since this is null twitter default to current user

        restClient.getUserTimeLine(countPerPage, screenName, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                addAll(Tweet.fromArrayJSON(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                JSONArray errors;
                try {
                    errors = errorResponse.getJSONArray("errors");
                    if (errors != null && errors.length() > 0) {
                        showToastText(errors.getJSONObject(0).getString("message"), Toast.LENGTH_SHORT);
                    }
                } catch (JSONException e) {
                    Log.e(this.getClass().getName(), "failed to get json errors", e);
                }
            }
        });
    }
}