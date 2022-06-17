package com.example.notepad;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public abstract class NoteDao extends AbstractDao<Note> {


    @Query("select * from notes")
    public abstract List<Note> getAll();

//    @Query("SELECT id, content, type, created_at, updated_at FROM notes WHERE id=:id")
//    public abstract Note get(int id);
}