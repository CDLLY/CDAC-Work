package com.example.cdacserveapp;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ServeyModel.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ServeyDao serveyDao();
}
