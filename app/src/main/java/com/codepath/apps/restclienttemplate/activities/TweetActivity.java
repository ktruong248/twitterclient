package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.apps.restclienttemplate.R;

public class TweetActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
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

        if(id == R.id.composeTweet) {
            
        }
        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
