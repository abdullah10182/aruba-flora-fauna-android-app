package com.triangon.aruba_flora_fauna.persistence;

import android.content.Context;

import com.triangon.aruba_flora_fauna.models.FloraCategory;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = FloraCategory.class, version = 1)
@TypeConverters({Convertors.class})
public abstract class FloraCategoryDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "flora_category_db";

    private static FloraCategoryDatabase instance;

    public static FloraCategoryDatabase getInstance(final Context context ) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), FloraCategoryDatabase.class, DATABASE_NAME).build();
        }
        return null;
    }

    public abstract FloraCategoryDao getFloraCategoryDao();
}
