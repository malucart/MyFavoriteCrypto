package com.luiza.luizacryptotracker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.luiza.luizacryptotracker.R;
import com.luiza.luizacryptotracker.database.DatabaseHandler;
import com.luiza.luizacryptotracker.model.CryptoModel;
import com.jjoe64.graphview.GraphView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.parse.ParseQuery;

public class CryptoAdapter extends RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder> {

    public static final String TAG = "CryptoAdapter";

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    private final Context context; // interface to global information about an application environment

    private final ArrayList<CryptoModel> cryptoModels;

    private DatabaseHandler favDB;

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

    @SuppressLint("SetTextI18n")
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
        private final TextView tvName;
        private final TextView tvSymbol;
        private final TextView tvPrice;
        private final TextView tvOneHour;
        private final TextView tv24Hour;
        private final TextView tvOneWeek;
        private final ImageView ivLogo;
        private final ImageButton ibLike;

        public CryptoViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing all our text views along with its ids
            tvName = itemView.findViewById(R.id.tvName);
            tvSymbol = itemView.findViewById(R.id.tvSymbol);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvOneHour = itemView.findViewById(R.id.tvOneHour);
            tv24Hour = itemView.findViewById(R.id.tv24Hour);
            tvOneWeek = itemView.findViewById(R.id.tvOneWeek);
            ivLogo = itemView.findViewById(R.id.ivLogo);
            ibLike = itemView.findViewById(R.id.ibLike);
            Button bReddit = itemView.findViewById(R.id.bReddit2);
            GraphView bGraphView = itemView.findViewById(R.id.gvGraph);
            RelativeLayout bRelLayout = itemView.findViewById(R.id.rlCrypto);
            bReddit.setVisibility(View.GONE);
            bGraphView.setVisibility(View.GONE);
            bRelLayout.getLayoutParams().height = 288;
            bReddit.setOnClickListener(view -> { });


            ibLike.setOnClickListener(view -> {
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
                });
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
