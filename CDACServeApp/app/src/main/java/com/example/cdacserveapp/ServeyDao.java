package com.example.cdacserveapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ServeyDao {

    @Query("SELECT * FROM ServeyModel")
    List<ServeyModel> getAllServeyLsit ();

    @Insert
    void insert (ServeyModel serveyModel);

    @Delete
    void delete (ServeyModel serveyModel);

}
