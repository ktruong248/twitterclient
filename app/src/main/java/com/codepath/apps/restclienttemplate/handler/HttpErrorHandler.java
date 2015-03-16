package com.codepath.apps.restclienttemplate.handler;

import android.content.Context;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by ktruong on 3/16/15.
 */
public interface HttpErrorHandler<T> {
    void handle(int statusCode, Header[] headers, Throwable throwable, T errorResponse, Context context);
}
