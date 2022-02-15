package com.example.fa_vergeldelacruz_835208_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.fa_vergeldelacruz_835208_android.databinding.ActivityAddFavoritePlaceBinding;
import com.example.fa_vergeldelacruz_835208_android.databinding.ActivityMainBinding;
import com.example.fa_vergeldelacruz_835208_android.entity.FavoritePlace;
import com.example.fa_vergeldelacruz_835208_android.util.FavoritePlaceRoomDB;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddFavoritePlaceActivity extends AppCompatActivity {

    private ActivityAddFavoritePlaceBinding binding;
    private FavoritePlaceRoomDB favoritePlaceRoomDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddFavoritePlaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        favoritePlaceRoomDB = FavoritePlaceRoomDB.getInstance(this);

        binding.btnAddFavoritePlace.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFavoritePlace();
            }
        });
        binding.btnCancelAddFavoritePlace.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackToMain();
            }
        });
    }
    private void goBackToMain() {
        startActivity(new Intent(this,MainActivity.class));
    }
    private void addFavoritePlace() {
        String address = binding.etAddress.getText().toString().trim();
        String date = binding.etDate.getText().toString().trim();
        String latitude =  binding.etLatitude.getText().toString().trim();
        String longitude = binding.etLongitude.getText().toString().trim();

        if (address.isEmpty()) {
            binding.etAddress.setError("address field is empty");
            binding.etAddress.requestFocus();
            return;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (date.isEmpty()) {
            binding.etDate.setError("date field is empty");
            binding.etDate.requestFocus();
            return;
        }
        Date finalDate;
        try {
            finalDate = format.parse(date);
        } catch (ParseException e) {
            return;
        }

        if (latitude.isEmpty()) {
            binding.etLatitude.setError("latitude field is empty");
            binding.etLatitude.requestFocus();
            return;
        }
        if (latitude.isEmpty()) {
            binding.etLongitude.setError("longitude field is empty");
            binding.etLongitude.requestFocus();
            return;
        }

        FavoritePlace favoritePlace = new FavoritePlace(address,finalDate,
                Double.parseDouble(latitude),Double.parseDouble(longitude),true);

        favoritePlaceRoomDB.favoritePlaceDao().insertFavoritePlace(favoritePlace);
        startActivity(new Intent(this,MainActivity.class));
    }
}