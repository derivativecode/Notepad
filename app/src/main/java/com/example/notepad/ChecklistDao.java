package com.example.notepad;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public abstract class ChecklistDao extends AbstractDao<Checklist> {


    @Query("select * from checklists")
    public abstract List<Checklist> getAll();

//    @Query("SELECT id, content, type, created_at, updated_at FROM notes WHERE id=:id")
//    public abstract Note get(int id);
}
