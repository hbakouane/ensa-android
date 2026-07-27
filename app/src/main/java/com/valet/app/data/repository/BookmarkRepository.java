package com.valet.app.data.repository;

import android.content.Context;

import com.valet.app.data.db.AppDatabase;
import com.valet.app.data.dao.BookmarkDao;
import com.valet.app.data.dao.EventDao;
import com.valet.app.data.entity.Bookmark;
import com.valet.app.data.pojo.EventWithClub;

import java.util.List;
import java.util.concurrent.Executors;

public class BookmarkRepository {

    private final BookmarkDao bookmarkDao;
    private final EventDao eventDao;

    public interface Callback<T> {
        void onResult(T result);
    }

    public BookmarkRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        bookmarkDao = db.bookmarkDao();
        eventDao = db.eventDao();
    }

    public void isBookmarked(long userId, long eventId, Callback<Boolean> callback) {
        Executors.newSingleThreadExecutor().execute(() ->
                callback.onResult(bookmarkDao.isBookmarked(userId, eventId) > 0));
    }

    public void toggleBookmark(long userId, long eventId, Callback<Boolean> callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            if (bookmarkDao.isBookmarked(userId, eventId) > 0) {
                bookmarkDao.delete(userId, eventId);
                callback.onResult(false);
            } else {
                Bookmark b = new Bookmark();
                b.userId = userId;
                b.eventId = eventId;
                b.createdAt = System.currentTimeMillis();
                bookmarkDao.insert(b);
                callback.onResult(true);
            }
        });
    }

    public void getBookmarkedEvents(long userId, Callback<List<EventWithClub>> callback) {
        Executors.newSingleThreadExecutor().execute(() ->
                callback.onResult(eventDao.getBookmarkedEvents(userId)));
    }
}
