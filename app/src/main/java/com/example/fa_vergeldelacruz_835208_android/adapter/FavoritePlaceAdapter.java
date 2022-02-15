package com.example.fa_vergeldelacruz_835208_android.adapter;

import static android.widget.Toast.LENGTH_SHORT;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fa_vergeldelacruz_835208_android.R;
import com.example.fa_vergeldelacruz_835208_android.entity.FavoritePlace;
import com.example.fa_vergeldelacruz_835208_android.util.FavoritePlaceRoomDB;

import java.util.List;
import java.util.Locale;

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
        holder.txt_address.setText(favoritePlace.getAddress());
        //holder.img_color_change.setColorFilter(CategoryColor.getColor(context, category));
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_address;
        //ImageView ivDelete;
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
            //updateCategory(categoryList.get(getAdapterPosition()));
             if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
