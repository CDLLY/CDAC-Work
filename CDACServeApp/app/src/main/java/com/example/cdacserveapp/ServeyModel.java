package com.example.cdacserveapp;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Entity
public class ServeyModel implements Serializable {


    @PrimaryKey (autoGenerate = true)
    private int userId;

    @ColumnInfo (name = "user-name")
    private String userName;

    @ColumnInfo (name = "rating")
    private Float rating;

    @ColumnInfo (name = "servey_discription")
    private String serveyDiscription;

    public ServeyModel() {

    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public void setServeyDiscription(String serveyDiscription) {
        this.serveyDiscription = serveyDiscription;
    }

    public String getUserName() {
        return userName;
    }

    public Float getRating() {
        return rating;
    }

    public String getServeyDiscription() {
        return serveyDiscription;
    }
}
