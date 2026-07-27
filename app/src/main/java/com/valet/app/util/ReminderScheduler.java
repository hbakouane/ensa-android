package com.valet.app.util;

import android.content.Context;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class ReminderScheduler {

    private static final long REMINDER_OFFSET_MS = 30 * 60 * 1000; // 30 minutes

    public static void schedule(Context context, long eventId, String eventTitle, long eventDateTime) {
        long delay = eventDateTime - System.currentTimeMillis() - REMINDER_OFFSET_MS;
        if (delay <= 0) return;

        Data inputData = new Data.Builder()
                .putLong(ReminderWorker.KEY_EVENT_ID, eventId)
                .putString(ReminderWorker.KEY_EVENT_TITLE, eventTitle)
                .build();

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(ReminderWorker.class)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(inputData)
                .addTag("event_reminder_" + eventId)
                .build();

        WorkManager.getInstance(context).enqueue(request);
    }

    public static void cancel(Context context, long eventId) {
        WorkManager.getInstance(context).cancelAllWorkByTag("event_reminder_" + eventId);
    }
}
