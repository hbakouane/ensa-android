package com.valet.app.data.pojo;

import androidx.room.Embedded;

import com.valet.app.data.entity.Event;

public class EventWithClub {
    @Embedded
    public Event event;
    public String clubName;

    public long getId() { return event.id; }
    public String getTitle() { return event.title; }
    public String getDescription() { return event.description; }
    public long getDateTime() { return event.dateTime; }
    public String getLocation() { return event.location; }
    public long getClubId() { return event.clubId; }
}
