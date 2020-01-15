package com.triangon.aruba_flora_fauna.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FloraSpecies implements Parcelable {

    private String id;

    @SerializedName("common_name")
    private String commonName;

    @SerializedName("papiamento_name")
    private String papiamentoName;

    @SerializedName("scientific_name")
    private String scientificName;

    @SerializedName("protected_locally")
    private boolean protectedLocally;

    @SerializedName("category_id")
    private String categoryId;

    @SerializedName("category_name")
    private String categoryName;

    @SerializedName("status_id")
    private String statusId;

    @SerializedName("status_name")
    private String statusName;

    private String family;

    @SerializedName("short_description")
    private String shortDescription;

    private String description;

    @SerializedName("more_info_link")
    private String moreInfoLink;

    @SerializedName("main_image")
    private ImageBundle mainImage;

    @SerializedName("additional_images")
    private List<ImageBundle> additionalImages;

    public FloraSpecies(String id, String commonName, String papiamentoName, String scientificName, boolean protectedLocally, String categoryId, String categoryName, String statusId, String statusName, String family, String shortDescription, String description, String moreInfoLink, ImageBundle mainImage, List<ImageBundle> additionalImages) {
        this.id = id;
        this.commonName = commonName;
        this.papiamentoName = papiamentoName;
        this.scientificName = scientificName;
        this.protectedLocally = protectedLocally;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.statusId = statusId;
        this.statusName = statusName;
        this.family = family;
        this.shortDescription = shortDescription;
        this.description = description;
        this.moreInfoLink = moreInfoLink;
        this.mainImage = mainImage;
        this.additionalImages = additionalImages;
    }

    public FloraSpecies() {
    }

    protected FloraSpecies(Parcel in) {
        id = in.readString();
        commonName = in.readString();
        papiamentoName = in.readString();
        scientificName = in.readString();
        protectedLocally = in.readByte() != 0;
        categoryId = in.readString();
        categoryName = in.readString();
        statusId = in.readString();
        statusName = in.readString();
        family = in.readString();
        shortDescription = in.readString();
        description = in.readString();
        moreInfoLink = in.readString();
        mainImage = in.readParcelable(ImageBundle.class.getClassLoader());
        additionalImages = in.createTypedArrayList(ImageBundle.CREATOR);
    }

    public static final Creator<FloraSpecies> CREATOR = new Creator<FloraSpecies>() {
        @Override
        public FloraSpecies createFromParcel(Parcel in) {
            return new FloraSpecies(in);
        }

        @Override
        public FloraSpecies[] newArray(int size) {
            return new FloraSpecies[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getPapiamentoName() {
        return papiamentoName;
    }

    public void setPapiamentoName(String papiamentoName) {
        this.papiamentoName = papiamentoName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public boolean isProtectedLocally() {
        return protectedLocally;
    }

    public void setProtectedLocally(boolean protectedLocally) {
        this.protectedLocally = protectedLocally;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMoreInfoLink() {
        return moreInfoLink;
    }

    public void setMoreInfoLink(String moreInfoLink) {
        this.moreInfoLink = moreInfoLink;
    }

    public ImageBundle getMainImage() {
        return mainImage;
    }

    public void setMainImage(ImageBundle mainImage) {
        this.mainImage = mainImage;
    }

    public List<ImageBundle> getAdditionalImages() {
        return additionalImages;
    }

    public void setAdditionalImages(List<ImageBundle> additionalImages) {
        this.additionalImages = additionalImages;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    @Override
    public String toString() {
        return "FloraSpecies{" +
                "id='" + id + '\'' +
                ", commonName='" + commonName + '\'' +
                ", papiamentoName='" + papiamentoName + '\'' +
                ", scientificName='" + scientificName + '\'' +
                ", protectedLocally=" + protectedLocally +
                ", categoryId='" + categoryId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", statusId='" + statusId + '\'' +
                ", statusName='" + statusName + '\'' +
                ", family='" + family + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                ", moreInfoLink='" + moreInfoLink + '\'' +
                ", mainImage=" + mainImage +
                ", additionalImages=" + additionalImages +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(commonName);
        parcel.writeString(papiamentoName);
        parcel.writeString(scientificName);
        parcel.writeByte((byte) (protectedLocally ? 1 : 0));
        parcel.writeString(categoryId);
        parcel.writeString(categoryName);
        parcel.writeString(statusId);
        parcel.writeString(statusName);
        parcel.writeString(family);
        parcel.writeString(shortDescription);
        parcel.writeString(description);
        parcel.writeString(moreInfoLink);
        parcel.writeParcelable(mainImage, i);
        parcel.writeTypedList(additionalImages);
    }
}
