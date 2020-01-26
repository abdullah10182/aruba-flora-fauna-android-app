package com.triangon.aruba_flora_fauna.persistence;

import com.triangon.aruba_flora_fauna.models.FloraSpecies;
import com.triangon.aruba_flora_fauna.models.ImageBundle;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface FloraSpeciesDao {

    @Insert(onConflict = IGNORE )
    long[] insertFloraSpecies(FloraSpecies... floraSpecies);

    @Insert(onConflict = REPLACE )
    void insertFloraSpecies(FloraSpecies floraSpecies);

    @Query("UPDATE flora_species set common_name = :common_name, " +
            "papiamento_name = :papiamento_name, " +
            "scientific_name = :scientific_name, " +
            "protected_locally = :protected_locally, " +
            "category_id = :category_id, " +
            "category_name = :category_name, " +
            "status_id = :status_id, " +
            "status_name = :status_name, " +
            "family = :family, " +
            "short_description = :short_description, " +
            "description = :description, " +
            "more_info_link = :more_info_link, " +
            "main_image = :main_image, " +
            "additional_images = :additional_images, " +
            "timestamp = :timestamp " +
            "WHERE id = :id")

    void updateFloraSpecies(String id,
                            String common_name,
                            String papiamento_name,
                            String scientific_name,
                            boolean protected_locally,
                            String category_id,
                            String category_name,
                            String status_id,
                            String status_name,
                            String family,
                            String short_description,
                            String description,
                            String more_info_link,
                            ImageBundle main_image,
                            List<ImageBundle> additional_images,
                            int timestamp);

    @Query("SELECT * FROM flora_species WHERE common_name LIKE '%' || :query || '%' OR description LIKE '%' || '%' " +
        "ORDER BY common_name DESC")
    LiveData<List<FloraSpecies>> searchFloraSpecies(String query);

    @Query("SELECT * FROM flora_species WHERE category_id = :category OR id = :speciesId OR common_name LIKE '%' || :searchQuery || '%' ORDER BY common_name ASC")
    LiveData<List<FloraSpecies>> getFloraSpecies(String category, String speciesId, String searchQuery);


    @Query("SELECT * from flora_species WHERE id = :id")
    LiveData<FloraSpecies> getFloraspecies(String id);

}
