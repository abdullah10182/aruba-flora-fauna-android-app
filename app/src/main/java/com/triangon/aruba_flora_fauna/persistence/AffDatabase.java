package com.triangon.aruba_flora_fauna.persistence;

import android.content.Context;

import com.triangon.aruba_flora_fauna.models.FloraCategory;
import com.triangon.aruba_flora_fauna.models.FloraSpecies;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {FloraCategory.class, FloraSpecies.class}, version = 1, exportSchema = false)
@TypeConverters({Convertors.class})
public abstract class AffDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "aruba_flora_fauna_db";

    private static AffDatabase instance;

    public static AffDatabase getInstance(final Context context ) {
        if(instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AffDatabase.class,
                    DATABASE_NAME
            ).build();
        }
        return instance;
    }

    public abstract FloraCategoryDao getFloraCategoryDao();

    public abstract FloraSpeciesDao getFloraSpeciesDao();
}
