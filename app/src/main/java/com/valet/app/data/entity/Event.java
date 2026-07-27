package com.valet.app.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "events",
        foreignKeys = @ForeignKey(entity = Club.class,
                parentColumns = "id",
                childColumns = "clubId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("clubId")})
public class Event {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String title;
    public String description;
    public long clubId;
    public String location;
    public long dateTime;
    public int durationMinutes;
    public long createdAt;
}
