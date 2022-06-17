package com.example.notepad;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;


@Entity(tableName = "notes")
public class Note extends BaseEntity implements Parcelable {

    @ColumnInfo(name = "content")
    private String content;

    @ColumnInfo(name = "type")
    private Boolean type;

    /*
    Constructor
     */

    public Note(String content, Boolean type){
        super();
        this.content = content;
        this.type = type;
    }



    /*
    Getters & Setters
     */
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public int getSize(){
        return content.length();
    }

    /*
    Parcelable
    */
    protected Note(Parcel in){
        super(in);
        content = in.readString();
        type = in.readByte() != 0;
        //type = in.readBoolean();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(content);
        dest.writeByte((byte) (type ? 1 : 0));
        //dest.writeBoolean(type);
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public int describeContents() {
        return 0;
    }
}