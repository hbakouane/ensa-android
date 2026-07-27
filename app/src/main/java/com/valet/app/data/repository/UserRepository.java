package com.valet.app.data.repository;

import android.content.Context;

import com.valet.app.data.db.AppDatabase;
import com.valet.app.data.dao.UserDao;
import com.valet.app.data.entity.User;
import com.valet.app.util.PasswordUtil;

import java.util.List;
import java.util.concurrent.Executors;

public class UserRepository {

    private final UserDao userDao;

    public UserRepository(Context context) {
        userDao = AppDatabase.getInstance(context).userDao();
    }

    public interface Callback<T> {
        void onResult(T result);
    }

    public void login(String email, String password, Callback<User> callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            User user = userDao.getByEmail(email);
            if (user != null && PasswordUtil.verify(password, user.passwordHash)) {
                callback.onResult(user);
            } else {
                callback.onResult(null);
            }
        });
    }

    public void signup(String name, String email, String password, Callback<Long> callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            if (userDao.countByEmail(email) > 0) {
                callback.onResult(-1L);
                return;
            }
            User user = new User();
            user.name = name;
            user.email = email;
            user.passwordHash = PasswordUtil.hash(password);
            user.createdAt = System.currentTimeMillis();
            long id = userDao.insert(user);
            callback.onResult(id);
        });
    }

    public void getById(long userId, Callback<User> callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            callback.onResult(userDao.getById(userId));
        });
    }

    public void updateProfile(User user, Callback<Boolean> callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            userDao.update(user);
            callback.onResult(true);
        });
    }

    public void searchStudents(String query, Callback<List<User>> callback) {
        Executors.newSingleThreadExecutor().execute(() ->
                callback.onResult(userDao.searchByNameOrEmail(query)));
    }
}
