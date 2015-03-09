package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ktruong on 3/8/15.
 */
public class Utils {
    public static final String TWITTER_DT_FORMAT = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TWITTER_DT_FORMAT);

    public static Date getTwitterDate(String date) throws ParseException {
        simpleDateFormat.setLenient(true);
        return simpleDateFormat.parse(date);
    }

    public static String formatRelativeTimeSpan(String tweetDate) throws ParseException {
        Date dt = getTwitterDate(tweetDate);
        CharSequence relativeTimeSpanString = DateUtils.getRelativeTimeSpanString(dt.getTime(), System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS);

        String[] hourTokenizer = relativeTimeSpanString.toString().split(" ");
        String format = "";

        Log.i("Utils", relativeTimeSpanString.toString());
        
        if(hourTokenizer.length == 1 && hourTokenizer[0].equals("Yesterday")) {
            format = "1d";
            return format;
        }else if(hourTokenizer.length == 3) {
            return hourTokenizer[0] + hourTokenizer[1].charAt(0);
        }
        
        return relativeTimeSpanString.toString();
    }
}
