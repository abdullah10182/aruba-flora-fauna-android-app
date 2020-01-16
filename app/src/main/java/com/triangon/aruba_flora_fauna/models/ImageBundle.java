package com.triangon.aruba_flora_fauna.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ImageBundle implements Parcelable {

    @SerializedName("image_large")
    private String imageLarge;
    @SerializedName("image_medium")
    private String imageMedium;
    @SerializedName("image_thumbnail")
    private String imageThumbnail;
    @SerializedName("image_small")
    private String imageSmall;
    @SerializedName("image_title")
    private String imageTitle;

    public ImageBundle(String imageLarge, String imageThumbnail, String imageMedium, String imageTitle, String imageSmall) {
        this.imageLarge = imageLarge;
        this.imageMedium = imageMedium;
        this.imageThumbnail = imageThumbnail;
        this.imageTitle = imageTitle;
        this.imageSmall = imageSmall;
    }

    public ImageBundle() {
    }

    protected ImageBundle(Parcel in) {
        imageLarge = in.readString();
        imageMedium = in.readString();
        imageThumbnail = in.readString();
        imageTitle = in.readString();
        imageSmall = in.readString();
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

    public String getImageMedium() {
        return imageMedium;
    }

    public void setImageMedium(String imageMedium) {
        this.imageMedium = imageMedium;
    }

    public String getImageSmall() {
        return imageSmall;
    }

    public void setImageSmall(String imageSmall) {
        this.imageSmall = imageSmall;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageLarge);
        parcel.writeString(imageMedium);
        parcel.writeString(imageThumbnail);
        parcel.writeString(imageTitle);
        parcel.writeString(imageSmall);
    }
}
