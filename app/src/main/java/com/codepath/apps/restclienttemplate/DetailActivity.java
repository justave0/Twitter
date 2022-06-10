package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class DetailActivity extends AppCompatActivity {
    Tweet tweet;
    TwitterClient client;
    public final String TAG = "DetailActivity";

    TextView tvDetailScreenName;
    TextView tvDetailBody;
    ImageView ivDetailProfile;
    ImageView ivDetailMedia;
    ImageView ivDetailLike;
    ImageView ivDetailReply;
    ImageView ivDetailRetweet;
    TextView tvFavoriteCount;
    TextView tvRetweetCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        client = TwitterApp.getRestClient(this);
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        tvDetailBody = (TextView) findViewById(R.id.tvDetailBody);
        tvDetailScreenName = (TextView) findViewById(R.id.tvDetailName);
        ivDetailProfile = (ImageView) findViewById(R.id.ivDetailProfile);
        ivDetailMedia = (ImageView) findViewById(R.id.ivDetailMedia);
        ivDetailLike = (ImageView) findViewById(R.id.ivDetailLike);
        ivDetailReply = (ImageView) findViewById(R.id.ivDetailReply);
        ivDetailRetweet = (ImageView) findViewById(R.id.ivDetailRetweet);
        tvFavoriteCount = (TextView) findViewById(R.id.tvFavoriteCount);
        tvRetweetCount = (TextView) findViewById(R.id.tvRetweetCount);

        tvDetailScreenName.setText(tweet.user.screenName);
        tvDetailBody.setText(tweet.body);
        tvFavoriteCount.setText(String.valueOf(tweet.favoriteCount) + " Likes");
        tvRetweetCount.setText(String.valueOf(tweet.retweetCount) + " Retweets");
        Glide.with(DetailActivity.this).load(tweet.user.profileImageURL).into(ivDetailProfile);
        if(tweet.mediaHTTPS != null){
            ivDetailMedia.setVisibility(View.VISIBLE);
            Glide.with(DetailActivity.this).load(tweet.mediaHTTPS).into(ivDetailMedia);
        }
        else{
            ivDetailMedia.setVisibility(View.GONE);
        }
        Glide.with(DetailActivity.this).load(R.drawable.ic_vector_retweet_stroke)
                .override(90,90)
                .into(ivDetailRetweet);

        Log.i(TAG, tweet.body);
        checkLikedTweet(tweet);
        checkRetweeted(tweet);



        ivDetailLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tweet.favorited){
                client.postLike(tweet.id, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        tweet.favorited = true;
                        Log.i(TAG, tweet.body);
                        checkLikedTweet(tweet);
                        tweet.favoriteCount += 1;
                        tvFavoriteCount.setText(String.valueOf(tweet.favoriteCount) + " Likes");
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.i(TAG, String.valueOf(tweet.id));
                        Log.e(TAG, "Failure to like" + response, throwable);
                    }
                });}
                else{
                    client.removeLike(tweet.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            tweet.favorited = false;
                            Log.i(TAG, tweet.body);
                            checkLikedTweet(tweet);
                            tweet.favoriteCount -= 1;
                            tvFavoriteCount.setText(String.valueOf(tweet.favoriteCount) + " Likes");
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.i(TAG, String.valueOf(tweet.id));
                            Log.e(TAG, "Failure to unlike" + response, throwable);
                        }
                    });
                }
            }
        });

        ivDetailRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tweet.retweeted){
                    client.postRetweet(tweet.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            tweet.retweeted = true;
                            Log.i(TAG, tweet.body);
                            checkRetweeted(tweet);
                            tweet.retweetCount += 1;
                            tvRetweetCount.setText(String.valueOf(tweet.retweetCount) + " Retweets");
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.i(TAG, String.valueOf(tweet.id));
                            Log.e(TAG, "Failure to retweet" + response, throwable);
                        }
                    });}
                else{
                    client.removeRetweet(tweet.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            tweet.retweeted = false;
                            Log.i(TAG, tweet.body);
                            checkRetweeted(tweet);
                            tweet.retweetCount -= 1;
                            tvRetweetCount.setText(String.valueOf(tweet.retweetCount) + " Retweets");
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.i(TAG, String.valueOf(tweet.id));
                            Log.e(TAG, "Failure to unretweet" + response, throwable);
                        }
                    });
                }
            }
        });
    }

    private void checkLikedTweet(Tweet tweet) {

        if (tweet.favorited){
            Glide.with(DetailActivity.this).load(R.drawable.ic_vector_heart)
                    .into(ivDetailLike);
        }
        else{
            Glide.with(DetailActivity.this).load(R.drawable.ic_vector_heart_stroke)
                    .into(ivDetailLike);
        }
    }
    private void checkRetweeted(Tweet tweet) {

        if (tweet.retweeted){
            Glide.with(DetailActivity.this).load(R.drawable.ic_vector_retweet)
                    .into(ivDetailRetweet);
        }
        else{
            Glide.with(DetailActivity.this).load(R.drawable.ic_vector_retweet_stroke)
                    .into(ivDetailRetweet);
        }
    }
}
