package com.example.fa_vergeldelacruz_835208_android.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fa_vergeldelacruz_835208_android.entity.FavoritePlace;

import java.util.List;

@Dao
public interface FavoritePlaceDao {
    @Insert
    void insertFavoritePlace(FavoritePlace favoritePlace);

    @Query("DELETE FROM favorite_place")
    void deleteAllFavoritePlaces();

    @Query("DELETE FROM favorite_place where id = :id")
    void deleteFavoritePlace(int id);

    @Query("UPDATE favorite_place SET address = :address,  latitude = :latitude, longitude = :longitude, visited = :visited  WHERE id = :id")
    int updateFavoritePlace(int id, String address,  String latitude, String longitude, boolean visited);

    @Query("UPDATE favorite_place SET visited = :visited  WHERE id = :id")
    int updateVisited(int id,  boolean visited);

    @Query("SELECT * FROM favorite_place ORDER BY id")
    List<FavoritePlace> getAllFavoritePlaces();

}
