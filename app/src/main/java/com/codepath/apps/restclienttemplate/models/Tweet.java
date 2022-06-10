package com.codepath.apps.restclienttemplate.models;

import android.provider.ContactsContract;
import android.util.AndroidException;
import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Parcel
@Entity(foreignKeys = @ForeignKey(entity= User.class, parentColumns="id", childColumns="userId"))
public class Tweet {

    private static final String TAG = "Tweet";
    @ColumnInfo
    @PrimaryKey
    public long id;

    @ColumnInfo
    public String body;
    @ColumnInfo
    public String createdAt;
    @ColumnInfo
    public String mediaHTTPS;
    @ColumnInfo
    public long userId;
    @ColumnInfo
    public Boolean favorited;
    @ColumnInfo
    public int favoriteCount;
    @ColumnInfo
    public Boolean retweeted;
    @ColumnInfo
    public int retweetCount;
    @ColumnInfo
    public long replyId;
    @ColumnInfo
    public String replyUserName;

    @Ignore
    public User user;

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public Tweet(){

    }

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        if (jsonObject.getBoolean("truncated")) {
            tweet.body = jsonObject.getString("full_text");
        }
        else{
            tweet.body = jsonObject.getString("full_text");
        }

        try {
            tweet.body = tweet.body.substring(0,tweet.body.indexOf("https://t.co/"));
        }
        catch(StringIndexOutOfBoundsException e){}

        tweet.createdAt = tweet.getRelativeTimeAgo(jsonObject.getString("created_at"));

        tweet.id = jsonObject.getLong("id");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));

        tweet.userId = tweet.user.id;
        tweet.favorited = jsonObject.getBoolean("favorited");
        tweet.favoriteCount = jsonObject.getInt("favorite_count");
        tweet.retweeted = jsonObject.getBoolean("retweeted");


        tweet.replyUserName = (jsonObject.getString("in_reply_to_screen_name"));

        JSONObject entities = jsonObject.getJSONObject("entities");
        try {
            JSONArray media = entities.getJSONArray("media");
            tweet.mediaHTTPS = media.getJSONObject(0).getString("media_url_https");
        }
        catch (JSONException jsonException){

        }
        try{
            tweet.retweetCount = jsonObject.getInt("retweet_count");
            tweet.replyId = jsonObject.getLong("in_reply_to_status_id");
        }
        catch (JSONException jsonException){}

        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        try {
            long time = sf.parse(rawJsonDate).getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " d";
            }
        } catch (ParseException e) {
            Log.i(TAG, "getRelativeTimeAgo failed");
            e.printStackTrace();
        }

        return "";
    }


}
