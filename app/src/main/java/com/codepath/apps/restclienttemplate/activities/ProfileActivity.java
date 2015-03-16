package com.codepath.apps.restclienttemplate.activities;

import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.RestApplication;
import com.codepath.apps.restclienttemplate.RestClient;
import com.codepath.apps.restclienttemplate.fragment.UserTimeLineFragment;
import com.codepath.apps.restclienttemplate.listener.EndlessScrollListener;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.makeramen.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.apache.http.Header;
import org.json.JSONObject;

public class ProfileActivity extends ActionBarActivity {

    private RestClient restClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String screenName = getIntent().getStringExtra("screenName");

        if (savedInstanceState == null) {
            UserTimeLineFragment userTimeLineFragment = UserTimeLineFragment.newInstance(screenName);

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.flContainer, userTimeLineFragment);

            fragmentTransaction.commit();
        }

        restClient = RestApplication.getRestClient();
        restClient.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                User user = User.fromJSON(response); // current user
                ActionBar supportActionBar = getSupportActionBar();
                supportActionBar.setTitle(user.getScreenName());
                populateProfileHeader(user);
            }
        });
    }

    private void populateProfileHeader(User user) {
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
        TextView tagLine = (TextView) findViewById(R.id.tagLine);
        TextView tvTweetCount = (TextView) findViewById(R.id.tvTweetCount);
        TextView followers = (TextView) findViewById(R.id.tvFollowers);
        TextView following = (TextView) findViewById(R.id.tvFollowing);

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.TRANSPARENT).scaleType(ImageView.ScaleType.FIT_CENTER)
                .borderWidthDp(3)
                .cornerRadiusDp(30)
                .build();
        Picasso.with(this).load(user.getProfileImgUrl()).transform(transformation).fit().placeholder(R.drawable.ic_launcher).into(ivProfileImage);

        tvUserName.setText(user.getName());
        tagLine.setText(user.getDescription());

        tvTweetCount.setText(Html.fromHtml("<b>" + user.getTweetCount() + "</b>    TWEETS"));
        followers.setText(Html.fromHtml("<b>" + user.getFollowersCount() + "</b>    FOLLOWERS"));
        following.setText(Html.fromHtml("<b>" + user.getTweetCount() + "</b>    FOLLOWING"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
}
