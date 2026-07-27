package com.valet.app.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.valet.app.data.entity.EventRsvp;

@Dao
public interface EventRsvpDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(EventRsvp rsvp);

    @Query("DELETE FROM event_rsvps WHERE userId = :userId AND eventId = :eventId")
    void delete(long userId, long eventId);

    @Query("SELECT * FROM event_rsvps WHERE userId = :userId AND eventId = :eventId LIMIT 1")
    EventRsvp get(long userId, long eventId);

    @Query("SELECT status FROM event_rsvps WHERE userId = :userId AND eventId = :eventId LIMIT 1")
    String getStatus(long userId, long eventId);
}
