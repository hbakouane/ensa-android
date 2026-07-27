package com.valet.app.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "bookmarks",
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
public class Bookmark {
    public long userId;
    public long eventId;
    public long createdAt;
}
