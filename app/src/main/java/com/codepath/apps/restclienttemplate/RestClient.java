package com.codepath.apps.restclienttemplate;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class RestClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
//    public static final String TIMELINE_URL = "https://api.twitter.com/1.1/statuses/home_timeline.json";
	public static final String REST_CONSUMER_KEY = "bsXb36Z4li02nGTKVOIod81Al";       // Change this
	public static final String REST_CONSUMER_SECRET = "HhfMgDMiaQ7lxY6KOQW9iPOhyy7lswOvbKVXBZw5CBLeP03RA4"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cprest"; // Change this (here and in manifest)

	public RestClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
	public void getInterestingnessList(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("format", "json");
		client.get(apiUrl, params, handler);
	}

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */

    public void getTimeLine(int page, int count, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams requestParams = new RequestParams();
        requestParams.put("count", count);
        requestParams.put("since_id", page);

        client.get(apiUrl, requestParams, handler);
    }

    public void createTweet(String tweetMsg, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams requestParams = new RequestParams();
        requestParams.put("status", tweetMsg);
        
        client.post(apiUrl, requestParams, handler);
    }

    public void getMentionsTimeLine(int page, int count, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams requestParams = new RequestParams();
        requestParams.put("count", count);
        requestParams.put("since_id", page);

        client.get(apiUrl, requestParams, handler);
    }

    public void getUserTimeLine(int page, int count,String screenName, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams requestParams = new RequestParams();
        requestParams.put("count", count);
        requestParams.put("since_id", page);
        requestParams.put("screen_name", screenName);

        client.get(apiUrl, requestParams, handler);
    }

    public void getUserInfo(String screenName, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("users/show.json");
        if(screenName == null || screenName.isEmpty()) {
            apiUrl = getApiUrl("account/verify_credentials.json");
            client.get(apiUrl, handler);
        }else {
            screenName = screenName.substring(1, screenName.length());

            RequestParams requestParams = new RequestParams();
            requestParams.put("screen_name", screenName);
            client.get(apiUrl, requestParams, handler);
        }

    }
}