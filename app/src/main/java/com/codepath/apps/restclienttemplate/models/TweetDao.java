package com.codepath.apps.restclienttemplate.models;

import static android.icu.text.MessagePattern.ArgType.SELECT;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TweetDao {

    @Query("SELECT Tweet.body as tweet_body, Tweet.createdAt as tweet_createdAt, Tweet.id as tweet_id, Tweet.mediaHTTPS as tweet_mediaHTTPS, User.* FROM Tweet INNER JOIN User ON Tweet.userId==User.id ORDER BY Tweet.createdAt DESC LIMIT 10")
    List<TweetWithUser> recentItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(Tweet... tweets);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(User... users);
}
