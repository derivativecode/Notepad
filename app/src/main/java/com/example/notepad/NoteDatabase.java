package com.example.notepad;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


@Database(entities = { Note.class, Checklist.class }, version = 2)
@TypeConverters(ListConverter.class)
public abstract class NoteDatabase extends RoomDatabase {

    public abstract NoteDao getNoteDao();

    public abstract ChecklistDao getChecklistDao();

    //public abstract  SuperDao getSuperDao();

    private static NoteDatabase noteDB;

    public static NoteDatabase getInstance(Context context) {
        if (null == noteDB) {
            noteDB = buildDatabaseInstance(context);
        }
        return noteDB;
    }

    private static NoteDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                NoteDatabase.class, "noteDB")
                .allowMainThreadQueries().build();
    }

    public void cleanUp(){
        noteDB = null;
    }
}