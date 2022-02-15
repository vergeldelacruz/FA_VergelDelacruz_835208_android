package com.example.fa_vergeldelacruz_835208_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

import com.example.fa_vergeldelacruz_835208_android.AddFavoritePlaceActivity;
import com.example.fa_vergeldelacruz_835208_android.R;
import com.example.fa_vergeldelacruz_835208_android.entity.FavoritePlace;
import com.example.fa_vergeldelacruz_835208_android.util.DateUtil;
import com.example.fa_vergeldelacruz_835208_android.util.FavoritePlaceRoomDB;
import com.example.fa_vergeldelacruz_835208_android.util.ItemClickListener;


/**
 * Adapter to display the list of favorite places in a list.
 */
public class FavoritePlaceAdapter extends RecyclerView.Adapter<FavoritePlaceAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<FavoritePlace> favoritePlaceList;
    private FavoritePlaceRoomDB favoritePlaceRoomDB;
    private Context  context;
    private ItemClickListener mClickListener;

    /**
     * Constructor for the Favorite Place Adapter.
     * The contet, layout inflator , favoritePlaceList is initialized here.
     * @param context
     * @param favoritePlaceList
     */
    public FavoritePlaceAdapter(Context context, List<FavoritePlace> favoritePlaceList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.favoritePlaceList = favoritePlaceList;
        this.context = context;
        favoritePlaceRoomDB = FavoritePlaceRoomDB.getInstance(context);
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_favorite_plaes, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull FavoritePlaceAdapter.ViewHolder holder, int position) {
        FavoritePlace favoritePlace = favoritePlaceList.get(position);
        String address = favoritePlace.getAddress();
        if (address.isEmpty()) {
            String dateString = DateUtil.convertToDateString(favoritePlace.getDate());
            holder.txt_address.setText(dateString);
        } else {
            holder.txt_address.setText(favoritePlace.getAddress());
        }
        if (!favoritePlace.isVisited()) {
            holder.img_color_visited.setColorFilter(ContextCompat.getColor(context, R.color.Red));
        }
    }

    @Override
    public int getItemCount() {
        return favoritePlaceList.size();
    }

    /**
     * Deletes the favorite place given the position
     * @param position
     */
    public void deleteItem(int position) {
        FavoritePlace favoritePlace = favoritePlaceList.get(position);
        favoritePlaceRoomDB.favoritePlaceDao().deleteFavoritePlace(favoritePlace.getId());
        favoritePlaceList.remove(position);
        notifyItemRemoved(position);
        Toast.makeText(context,"The address is deleted", Toast.LENGTH_LONG).show();
    }
    /**
     * Starts the Update Favorite Place Activity given the selected position in
     * the recycler view.
     * @param position
     */
    public void updateItem(int position) {
        FavoritePlace favoritePlace = favoritePlaceList.get(position);
        Intent i = new Intent(context, AddFavoritePlaceActivity.class);
        i.putExtra("isEditing", true);
        i.putExtra("id", favoritePlace.getId());
        i.putExtra("address", favoritePlace.getAddress());
        i.putExtra("date", DateUtil.convertToDateString(favoritePlace.getDate()));
        i.putExtra("latitude", favoritePlace.getLatitude());
        i.putExtra("longitude", favoritePlace.getLongitude());
        i.putExtra("visited", favoritePlace.isVisited());
        context.startActivity(i);
    }
    /**
     * ViewHolder class
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_address;
        ImageView img_color_visited;
        /**
         * Binds the text, image view variables and click listener.
         * @param itemView
         */
        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            txt_address = itemView.findViewById(R.id.txt_address);
            img_color_visited = itemView.findViewById(R.id.img_color_visited);
        }
        @Override
        public void onClick(View view) {
             if (mClickListener != null) mClickListener.onClick(view, getAdapterPosition());
        }
    }
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
}
