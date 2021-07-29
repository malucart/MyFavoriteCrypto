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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
/*
public class FavAdapter extends RecyclerView.Adapter<FavAdapter.FavViewHolder> {

    private Favorite favorite;

    private final List<Favorite> favoriteList;
    private final Context context;
    private View.OnClickListener clickListener;
    private static DecimalFormat decimalFormat = new DecimalFormat("#.##");

    public FavAdapter(Context context, List<Favorite> favoriteList, View.OnClickListener clickListener) {
        this.context = context;
        this.favoriteList = favoriteList;
        this.clickListener = clickListener;
    }

    public interface OnClickListener {
        void onPostClicked(int position);
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item, parent, false);
        return new FavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavAdapter.FavViewHolder holder, int position) {
        Favorite favorite = favoriteList.get(position);
        holder.tvName.setText(favorite.getName());
        holder.tvSymbol.setText(favorite.getSymbol());
        holder.tvPrice.setText("$ " + decimalFormat.format(favorite.getPrice()));
        holder.tvOneHour.setText(decimalFormat.format((favorite.getOneHour())) + "%");
        holder.tv24Hour.setText(decimalFormat.format((favorite.getTwentyFourHour())) + "%");
        holder.tvOneWeek.setText(decimalFormat.format((favorite.getOneWeek())) + "%");

        String imageUrl = favorite.getLogoURL();
        Glide.with(context).load(imageUrl).into(holder.ivLogo);

        // changing color for positive and negative numbers
        holder.tvOneHour.setTextColor(favorite.getOneHour().toString().contains("-")?
                Color.parseColor("#FF0000"):Color.parseColor("#32CD32"));
        holder.tv24Hour.setTextColor(favorite.getTwentyFourHour().toString().contains("-")?
                Color.parseColor("#FF0000"):Color.parseColor("#32CD32"));
        holder.tvOneWeek.setTextColor(favorite.getOneWeek().toString().contains("-")?
                Color.parseColor("#FF0000"):Color.parseColor("#32CD32"));

        holder.ibLike.getTag(R.drawable.ic_baseline_favorite_24);
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public class FavViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSymbol, tvName, tvPrice, tvOneHour, tv24Hour, tvOneWeek;
        private ImageView ivLogo;
        private ImageView ibLike;

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
                    clickListener.onPostClicked(getAdapterPosition());
                    int position = getAdapterPosition();
                    Favorite fav = favoriteList.get(position);

                }
            });
        }
    }
}
 */
