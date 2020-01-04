package com.triangon.aruba_flora_fauna.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ImageBundle implements Parcelable {

    @SerializedName("image_large")
    private String imageLarge;
    @SerializedName("image_thumbnail")
    private String imageThumbnail;
    @SerializedName("image_title")
    private String imageTitle;

    public ImageBundle(String imageLarge, String imageThumbnail, String imageTitle) {
        this.imageLarge = imageLarge;
        this.imageThumbnail = imageThumbnail;
        this.imageTitle = imageTitle;
    }

    public ImageBundle() {
    }

    protected ImageBundle(Parcel in) {
        imageLarge = in.readString();
        imageThumbnail = in.readString();
        imageTitle = in.readString();
    }

    public static final Creator<ImageBundle> CREATOR = new Creator<ImageBundle>() {
        @Override
        public ImageBundle createFromParcel(Parcel in) {
            return new ImageBundle(in);
        }

        @Override
        public ImageBundle[] newArray(int size) {
            return new ImageBundle[size];
        }
    };

    public String getImageLarge() {
        return imageLarge;
    }

    public void setImageLarge(String imageLarge) {
        this.imageLarge = imageLarge;
    }

    public String getImageThumbnail() {
        return imageThumbnail;
    }

    public void setImageThumbnail(String imageThumbnail) {
        this.imageThumbnail = imageThumbnail;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageLarge);
        parcel.writeString(imageThumbnail);
        parcel.writeString(imageTitle);
    }
}
