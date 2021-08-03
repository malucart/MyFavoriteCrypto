package com.luiza.luizacryptotracker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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
import com.like.LikeButton;
import com.like.OnAnimationEndListener;
import com.like.OnLikeListener;
import com.luiza.luizacryptotracker.LikedActivity;
import com.luiza.luizacryptotracker.R;
import com.luiza.luizacryptotracker.RequestAPI;
import com.luiza.luizacryptotracker.database.DatabaseHandler;
import com.luiza.luizacryptotracker.model.CryptoModel;
import com.luiza.luizacryptotracker.model.FavoriteModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Handler;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class CryptoAdapter extends RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder> {

    public static final String TAG = "CryptoAdapter";

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    private Context context; // interface to global information about an application environment

    private ArrayList<CryptoModel> cryptoModels = new ArrayList<CryptoModel>();

    private FavoriteModel FavoriteModel;

    private DatabaseHandler favDB;

    private Toolbar toolbar;

    private Boolean isInitianlized = false;
    // We will only need to update the remote database if we have any changes, to keep
    // a track, let's use a static boolean that will be updated by other Activity/Adapter
    private boolean isUpdateRemoteDatabase = false;

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

        // we are going to pull the data locally to save bandwidth and update the "favorite icons"
        // this happens only once at the start up, after, it happens dynamically
        if (!isInitianlized) {
            // update favorites to not have duplicates
            ArrayList<CryptoModel> favList = favDB.getFavListFromDatabase();
            for (int i = 0; i < cryptoModels.size(); i++) {
                for (int j = 0; j < favList.size(); j++) {
                    if (cryptoModels.get(i).getSymbol().equals(favList.get(j).getSymbol())) {
                        cryptoModels.get(i).setFavStatus(true);
                        cryptoModels.get(i).setObjectId(favList.get(j).getObjectId());
                        break;
                    }
                }
            }
            isInitianlized = true;
        }

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

        // check if we need to change the heart color (is favorite or not?)
        if (model.getFavStatus())
        {
            holder.ibLike.setImageResource(R.drawable.ic_baseline_favorite_24);
        }
        else
        {
            holder.ibLike.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }
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
                        targetModel.setFavStatus(false);
                        ibLike.setImageResource(R.drawable.ic_baseline_favorite_24);
                        if (!targetModel.getObjectId().equals(""))
                        {
                            deleteCryptoModelFromRemoteDatabase(targetModel.getObjectId());
                        }
                        targetModel.setObjectId("");
                        favDB.removeFavorite(targetModel.getSymbol());
                    }
                    else {
                        targetModel.setFavStatus(true);
                        ibLike.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                        insertIntoDatabase(targetModel);
                    }

                    notifyDataSetChanged();
                }
            });
        }
    }

    public void insertIntoDatabase(CryptoModel model)
    {
        ArrayList<CryptoModel> favList = favDB.getFavListFromDatabase();

        // Let's ensure that we don't have it already in the favorite list
        for (int i = 0 ; i < favList.size(); i++)
        {
            // if it's already in the list, just return and don't do anything
            if (favList.get(i).getSymbol().equals(model.getSymbol()))
            {
                return;
            }
        }

        // if we are here, there is nothing in the list, should be fine upload to remote database later
        // and update local database
        favDB.insertDataIntoDatabase(model);
        setIsRemoteUpdateDatabase(true);
    }

    public void deleteCryptoModelFromRemoteDatabase(String objectId) {
        ParseQuery<com.parse.ParseObject> query = ParseQuery.getQuery("FavoriteModel");

        // Retrieve the object by id
        query.getInBackground(objectId, (object, e) -> {
            if (e == null) {
                //Object was fetched
                //Deletes the fetched ParseObject from the database
                object.deleteInBackground(e2 -> {
                    if(e2 == null) {

                    } else {

                    }
                });
            } else {

            }
        });
    }

    public boolean getIsUpdateRemoteDatabase()
    {
        return isUpdateRemoteDatabase;
    }

    public void setIsRemoteUpdateDatabase(boolean isUpdate)
    {
        this.isUpdateRemoteDatabase = isUpdate;
    }
}
