package com.valet.app.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.valet.app.data.entity.Bookmark;

@Dao
public interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Bookmark bookmark);

    @Query("DELETE FROM bookmarks WHERE userId = :userId AND eventId = :eventId")
    void delete(long userId, long eventId);

    @Query("SELECT COUNT(*) FROM bookmarks WHERE userId = :userId AND eventId = :eventId")
    int isBookmarked(long userId, long eventId);
}
