package com.valet.app.data.repository;

import android.content.Context;

import com.valet.app.data.db.AppDatabase;
import com.valet.app.data.dao.CategoryDao;
import com.valet.app.data.dao.ClubDao;
import com.valet.app.data.dao.ClubMemberDao;
import com.valet.app.data.entity.Category;
import com.valet.app.data.entity.Club;
import com.valet.app.data.entity.ClubMember;
import com.valet.app.data.pojo.ClubWithMemberCount;

import java.util.List;
import java.util.concurrent.Executors;

public class ClubRepository {

    private final ClubDao clubDao;
    private final ClubMemberDao memberDao;
    private final CategoryDao categoryDao;

    public interface Callback<T> {
        void onResult(T result);
    }

    public ClubRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        clubDao = db.clubDao();
        memberDao = db.clubMemberDao();
        categoryDao = db.categoryDao();
    }

    public void getAllClubs(Callback<List<ClubWithMemberCount>> callback) {
        Executors.newSingleThreadExecutor().execute(() ->
                callback.onResult(clubDao.getAllWithMemberCount()));
    }

    public void getClubsByCategory(long categoryId, Callback<List<ClubWithMemberCount>> callback) {
        Executors.newSingleThreadExecutor().execute(() ->
                callback.onResult(clubDao.getByCategoryWithMemberCount(categoryId)));
    }

    public void getClubById(long clubId, Callback<Club> callback) {
        Executors.newSingleThreadExecutor().execute(() ->
                callback.onResult(clubDao.getById(clubId)));
    }

    public void getCategories(Callback<List<Category>> callback) {
        Executors.newSingleThreadExecutor().execute(() ->
                callback.onResult(categoryDao.getAll()));
    }

    public void isMember(long userId, long clubId, Callback<Boolean> callback) {
        Executors.newSingleThreadExecutor().execute(() ->
                callback.onResult(memberDao.isMember(userId, clubId) > 0));
    }

    public void joinClub(long userId, long clubId, Callback<Boolean> callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            ClubMember m = new ClubMember();
            m.userId = userId;
            m.clubId = clubId;
            m.joinedAt = System.currentTimeMillis();
            memberDao.insert(m);
            callback.onResult(true);
        });
    }

    public void leaveClub(long userId, long clubId, Callback<Boolean> callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            memberDao.delete(userId, clubId);
            callback.onResult(true);
        });
    }

    public void getMemberCount(long clubId, Callback<Integer> callback) {
        Executors.newSingleThreadExecutor().execute(() ->
                callback.onResult(memberDao.getMemberCount(clubId)));
    }

    public void getJoinedClubs(long userId, Callback<List<ClubWithMemberCount>> callback) {
        Executors.newSingleThreadExecutor().execute(() ->
                callback.onResult(clubDao.getJoinedClubs(userId)));
    }

    public void searchClubs(String query, Callback<List<ClubWithMemberCount>> callback) {
        Executors.newSingleThreadExecutor().execute(() ->
                callback.onResult(clubDao.searchWithMemberCount(query)));
    }
}
