package com.valet.app.data.repository;

import android.content.Context;

import com.valet.app.data.db.AppDatabase;
import com.valet.app.data.dao.EventDao;
import com.valet.app.data.dao.EventRsvpDao;
import com.valet.app.data.entity.EventRsvp;
import com.valet.app.data.pojo.EventWithClub;

import java.util.List;
import java.util.concurrent.Executors;

public class EventRepository {

    private final EventDao eventDao;
    private final EventRsvpDao rsvpDao;

    public interface Callback<T> {
        void onResult(T result);
    }

    public EventRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        eventDao = db.eventDao();
        rsvpDao = db.eventRsvpDao();
    }

    public void getUpcomingEvents(Callback<List<EventWithClub>> callback) {
        Executors.newSingleThreadExecutor().execute(() ->
                callback.onResult(eventDao.getUpcoming(System.currentTimeMillis())));
    }

    public void getUpcomingByClub(long clubId, Callback<List<EventWithClub>> callback) {
        Executors.newSingleThreadExecutor().execute(() ->
                callback.onResult(eventDao.getUpcomingByClub(clubId, System.currentTimeMillis())));
    }

    public void getFeedEvents(long userId, Callback<List<EventWithClub>> callback) {
        Executors.newSingleThreadExecutor().execute(() ->
                callback.onResult(eventDao.getFeedEvents(userId, System.currentTimeMillis())));
    }

    public void searchEvents(String query, Callback<List<EventWithClub>> callback) {
        Executors.newSingleThreadExecutor().execute(() ->
                callback.onResult(eventDao.search(query)));
    }

    public void getRsvpStatus(long userId, long eventId, Callback<String> callback) {
        Executors.newSingleThreadExecutor().execute(() ->
                callback.onResult(rsvpDao.getStatus(userId, eventId)));
    }

    public void setRsvp(long userId, long eventId, String status, Callback<Boolean> callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            EventRsvp rsvp = new EventRsvp();
            rsvp.userId = userId;
            rsvp.eventId = eventId;
            rsvp.status = status;
            rsvp.respondedAt = System.currentTimeMillis();
            rsvpDao.insert(rsvp);
            callback.onResult(true);
        });
    }

    public void removeRsvp(long userId, long eventId, Callback<Boolean> callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            rsvpDao.delete(userId, eventId);
            callback.onResult(true);
        });
    }

    public void getGoingCount(long eventId, Callback<Integer> callback) {
        Executors.newSingleThreadExecutor().execute(() ->
                callback.onResult(eventDao.getGoingCount(eventId)));
    }

    public void getUpcomingRsvps(long userId, Callback<List<EventWithClub>> callback) {
        Executors.newSingleThreadExecutor().execute(() ->
                callback.onResult(eventDao.getUpcomingRsvps(userId, System.currentTimeMillis())));
    }
}
