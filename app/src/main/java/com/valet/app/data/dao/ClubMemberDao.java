package com.valet.app.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.valet.app.data.entity.ClubMember;

@Dao
public interface ClubMemberDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ClubMember member);

    @Query("DELETE FROM club_members WHERE userId = :userId AND clubId = :clubId")
    void delete(long userId, long clubId);

    @Query("SELECT COUNT(*) FROM club_members WHERE userId = :userId AND clubId = :clubId")
    int isMember(long userId, long clubId);

    @Query("SELECT COUNT(*) FROM club_members WHERE clubId = :clubId")
    int getMemberCount(long clubId);
}
