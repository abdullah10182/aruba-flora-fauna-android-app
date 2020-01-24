package com.triangon.aruba_flora_fauna.persistence;

import com.triangon.aruba_flora_fauna.models.FloraCategory;
import com.triangon.aruba_flora_fauna.models.ImageBundle;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface FloraCategoryDao {

    @Insert(onConflict = IGNORE )
    long[] insertFloraCategories(FloraCategory... floraCategories);

    @Insert(onConflict = REPLACE )
    void insertFloraCategories(FloraCategory floraCategories);

    @Query("UPDATE flora_categories set name = :name, description = :description, category_image = :category_image " +
            "WHERE id = :id")
    void updateFloraCategory(String id, String name, String description, ImageBundle category_image);

//    @Query("SELECT * FROM flora_categories WHERE name LIKE '%' || :query || '%' OR description LIKE '%' || '%' " +
//        "ORDER BY name DESC")
//    LiveData<List<FloraCategory>> searchFloraCategories(String query);

    @Query("SELECT * FROM flora_categories ORDER BY weight ASC")
    LiveData<List<FloraCategory>> getFloraCategories();


    @Query("SELECT * from flora_categories WHERE id = :id")
    LiveData<FloraCategory> getFloraCategory(String id);

}
