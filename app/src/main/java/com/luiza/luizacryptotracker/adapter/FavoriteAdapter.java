package com.luiza.luizacryptotracker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.luiza.luizacryptotracker.R;
import com.luiza.luizacryptotracker.database.DatabaseHandler;
import com.luiza.luizacryptotracker.model.CryptoModel;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavViewHolder> {

    private static final String TAG = "FavoriteAdapter";

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    private final Context context;  // interface to global information about an application environment

    private ArrayList<CryptoModel> cryptoFavList;

    private DatabaseHandler favDB;

    public FavoriteAdapter(ArrayList<CryptoModel> cryptoFavList, Context context) {
        this.cryptoFavList = cryptoFavList;
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @NonNull
    @Override
    public FavoriteAdapter.FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // the idea here is to inflate the layout file that we created for our recycler view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item, parent, false);
        favDB = new DatabaseHandler(context);
        return new FavoriteAdapter.FavViewHolder(view);
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.FavViewHolder holder, int position) {
        // setting data to our item of recycler view
        cryptoFavList = favDB.getFavListFromDatabase();
        holder.tvName.setText(cryptoFavList.get(position).getName());
        holder.tvSymbol.setText(cryptoFavList.get(position).getSymbol());
        holder.tvPrice.setText("$ " + DECIMAL_FORMAT.format(cryptoFavList.get(position).getPrice()));
        holder.tvOneHour.setText(DECIMAL_FORMAT.format(cryptoFavList.get(position).getOneHour()) + "%");
        holder.tv24Hour.setText(DECIMAL_FORMAT.format(cryptoFavList.get(position).getTwentyFourHour()) + "%");
        holder.tvOneWeek.setText(DECIMAL_FORMAT.format(cryptoFavList.get(position).getOneWeek()) + "%");

        String imageUrl = cryptoFavList.get(position).getLogoURL();
        Glide.with(context).load(imageUrl).into(holder.ivLogo);

        // changing color for positive and negative numbers
        holder.tvOneHour.setTextColor(cryptoFavList.get(position).getOneHour().toString().contains("-")?
                Color.parseColor("#FF0000"):Color.parseColor("#32CD32"));
        holder.tv24Hour.setTextColor(cryptoFavList.get(position).getTwentyFourHour().toString().contains("-")?
                Color.parseColor("#FF0000"):Color.parseColor("#32CD32"));
        holder.tvOneWeek.setTextColor(cryptoFavList.get(position).getOneWeek().toString().contains("-")?
                Color.parseColor("#FF0000"):Color.parseColor("#32CD32"));

        holder.ibLike.getTag(R.drawable.ic_baseline_favorite_24);

        // update graph with information regarding the crypto over time
        // adding data to our graph view
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {

                // X = TIME (hours)
                // Y = PRICE (usd)
                new DataPoint(0, cryptoFavList.get(position).getPrice()),
                new DataPoint(1, cryptoFavList.get(position).getPrice() + (cryptoFavList.get(position).getOneHour()) / 100 * cryptoFavList.get(position).getPrice()),
                new DataPoint(24, cryptoFavList.get(position).getPrice() + (cryptoFavList.get(position).getTwentyFourHour()) / 100 * cryptoFavList.get(position).getPrice()),
                new DataPoint(168, cryptoFavList.get(position).getPrice() + (cryptoFavList.get(position).getOneWeek()) / 100 * cryptoFavList.get(position).getPrice())

        });

        // title for our graph view
        holder.bGView.setTitle(cryptoFavList.get(position).getName());

        // setting text color to our graph view.
        holder.bGView.setTitleColor(R.color.orange);

        // setting our title text size.
        holder.bGView.setTitleTextSize(20);

        // adding data series to our graph view
        holder.bGView.addSeries(series);

        holder.bGView.setBackgroundColor(R.color.orange);

        // activate horizontal and vertical zooming and scrolling
        holder.bGView.getViewport().setScalableY(true);

        // set manual X bounds
        holder.bGView.getViewport().setXAxisBoundsManual(true);
        holder.bGView.getViewport().setMinX(0);
        holder.bGView.getViewport().setMaxX(100);
        // set manual Y bounds
        holder.bGView.getViewport().setYAxisBoundsManual(true);
        holder.bGView.getViewport().setMinY(0);
        holder.bGView.getViewport().setMaxY(40000);
    }

    @Override
    public int getItemCount() {
        // returning the size of the array list
        return cryptoFavList.size();
    }

    public class FavViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvSymbol;
        private final TextView tvName;
        private final TextView tvPrice;
        private final TextView tvOneHour;
        private final TextView tv24Hour;
        private final TextView tvOneWeek;
        private final ImageView ivLogo;
        private final ImageButton ibLike;
        private final GraphView bGView;

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public FavViewHolder(@NonNull View itemView) {
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
            bGView = itemView.findViewById(R.id.gvGraph);

            Button bReddit = itemView.findViewById(R.id.bReddit);

            bReddit.setOnClickListener(view -> {
                int position = getBindingAdapterPosition();
                CryptoModel fav = cryptoFavList.get(position);
                String url = "https://www.reddit.com/r/" + fav.getName();

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(android.net.Uri.parse(url));
                context.startActivity(intent);
            });

            ibLike.setOnClickListener(view -> {
                int position = getBindingAdapterPosition();
                CryptoModel fav = cryptoFavList.get(position);

                if (!fav.getObjectId().equals("")) {
                    deleteCryptoModelFromRemoteDatabase(fav.getObjectId());
                }
                else {
                    deleteCryptoModelFromRemoteDatabaseSlowPath(fav);
                }

                favDB.removeFavorite(fav.getSymbol());
                itemView.setVisibility(View.GONE);
                
                notifyItemRemoved(position);
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void deleteCryptoModelFromRemoteDatabaseSlowPath(CryptoModel model)
    {
        // this is the slowest case, when there is a "de-sync" between the remote and
        // local database and we don't know the object id, so we need to pull the
        // remote database and find the target object we want to remove, this is slow
        // as we end up doing two request (one to download and another for deleting)
        ParseQuery<ParseObject> query = new ParseQuery<>("FavoriteModel");

        query.findInBackground((list, e) -> {
            for(ParseObject p : list) {
                // same symbol, it's our target
                if (Objects.equals(p.get("symbol"), model.getSymbol())) {
                    deleteCryptoModelFromRemoteDatabase(p.getObjectId());
                    return;
                }
            }
        });
    }


    public void deleteCryptoModelFromRemoteDatabase(String objectId) {
        com.parse.ParseQuery<ParseObject> query = ParseQuery.getQuery("FavoriteModel");

        // retrieve the object by id
        query.getInBackground(objectId, (object, e) -> {
            if (e == null) {
                // object was fetched
                // deletes the fetched ParseObject from the database
                object.deleteInBackground(e2 -> { });
            }
        });
    }
}
