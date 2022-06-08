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

import java.util.ArrayList;
import java.util.List;

@Parcel
@Entity(foreignKeys = @ForeignKey(entity= User.class, parentColumns="id", childColumns="userId"))
public class Tweet {

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

    @Ignore
    public User user;


    public Tweet(){

    }

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");

        try {
            tweet.body = tweet.body.substring(0,tweet.body.indexOf("https://t.co/"));
        }
        catch(StringIndexOutOfBoundsException e){}
        tweet.body.replaceAll("\\s+","");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.id = jsonObject.getLong("id");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.userId = tweet.user.id;
        JSONObject entities = jsonObject.getJSONObject("entities");
        Log.i("ere", String.valueOf(entities));
        try {
            JSONArray media = entities.getJSONArray("media");
            tweet.mediaHTTPS = media.getJSONObject(0).getString("media_url_https");
        }
        catch (JSONException jsonException){

        }
        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }
}
