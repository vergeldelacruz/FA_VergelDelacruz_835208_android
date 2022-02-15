package com.example.fa_vergeldelacruz_835208_android.util;

import androidx.room.Database;
import android.content.Context;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.fa_vergeldelacruz_835208_android.data.FavoritePlaceDao;
import com.example.fa_vergeldelacruz_835208_android.entity.FavoritePlace;

@Database(entities = {FavoritePlace.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class FavoritePlaceRoomDB extends RoomDatabase {

    public abstract FavoritePlaceDao favoritePlaceDao();
    private static final String DB_NAME = "favorite_place_db";

    private static volatile FavoritePlaceRoomDB INSTANCE;

    public static FavoritePlaceRoomDB getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), FavoritePlaceRoomDB.class, DB_NAME)
                    .allowMainThreadQueries()
                    .build();
        return INSTANCE;
    }
}