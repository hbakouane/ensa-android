package com.valet.app.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "club_members",
        primaryKeys = {"userId", "clubId"},
        foreignKeys = {
                @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Club.class,
                        parentColumns = "id",
                        childColumns = "clubId",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index("userId"), @Index("clubId")})
public class ClubMember {
    public long userId;
    public long clubId;
    public long joinedAt;
}
