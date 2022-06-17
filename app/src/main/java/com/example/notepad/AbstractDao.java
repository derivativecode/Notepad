package com.example.notepad;

import android.util.Log;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public abstract class AbstractDao<T extends BaseEntity> {

    @Insert
    public abstract long actualInsert(T t);

    public long insert(T t) {
        t.setCreatedAt(new Date());
        t.setUpdatedAt(new Date());
        return actualInsert(t);
    }


    @Update
    public abstract void actualUpdate(T t);

    public void update(T t) {
        t.setUpdatedAt(new Date());
        actualUpdate(t);
    }


    @Delete
    public abstract void delete(T t);

    @Delete
    public abstract void deleteAll(List<T> ts);
}