package com.valet.app;

import android.app.Application;

import com.valet.app.data.db.AppDatabase;
import com.valet.app.data.db.DatabaseSeeder;

public class ValetApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppDatabase db = AppDatabase.getInstance(this);
        DatabaseSeeder.seedIfNeeded(db);
    }
}
