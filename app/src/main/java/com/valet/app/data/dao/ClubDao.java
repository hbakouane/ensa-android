package com.valet.app.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.valet.app.data.entity.Club;
import com.valet.app.data.pojo.ClubWithMemberCount;

import java.util.List;

@Dao
public interface ClubDao {
    @Insert
    long insert(Club club);

    @Query("SELECT * FROM clubs WHERE id = :id LIMIT 1")
    Club getById(long id);

    @Query("SELECT c.*, cat.name AS categoryName, " +
            "(SELECT COUNT(*) FROM club_members cm WHERE cm.clubId = c.id) AS memberCount " +
            "FROM clubs c " +
            "INNER JOIN categories cat ON cat.id = c.categoryId " +
            "ORDER BY c.name ASC")
    List<ClubWithMemberCount> getAllWithMemberCount();

    @Query("SELECT c.*, cat.name AS categoryName, " +
            "(SELECT COUNT(*) FROM club_members cm WHERE cm.clubId = c.id) AS memberCount " +
            "FROM clubs c " +
            "INNER JOIN categories cat ON cat.id = c.categoryId " +
            "WHERE c.categoryId = :categoryId " +
            "ORDER BY c.name ASC")
    List<ClubWithMemberCount> getByCategoryWithMemberCount(long categoryId);

    @Query("SELECT c.*, cat.name AS categoryName, " +
            "(SELECT COUNT(*) FROM club_members cm WHERE cm.clubId = c.id) AS memberCount " +
            "FROM clubs c " +
            "INNER JOIN categories cat ON cat.id = c.categoryId " +
            "WHERE c.name LIKE '%' || :query || '%' OR c.description LIKE '%' || :query || '%' " +
            "ORDER BY c.name ASC")
    List<ClubWithMemberCount> searchWithMemberCount(String query);

    @Query("SELECT c.*, cat.name AS categoryName, " +
            "(SELECT COUNT(*) FROM club_members cm WHERE cm.clubId = c.id) AS memberCount " +
            "FROM clubs c " +
            "INNER JOIN categories cat ON cat.id = c.categoryId " +
            "INNER JOIN club_members cm2 ON cm2.clubId = c.id " +
            "WHERE cm2.userId = :userId " +
            "ORDER BY c.name ASC")
    List<ClubWithMemberCount> getJoinedClubs(long userId);

    @Query("SELECT COUNT(*) FROM clubs")
    int count();
}
