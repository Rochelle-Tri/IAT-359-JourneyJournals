package com.example.journeyjournals;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class PhotoModel implements Parcelable {
    private Bitmap photo;

    public PhotoModel(Bitmap photo) {
        this.photo = photo;
    }

    protected PhotoModel(Parcel in) {
        // read data from Parcel and populate your object
    }

    public static final Parcelable.Creator<PhotoModel> CREATOR = new Parcelable.Creator<PhotoModel>() {
        @Override
        public PhotoModel createFromParcel(Parcel in) {
            return new PhotoModel(in);
        }

        @Override
        public PhotoModel[] newArray(int size) {
            return new PhotoModel[size];
        }
    };

    public Bitmap getPhoto() {
        return photo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // write data to Parcel
    }
}


