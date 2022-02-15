package com.example.fa_vergeldelacruz_835208_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.fa_vergeldelacruz_835208_android.databinding.ActivityAddFavoritePlaceBinding;
import com.example.fa_vergeldelacruz_835208_android.databinding.ActivityMainBinding;
import com.example.fa_vergeldelacruz_835208_android.entity.FavoritePlace;
import com.example.fa_vergeldelacruz_835208_android.util.DateUtil;
import com.example.fa_vergeldelacruz_835208_android.util.FavoritePlaceRoomDB;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddFavoritePlaceActivity extends AppCompatActivity {

    private ActivityAddFavoritePlaceBinding binding;
    private FavoritePlaceRoomDB favoritePlaceRoomDB;
    private boolean isEditing = false;
    private int id ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddFavoritePlaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        favoritePlaceRoomDB = FavoritePlaceRoomDB.getInstance(this);

        Intent intent = getIntent();
        isEditing = intent.getBooleanExtra("isEditing",false);
        if (isEditing) {
            id = intent.getIntExtra("id",0);
            String address = intent.getStringExtra("address");
            String date = intent.getStringExtra("date");
            double latitude = intent.getDoubleExtra("latitude",0);
            double longitude = intent.getDoubleExtra("longitude",0);
            boolean visited = intent.getBooleanExtra("visited",false);
            binding.etAddress.setText(address);
            binding.etDate.setText(date);
            binding.etLatitude.setText(String.valueOf(latitude));
            binding.etLongitude.setText(String.valueOf(longitude));
            binding.cbVisited.setChecked(visited);
        }
        binding.btnAddFavoritePlace.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOrUpdateFavoritePlace();
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
    private void addOrUpdateFavoritePlace() {
        String address = binding.etAddress.getText().toString().trim();
        String date = binding.etDate.getText().toString().trim();
        String latitude =  binding.etLatitude.getText().toString().trim();
        String longitude = binding.etLongitude.getText().toString().trim();
        boolean visited = binding.cbVisited.isChecked();

        if (date.isEmpty()) {
            binding.etDate.setError("date field is empty");
            binding.etDate.requestFocus();
            return;
        }
        Date finalDate = DateUtil.stringToDate(date);
        if (finalDate == null) {
            binding.etDate.setError("date field format should be yyyy-MM-dd");
            binding.etDate.requestFocus();
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
        if (isEditing) {
            favoritePlaceRoomDB.favoritePlaceDao().updateFavoritePlace(id,
                    address,latitude,longitude,visited);
        } else {
            FavoritePlace favoritePlace = new FavoritePlace(address,finalDate,
                    Double.parseDouble(latitude),Double.parseDouble(longitude),visited);
            favoritePlaceRoomDB.favoritePlaceDao().insertFavoritePlace(favoritePlace);
        }
        startActivity(new Intent(this,MainActivity.class));
    }
}