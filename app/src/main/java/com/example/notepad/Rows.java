package com.example.notepad;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;

public class Rows implements Parcelable {

    Boolean checked;
    String contentRow;

    /*
    Constructor
    */
    public Rows(Boolean checked, String contentRow) {
        //row = new Pair<Boolean, String>(checked, contentRow);
        this.checked = checked;
        this.contentRow = contentRow;
    }

    /*
    Getters & Setters
    */
    public Boolean isChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getContentRow() {
        return contentRow;
    }

    public void setContentRow(String contentRow) {
        this.contentRow = contentRow;
    }

    /*
    Parcelable
    */
    protected Rows(Parcel in) {
        checked = in.readByte() != 0;
        contentRow = in.readString();
    }

    public static final Parcelable.Creator<Rows> CREATOR = new Parcelable.Creator<Rows>() {
        @Override
        public Rows createFromParcel(Parcel in) {
            return new Rows(in);
        }

        @Override
        public Rows[] newArray(int size) {
            return new Rows[size];
        }
    };

    @Override
    public String toString() {
        return "Rows [checked=" + checked + ", content=" + contentRow + "]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (checked ? 1 : 0));
        dest.writeString(contentRow);
    }
}