package com.valet.app.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.valet.app.data.dao.BookmarkDao;
import com.valet.app.data.dao.CategoryDao;
import com.valet.app.data.dao.ClubDao;
import com.valet.app.data.dao.ClubMemberDao;
import com.valet.app.data.dao.EventDao;
import com.valet.app.data.dao.EventRsvpDao;
import com.valet.app.data.dao.UserDao;
import com.valet.app.data.entity.Bookmark;
import com.valet.app.data.entity.Category;
import com.valet.app.data.entity.Club;
import com.valet.app.data.entity.ClubMember;
import com.valet.app.data.entity.Event;
import com.valet.app.data.entity.EventRsvp;
import com.valet.app.data.entity.User;

@Database(entities = {User.class, Category.class, Club.class, Event.class,
        ClubMember.class, EventRsvp.class, Bookmark.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract CategoryDao categoryDao();
    public abstract ClubDao clubDao();
    public abstract EventDao eventDao();
    public abstract ClubMemberDao clubMemberDao();
    public abstract EventRsvpDao eventRsvpDao();
    public abstract BookmarkDao bookmarkDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "valet_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
