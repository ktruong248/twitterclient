package com.codepath.apps.restclienttemplate.handler;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.ErrorHandler;

/**
 * Created by ktruong on 3/16/15.
 */
public class JSONObjectHttpErrorHandler implements HttpErrorHandler<JSONObject> {

    @Override
    public void handle(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse, Context context) {
        JSONArray errors;
        try {
            errors = errorResponse.getJSONArray("errors");
            if (errors != null && errors.length() > 0) {
                String message = errors.getJSONObject(0).getString("message");
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.e(this.getClass().getName(), "failed to get json errors", e);
        }
    }
}
