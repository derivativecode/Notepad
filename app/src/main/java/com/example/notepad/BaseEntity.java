package com.example.notepad;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

public abstract class BaseEntity implements Parcelable {

    String TAG = "BaseEntity";

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    @TypeConverters(DateConverter.class)
    private Date createdAt;

    @ColumnInfo(name = "updated_at", defaultValue = "CURRENT_TIMESTAMP")
    @TypeConverters(DateConverter.class)
    private Date updatedAt;

    /*
    Empty Constructor, since objects inferred from BaseEntity will be created with their own
     */
    public BaseEntity(){
    }


    /*
    Getter & Setter
    */

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }


    @Override
    public boolean equals(Object o) {
        return false;
//        if (this == o) return true;
//        if (!(o instanceof Note)) return false;
//
//        Note note = (Note) o;
//
//        if (note_id != note.note_id) return false;
//        return Objects.equals(type, note.type);
    }


//    @Override
//    public int hashCode() {
//        int result = note_id;
//        result = 31 * result + (type != null ? type.hashCode() : 0);
//        return result;
//    }

//    @Override
//    public String toString() {
//        return "Note{" +
//                "note_id=" + note_id +
//                ", content='" + content + '\'' +
//                ", type='" + type + '\'' +
//                ", createdAt='" + createdAt + '\'' +
//                ", updatedAt='" + updatedAt + '\'' +
//                '}';
//    }


    /*
    Parcelable
    */

    protected BaseEntity(Parcel in) {
        id = in.readLong();
        createdAt = new Date(in.readLong());
        updatedAt = new Date(in.readLong());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(createdAt.getTime());
        dest.writeLong(updatedAt.getTime());
    }


//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    protected void Note(Parcel in) {
//        id = in.readInt();
//        //content = in.readString();
//        //type = in.readString();
//        createdAt = new Date(in.readLong());
//        updatedAt = new Date(in.readLong());
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeInt(id);
//        //parcel.writeString(content);
//        ///parcel.writeString(type);
//        parcel.writeLong(createdAt.getTime());
//        parcel.writeLong(updatedAt.getTime());
//    }
//
//    public static final Creator<Note> CREATOR = new Creator<Note>() {
//        @Override
//        public Note createFromParcel(Parcel in) {
//            return new Note(in.readString(), in.readString());
//        }
//
//        @Override
//        public Note[] newArray(int size) {
//            return new Note[size];
//        }
//    };
}