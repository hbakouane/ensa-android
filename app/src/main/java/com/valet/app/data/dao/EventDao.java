package com.valet.app.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.valet.app.data.entity.Event;
import com.valet.app.data.pojo.EventWithClub;

import java.util.List;

@Dao
public interface EventDao {
    @Insert
    long insert(Event event);

    @Query("SELECT * FROM events WHERE id = :id LIMIT 1")
    Event getById(long id);

    @Query("SELECT e.*, c.name AS clubName " +
            "FROM events e " +
            "INNER JOIN clubs c ON c.id = e.clubId " +
            "WHERE e.dateTime >= :now " +
            "ORDER BY e.dateTime ASC")
    List<EventWithClub> getUpcoming(long now);

    @Query("SELECT e.*, c.name AS clubName " +
            "FROM events e " +
            "INNER JOIN clubs c ON c.id = e.clubId " +
            "WHERE e.clubId = :clubId AND e.dateTime >= :now " +
            "ORDER BY e.dateTime ASC")
    List<EventWithClub> getUpcomingByClub(long clubId, long now);

    @Query("SELECT e.*, c.name AS clubName " +
            "FROM events e " +
            "INNER JOIN clubs c ON c.id = e.clubId " +
            "INNER JOIN club_members cm ON cm.clubId = e.clubId " +
            "WHERE cm.userId = :userId AND e.dateTime >= :now " +
            "ORDER BY e.dateTime ASC")
    List<EventWithClub> getFeedEvents(long userId, long now);

    @Query("SELECT e.*, c.name AS clubName " +
            "FROM events e " +
            "INNER JOIN clubs c ON c.id = e.clubId " +
            "WHERE e.title LIKE '%' || :query || '%' OR e.description LIKE '%' || :query || '%' " +
            "ORDER BY e.dateTime ASC")
    List<EventWithClub> search(String query);

    @Query("SELECT e.*, c.name AS clubName " +
            "FROM events e " +
            "INNER JOIN clubs c ON c.id = e.clubId " +
            "INNER JOIN event_rsvps r ON r.eventId = e.id " +
            "WHERE r.userId = :userId AND r.status = 'GOING' AND e.dateTime >= :now " +
            "ORDER BY e.dateTime ASC")
    List<EventWithClub> getUpcomingRsvps(long userId, long now);

    @Query("SELECT e.*, c.name AS clubName " +
            "FROM events e " +
            "INNER JOIN clubs c ON c.id = e.clubId " +
            "INNER JOIN bookmarks b ON b.eventId = e.id " +
            "WHERE b.userId = :userId " +
            "ORDER BY e.dateTime ASC")
    List<EventWithClub> getBookmarkedEvents(long userId);

    @Query("SELECT COUNT(*) FROM event_rsvps WHERE eventId = :eventId AND status = 'GOING'")
    int getGoingCount(long eventId);

    @Query("SELECT COUNT(*) FROM events")
    int count();
}
