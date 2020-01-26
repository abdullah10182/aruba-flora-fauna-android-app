package com.triangon.aruba_flora_fauna.persistence;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.triangon.aruba_flora_fauna.models.ImageBundle;

import java.lang.reflect.Type;
import java.util.List;

import androidx.room.TypeConverter;

public class Convertors {

    @TypeConverter
    public static String[] fromString(String value) {
        Type listType = new TypeToken<String[]>(){}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(String[] list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static ImageBundle imageBundleFromString(String value) {
        Type listType = new TypeToken<ImageBundle>(){}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromImageBundle(ImageBundle imageBundle) {
        Gson gson = new Gson();
        String json = gson.toJson(imageBundle);
        return json;
    }

    @TypeConverter
    public static List<ImageBundle> imageBundleListFromString(String value) {
        Type listType = new TypeToken<List<ImageBundle>>(){}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromImageBundleList(List<ImageBundle> imageBundleList) {
        Gson gson = new Gson();
        String json = gson.toJson(imageBundleList);
        return json;
    }
}
