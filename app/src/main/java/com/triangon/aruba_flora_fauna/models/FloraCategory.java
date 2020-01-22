package com.triangon.aruba_flora_fauna.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "flora_categories")
public class FloraCategory implements Parcelable {

    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "category_image")
    @SerializedName("category_image")
    private ImageBundle categoryImage;

    @ColumnInfo(name = "timestamp")
    private int timestamp;

    public FloraCategory(@NonNull String id, String name, String description, ImageBundle categoryImage, int timestamp) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categoryImage = categoryImage;
        this.timestamp = timestamp;
    }

    public FloraCategory() {
    }

    public static final Creator<FloraCategory> CREATOR = new Creator<FloraCategory>() {
        @Override
        public FloraCategory createFromParcel(Parcel in) {
            return new FloraCategory(in);
        }

        @Override
        public FloraCategory[] newArray(int size) {
            return new FloraCategory[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ImageBundle getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(ImageBundle categoryImage) {
        this.categoryImage = categoryImage;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected FloraCategory(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        categoryImage = in.readParcelable(ImageBundle.class.getClassLoader());
        timestamp = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeParcelable(categoryImage, i);
        parcel.writeInt(timestamp);
    }

    @Override
    public String toString() {
        return "FloraCategory{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", categoryImage=" + categoryImage +
                ", timestamp=" + timestamp +
                '}';
    }
}
