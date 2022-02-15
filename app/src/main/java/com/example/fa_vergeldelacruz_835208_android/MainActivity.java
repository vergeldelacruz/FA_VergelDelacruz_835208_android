package com.example.fa_vergeldelacruz_835208_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewConfiguration;

import com.example.fa_vergeldelacruz_835208_android.adapter.FavoritePlaceAdapter;
import com.example.fa_vergeldelacruz_835208_android.databinding.ActivityMainBinding;
import com.example.fa_vergeldelacruz_835208_android.entity.FavoritePlace;
import com.example.fa_vergeldelacruz_835208_android.util.FavoritePlaceRoomDB;
import com.example.fa_vergeldelacruz_835208_android.util.ItemClickListener;
import com.example.fa_vergeldelacruz_835208_android.util.SwipeCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ItemClickListener {

    private ActivityMainBinding binding;
    private FavoritePlaceRoomDB favoritePlaceRoomDB;
    private RecyclerView rvFavoritePlaces;
    private List<FavoritePlace> favoritePlaceList;
    private FavoritePlaceAdapter favoritePlaceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        favoritePlaceRoomDB = FavoritePlaceRoomDB.getInstance(this);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFavoritePlace();
            }
        });
        loadFavoritePlaces();
        rvFavoritePlaces = binding.rvFavoritePlaces;
        rvFavoritePlaces.setLayoutManager(new LinearLayoutManager(this));
        favoritePlaceAdapter = new FavoritePlaceAdapter(this, favoritePlaceList);
        favoritePlaceAdapter.setClickListener(this);
        rvFavoritePlaces.setAdapter(favoritePlaceAdapter);
        //binding.imgBack.setOnClickListener(this);

        // Attach ItemTouchHelper to the RecyclerView
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeCallback(this,favoritePlaceAdapter));
        itemTouchHelper.attachToRecyclerView(rvFavoritePlaces);

    }

    private void addFavoritePlace() {
        Intent i = new Intent(this, AddFavoritePlaceActivity.class);
        startActivity(i);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadFavoritePlaces();
        favoritePlaceAdapter.notifyDataSetChanged();
    }

    private void loadFavoritePlaces() {
        favoritePlaceList = favoritePlaceRoomDB.favoritePlaceDao().getAllFavoritePlaces();
    }
    @Override
    public void onClick(View view) {
        Intent i = new Intent(this,FavoritePlaceMapActivity.class);
        startActivity(i);
    }


    @Override
    public void onClick(View view, int position) {
        FavoritePlace favoritePlace = favoritePlaceList.get(position);
        Intent i = new Intent(this, FavoritePlaceMapActivity.class);
        i.putExtra("address", favoritePlace.getAddress());
        i.putExtra("latitude", favoritePlace.getLatitude());
        i.putExtra("longitude", favoritePlace.getLongitude());

        startActivity(i);
    }
}