package com.example.fa_vergeldelacruz_835208_android.entity;

import androidx.annotation.NonNull;
import androidx.room.*;

import java.util.Date;

@Entity(tableName = "favorite_place")

public class FavoritePlace {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String address;
    @NonNull
    private Date date;
    @NonNull
    private boolean visited;
    @NonNull
    private double latitude;
    @NonNull
    private double longitude;


    public FavoritePlace(@NonNull String address, @NonNull Date date, double latitude, double longitude, boolean visited ) {
        this.address = address;
        this.date = date;
        this.visited = visited;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    public void setAddress(@NonNull String address) {
        this.address = address;
    }

    @NonNull
    public Date getDate() {
        return date;
    }

    public void setDate(@NonNull Date date) {
        this.date = date;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
