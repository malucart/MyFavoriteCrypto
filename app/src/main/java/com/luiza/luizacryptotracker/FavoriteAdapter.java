package com.luiza.luizacryptotracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavViewHolder> {

    private static final String TAG = "FavoriteAdapter";
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");
    private ArrayList<CryptoModel> cryptoFavList;
    private Context context;  // interface to global information about an application environment
    private DatabaseHandler favDB;
    public FavoriteAdapter(ArrayList<CryptoModel> cryptoFavList, Context context) {

        this.cryptoFavList = cryptoFavList;
        this.context = context;
    }

    @NonNull
    @Override
    public FavoriteAdapter.FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // the idea here is to inflate the layout file that we created for our recycler view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item, parent, false);
        favDB = new DatabaseHandler(context);
        return new FavoriteAdapter.FavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.FavViewHolder holder, int position) {
        // setting data to our item of recycler view
        CryptoModel FavoriteModel = cryptoFavList.get(position);
        holder.tvName.setText(FavoriteModel.getName());
        holder.tvSymbol.setText(FavoriteModel.getSymbol());
        holder.tvPrice.setText("xd");//"$ " + DECIMAL_FORMAT.format(FavoriteModel.getPrice()));
        holder.tvOneHour.setText("xd");//(DECIMAL_FORMAT.format((FavoriteModel.getOneHour())) + "%");
        holder.tv24Hour.setText("xd");//DECIMAL_FORMAT.format((FavoriteModel.getTwentyFourHour())) + "%");
        holder.tvOneWeek.setText("xd");//DECIMAL_FORMAT.format((FavoriteModel.getOneWeek())) + "%");

        String imageUrl = FavoriteModel.getLogoURL();
        Glide.with(context).load(imageUrl).into(holder.ivLogo);

        // changing color for positive and negative numbers
        //holder.tvOneHour.setTextColor(FavoriteModel.getOneHour().toString().contains("-")?
        //        Color.parseColor("#FF0000"):Color.parseColor("#32CD32"));
        //holder.tv24Hour.setTextColor(FavoriteModel.getTwentyFourHour().toString().contains("-")?
        //        Color.parseColor("#FF0000"):Color.parseColor("#32CD32"));
        //holder.tvOneWeek.setTextColor(FavoriteModel.getOneWeek().toString().contains("-")?
        //        Color.parseColor("#FF0000"):Color.parseColor("#32CD32"));

        holder.ibLike.getTag(R.drawable.ic_baseline_favorite_24);
    }

    @Override
    public int getItemCount() {
        // returning the size of the array list
        return cryptoFavList.size();
    }

    public class FavViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSymbol, tvName, tvPrice, tvOneHour, tv24Hour, tvOneWeek;
        private ImageView ivLogo;
        private ImageView ibLike;
        private LikedActivity likedPage = new LikedActivity();

        public FavViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing all our text views along with its ids
            tvSymbol = itemView.findViewById(R.id.tvSymbol);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvOneHour = itemView.findViewById(R.id.tvOneHour);
            tv24Hour = itemView.findViewById(R.id.tv24Hour);
            tvOneWeek = itemView.findViewById(R.id.tvOneWeek);
            ivLogo = itemView.findViewById(R.id.ivLogo);
            ibLike = itemView.findViewById(R.id.ibLike);

            ibLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getBindingAdapterPosition();
                    CryptoModel fav = cryptoFavList.get(position);

                    if (fav.getFavStatus() == true) {
                        fav.setFavStatus(false);
                        ibLike.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
                        likedPage.deleteObject();
                        // likedPage.createObject(fav.getName(), fav.getSymbol(), fav.getLogoURL(), fav.StringOf(getPrice()), fav.getOneHour(), fav.getTwentyFourHour(), fav.getOneWeek(), fav.getFavStatus());

                    } else {
                        fav.setFavStatus(true);
                        // ibLike.setBackgroundResource(R.drawable.ic_baseline_FavoriteModel_border_24);
                        // likedPage.deleteObject();
                    }
                }
            });
        }
    }
}
