package com.luiza.luizacryptotracker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.luiza.luizacryptotracker.LikedActivity;
import com.luiza.luizacryptotracker.R;
import com.luiza.luizacryptotracker.database.DatabaseHandler;
import com.luiza.luizacryptotracker.model.CryptoModel;
import com.luiza.luizacryptotracker.model.FavoriteModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Handler;

public class CryptoAdapter extends RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder> {

    public static final String TAG = "CryptoAdapter";

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    private Context context; // interface to global information about an application environment

    private ArrayList<CryptoModel> cryptoModels = new ArrayList<CryptoModel>();

    private FavoriteModel FavoriteModel;

    private DatabaseHandler favDB;

    private Toolbar toolbar;

    public CryptoAdapter(ArrayList<CryptoModel> cryptoModels, Context context) {
        this.cryptoModels = cryptoModels;
        this.context = context;
    }

    @NonNull
    @Override
    public CryptoAdapter.CryptoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // the idea here is to inflate the layout file that we created for our recycler view
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item, parent, false);
        favDB = new DatabaseHandler(context);
        return new CryptoAdapter.CryptoViewHolder(view); // inflating our layout file
    }

    @Override
    public void onBindViewHolder(@NonNull CryptoViewHolder holder, int position) {
        // setting data to our item of recycler view
        CryptoModel model = cryptoModels.get(position);
        holder.tvName.setText(model.getName());
        holder.tvSymbol.setText(model.getSymbol());
        holder.tvPrice.setText("$ " + DECIMAL_FORMAT.format(model.getPrice()));
        holder.tvOneHour.setText(DECIMAL_FORMAT.format((model.getOneHour())) + "%");
        holder.tv24Hour.setText(DECIMAL_FORMAT.format((model.getTwentyFourHour())) + "%");
        holder.tvOneWeek.setText(DECIMAL_FORMAT.format((model.getOneWeek())) + "%");

        String imageUrl = model.getLogoURL();
        Glide.with(context).load(imageUrl).into(holder.ivLogo);

        // changing color for positive and negative numbers
        holder.tvOneHour.setTextColor(model.getOneHour().toString().contains("-")?
                Color.parseColor("#FF0000"):Color.parseColor("#32CD32"));
        holder.tv24Hour.setTextColor(model.getTwentyFourHour().toString().contains("-")?
                Color.parseColor("#FF0000"):Color.parseColor("#32CD32"));
        holder.tvOneWeek.setTextColor(model.getOneWeek().toString().contains("-")?
                Color.parseColor("#FF0000"):Color.parseColor("#32CD32"));

        holder.ibLike.getTag(R.drawable.ic_baseline_favorite_border_24);
    }

    @Override
    public int getItemCount() {
        // returning the size of the array list
        return cryptoModels.size();
    }

    // creating the view holder class that will be used to initialize each view of the layout file
    public class CryptoViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvSymbol, tvPrice, tvOneHour, tv24Hour, tvOneWeek;
        private ImageView ivLogo;
        private ImageButton ibLike;

        public CryptoViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing all our text views along with its ids
            toolbar = itemView.findViewById(R.id.mainToolbar);
            tvName = itemView.findViewById(R.id.tvName);
            tvSymbol = itemView.findViewById(R.id.tvSymbol);
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
                    CryptoModel targetModel = cryptoModels.get(position);

                    if (targetModel.getFavStatus()) {
                        // remove from the database (locally)
                        // and later (timeout or closing app) store this information remotely
                        targetModel.setFavStatus(false);
                    }
                    else {
                        // insert into favorites
                        targetModel.setFavStatus(true);
                        ibLike.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                        long id = favDB.insertDataIntoDatabase(targetModel);
                    }


                    /*
                    int position = getBindingAdapterPosition();
                    CryptoModel model = cryptoModels.get(position);

                    if (!model.getFavStatus()) { // if getFavStatus == false, which means that heart is empty
                        model.setFavStatus(true); // heart becomes full because of the click
                        ibLike.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                        likedPage.createObject(model.getName(), model.getSymbol(), model.getLogoURL(), model.getPrice(), model.getOneHour(), model.getTwentyFourHour(), model.getOneWeek(), model.getFavStatus());
                        //favDB.insertDataIntoDatabase(model.getName(), model.getSymbol(), model.getLogoURL(), model.getPrice(), model.getOneHour(), model.getTwentyFourHour(), model.getOneWeek(), model.getFavStatus());

                    } else {
                        model.setFavStatus(false);
                        ibLike.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
                        likedPage.deleteObject();
                        //favDB.removeFavorite(model.getName(), model.getSymbol(), model.getLogoURL(), model.getPrice(), model.getOneHour(), model.getTwentyFourHour(), model.getOneWeek(), model.getFavStatus());
                    }

                     */
                }
            });
        }
    }
}
