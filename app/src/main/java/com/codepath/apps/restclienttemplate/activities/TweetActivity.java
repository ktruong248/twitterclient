package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;

public class TweetActivity extends ActionBarActivity {

    private static final int MAX_TWEET_CHARS = 140;
    private TextView tweetChars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        tweetChars = (TextView) findViewById(R.id.tweetChars);

        EditText tweetMsg = (EditText) findViewById(R.id.tweetMsg);
        tweetMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i(this.getClass().getCanonicalName(), "start " + start + " count " + count);
                updateTweetRemain(start);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i(this.getClass().getCanonicalName(), "text change start " + start + " count " + count);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void updateTweetRemain(int currentCount) {
        if (currentCount == 0) {
            tweetChars.setText("");
        } else {
            int remain = MAX_TWEET_CHARS - currentCount;

            if(remain == 0) {
                tweetChars.setText(R.string.maxTweetChars);
            }else{
                String remainText = remain + " characters remains";
                tweetChars.setText(remainText);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tweet, menu);

        MenuItem item = menu.findItem(R.id.saveTweet);

        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                EditText editText = (EditText) findViewById(R.id.tweetMsg);
                String msg = editText.getText().toString();

                Intent responseData = new Intent();
                responseData.putExtra("tweetMsg", msg);

                setResult(RESULT_OK, responseData);
                // go back
                finish();

                return true;
            }
        });

        MenuItem cancel = menu.findItem(R.id.cancelTweet);
        cancel.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                setResult(RESULT_CANCELED);
                // go back
                finish();

                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.composeTweet) {

        }
        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


}
