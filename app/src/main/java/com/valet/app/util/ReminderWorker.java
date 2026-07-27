package com.valet.app.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ReminderWorker extends Worker {

    public static final String KEY_EVENT_ID = "event_id";
    public static final String KEY_EVENT_TITLE = "event_title";

    public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        long eventId = getInputData().getLong(KEY_EVENT_ID, -1);
        String eventTitle = getInputData().getString(KEY_EVENT_TITLE);

        if (eventId == -1 || eventTitle == null) {
            return Result.failure();
        }

        NotificationHelper.showEventReminder(getApplicationContext(), eventId, eventTitle);
        return Result.success();
    }
}
