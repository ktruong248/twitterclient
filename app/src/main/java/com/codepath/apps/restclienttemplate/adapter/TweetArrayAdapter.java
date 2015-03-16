package com.codepath.apps.restclienttemplate.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.RestApplication;
import com.codepath.apps.restclienttemplate.activities.ProfileActivity;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.makeramen.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 */
public class TweetArrayAdapter extends ArrayAdapter<Tweet> {
    private static class ViewHolder {
        public ImageView profileImageUrl;
        public TextView userName;
        public TextView screenName;
        public TextView tweetSince;
        public TextView desc;
        public TextView favouriteCount;
        
    }

    
    public TweetArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Tweet tweet = getItem(position);
        ViewHolder viewHolder;
        
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tweet_item_layout, parent, false);
            
            viewHolder = new ViewHolder();
            viewHolder.profileImageUrl = (ImageView)convertView.findViewById(R.id.profileImageUrl);
            viewHolder.userName = (TextView)convertView.findViewById(R.id.userName);
            viewHolder.screenName = (TextView)convertView.findViewById(R.id.screenName);
            viewHolder.tweetSince = (TextView)convertView.findViewById(R.id.tweetSince);
            viewHolder.desc = (TextView)convertView.findViewById(R.id.tweetDescription);
            viewHolder.favouriteCount = (TextView)convertView.findViewById(R.id.favoriteCount);
            
            convertView.setTag(viewHolder);        
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.profileImageUrl.setImageResource(0);
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.TRANSPARENT).scaleType(ImageView.ScaleType.FIT_CENTER)
                .borderWidthDp(3)
                .cornerRadiusDp(30)
                .build();
        Picasso.with(getContext()).load(tweet.getUser().getProfileImgUrl()).transform(transformation).fit().placeholder(R.drawable.ic_launcher).into(viewHolder.profileImageUrl);
        viewHolder.profileImageUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra("screenName", tweet.getUser().getScreenName());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                getContext().startActivity(intent);
            }
        });

        viewHolder.userName.setText(tweet.getUser().getName());
        viewHolder.screenName.setText(tweet.getUser().getScreenName());
        viewHolder.tweetSince.setText(tweet.getCreatedAt());
        viewHolder.desc.setText(Html.fromHtml(tweet.getBody()));

        viewHolder.favouriteCount.setText(String.valueOf(tweet.getUser().getFavouriteCount()));
        
        return convertView;
    }
}
