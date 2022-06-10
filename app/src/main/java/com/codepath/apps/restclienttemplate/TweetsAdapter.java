package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {
    Context context;
    List<Tweet> tweets;
    public String TAG = "TweetsAdapter";

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        ImageView ivTweetMedia;
        TextView tvTweetTime;
        TextView tvReplyName;
        ImageView ivFavorite;
        ImageView ivRetweet;
        TextView tvFavoriteCounter;
        TextView tvRetweetCounter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            ivTweetMedia = itemView.findViewById(R.id.ivTweetMedia);
            tvTweetTime = itemView.findViewById(R.id.tvTweetTime);
            tvReplyName = itemView.findViewById(R.id.tvReplyName);
            itemView.setOnClickListener(this);

        }

        public void bind(Tweet tweet) {
            Log.i(TAG,"hello");
            tvBody.setText(tweet.body);
            tvScreenName.setText("@" + tweet.user.screenName);
            Glide.with(context).load(tweet.user.profileImageURL).transform(new RoundedCorners(100)).into(ivProfileImage);
            if(tweet.mediaHTTPS != null){
                ivTweetMedia.setVisibility(View.VISIBLE);
                Glide.with(context).load(tweet.mediaHTTPS).into(ivTweetMedia);
            }
            else{
                ivTweetMedia.setVisibility(View.GONE);
            }

            if (tweet.replyUserName != "null") {
                Log.i(TAG, tweet.replyUserName);
                tvReplyName.setVisibility(View.VISIBLE);
                tvReplyName.setText("Replying to @" + tweet.replyUserName);
            }
            else{
                tvReplyName.setVisibility(View.INVISIBLE);
                tvReplyName.setHeight(0);
            }
            tvTweetTime.setText(tweet.createdAt);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION){
                Tweet tweet = tweets.get(position);
                Intent intent = new Intent (context, DetailActivity.class);
                intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                context.startActivity(intent);
            }
        }
    }

    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }

}
