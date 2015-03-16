package com.codepath.apps.restclienttemplate.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.fragment.HomeTimeLineFragment;
import com.codepath.apps.restclienttemplate.fragment.MentionTimeLineFragment;

/**
 */
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
        if (position == 0) {
            return new HomeTimeLineFragment();
        } else if (position == 1) {
            return new MentionTimeLineFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return tabIcons.length;
    }
}