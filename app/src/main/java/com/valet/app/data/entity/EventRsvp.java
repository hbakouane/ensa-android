package com.valet.app.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "event_rsvps",
        primaryKeys = {"userId", "eventId"},
        foreignKeys = {
                @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Event.class,
                        parentColumns = "id",
                        childColumns = "eventId",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index("userId"), @Index("eventId")})
public class EventRsvp {
    public long userId;
    public long eventId;
    public String status;
    public long respondedAt;
}
