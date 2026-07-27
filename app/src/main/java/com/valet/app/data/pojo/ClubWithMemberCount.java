package com.valet.app.data.pojo;

import androidx.room.Embedded;

import com.valet.app.data.entity.Club;

public class ClubWithMemberCount {
    @Embedded
    public Club club;
    public String categoryName;
    public int memberCount;

    public long getId() { return club.id; }
    public String getName() { return club.name; }
    public String getDescription() { return club.description; }
}
