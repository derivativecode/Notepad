package com.example.notepad;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class ListConverter {
    @TypeConverter
    public static ArrayList<Rows> restoreList(String rows) {
        return new Gson().fromJson(rows, new TypeToken<ArrayList<Rows>>() {}.getType());
    }

    @TypeConverter
    public static String saveList(ArrayList<Rows> rows) {
        return new Gson().toJson(rows);
    }
}