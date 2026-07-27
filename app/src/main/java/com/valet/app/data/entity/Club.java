package com.valet.app.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "clubs",
        foreignKeys = @ForeignKey(entity = Category.class,
                parentColumns = "id",
                childColumns = "categoryId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("categoryId")})
public class Club {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;
    public String description;
    public String logoUrl;
    public long categoryId;
    public long createdAt;
}
