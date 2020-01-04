package com.triangon.aruba_flora_fauna.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class FloraCategory implements Parcelable {
    private String id;
    private String name;
    private String description;
    @SerializedName("category_image")
    private ImageBundle categoryImage;

    public FloraCategory(String id, String name, String description, ImageBundle categoryImage) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categoryImage = categoryImage;
    }

    public FloraCategory() {
    }

    protected FloraCategory(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        categoryImage = in.readParcelable(ImageBundle.class.getClassLoader());
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeParcelable(categoryImage, i);
    }
}
