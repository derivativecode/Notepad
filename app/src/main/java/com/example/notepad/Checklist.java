package com.example.notepad;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import java.util.ArrayList;


@Entity(tableName = "checklists")
public class Checklist extends BaseEntity implements Parcelable {

    @ColumnInfo(name = "content")
    //@TypeConverters(ListConverter.class)
    //private ArrayList<String> row;
    private ArrayList<Rows> content;

    @ColumnInfo(name = "type")
    private Boolean type;

    /*
    Constructor
     */
    public Checklist(ArrayList<Rows> content, Boolean type){
        super();
        this.content = content;
        this.type = type;
    }

    /*
    Getters & Setters
    */
    public ArrayList<Rows> getContent() {
        return content;
    }

    public void setContent(ArrayList<Rows> content) {
        this.content = content;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public int getSize(){
        return content.size();
    }

    /*
    Parcelable
    */
    protected Checklist(Parcel in){
        super(in);
        content = new ArrayList<Rows>();
        in.readList(content, Rows.class.getClassLoader());
        type = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        /*
        if (content == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(content);
        }*/
        dest.writeList(content);
        dest.writeByte((byte) (type ? 1 : 0));
    }

    public static final Parcelable.Creator<Checklist> CREATOR = new Parcelable.Creator<Checklist>() {
        @Override
        public Checklist createFromParcel(Parcel in) {
            return new Checklist(in);
        }

        @Override
        public Checklist[] newArray(int size) {
            return new Checklist[size];
        }
    };

    public int describeContents() {
        return 0;
    }
}
