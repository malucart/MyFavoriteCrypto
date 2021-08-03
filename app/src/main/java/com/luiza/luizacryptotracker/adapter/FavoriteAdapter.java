package com.luiza.luizacryptotracker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.luiza.luizacryptotracker.LikedActivity;
import com.luiza.luizacryptotracker.R;
import com.luiza.luizacryptotracker.database.DatabaseHandler;
import com.luiza.luizacryptotracker.model.CryptoModel;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavViewHolder> {

    private static final String TAG = "FavoriteAdapter";

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    private Context context;  // interface to global information about an application environment

    private ArrayList<CryptoModel> cryptoFavList = new ArrayList<CryptoModel>();

    private DatabaseHandler favDB;

    private Toolbar toolbar;

    public Button bReddit;


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

    @SuppressLint("ResourceAsColor")
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
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{

                // X = TIME
                // Y = PRICE
                new DataPoint(0, cryptoFavList.get(position).getPrice()),
                new DataPoint(1, cryptoFavList.get(position).getPrice() + (cryptoFavList.get(position).getOneHour())/100 * cryptoFavList.get(position).getPrice()),
                new DataPoint(24, cryptoFavList.get(position).getPrice() + (cryptoFavList.get(position).getTwentyFourHour())/100 * cryptoFavList.get(position).getPrice()),
                new DataPoint(168, cryptoFavList.get(position).getPrice() + (cryptoFavList.get(position).getOneWeek())/100 * cryptoFavList.get(position).getPrice())
                /*
                new DataPoint(3, 9),
                new DataPoint(4, 6),
                new DataPoint(5, 3),
                new DataPoint(6, 6),
                new DataPoint(7, 1),
                new DataPoint(8, 2)*/

        });

        // title for our graph view
        holder.bGView.setTitle(cryptoFavList.get(position).getName());

        // setting text color to our graph view.
        holder.bGView.setTitleColor(R.color.orange);

        // setting our title text size.
        holder.bGView.setTitleTextSize(18);

        // adding data series to our graph view
        holder.bGView.addSeries(series);

        holder.bGView.setBackgroundColor(R.color.orange);
    }

    @Override
    public int getItemCount() {
        // returning the size of the array list
        return cryptoFavList.size();
    }

    public class FavViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSymbol, tvName, tvPrice, tvOneHour, tv24Hour, tvOneWeek;
        private ImageView ivLogo;
        private ImageButton ibLike;
        private Button bReddit;
        private GraphView bGView;

        public FavViewHolder(@NonNull View itemView) {
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
            bReddit = itemView.findViewById(R.id.bReddit2);
            bGView = itemView.findViewById(R.id.gvGraph);

            bReddit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    int position = getBindingAdapterPosition();
                    CryptoModel fav = cryptoFavList.get(position);
                    String url = "https://www.reddit.com/r/" + fav.getName();

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(android.net.Uri.parse(url));
                    context.startActivity(intent);
                }
            });

            ibLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                }
            });
        }
    }

    public void deleteCryptoModelFromRemoteDatabaseSlowPath(CryptoModel model)
    {
        // This is the slowest case, when there is a "de-sync" between the remote and
        // local database and we don't know the object id, so we need to pull the
        // remote database and find the target object we want to remove, this is slow
        // as we end up doing two request (one to download and another for deleting).

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("FavoriteModel");

        query.findInBackground(new com.parse.FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for(ParseObject p : list){

                    // same symbol, is our target
                    if (p.get("symbol").equals(model.getSymbol()))
                    {
                        deleteCryptoModelFromRemoteDatabase(p.getObjectId());
                        return;
                    }
                }
            }
        });
    }


    public void deleteCryptoModelFromRemoteDatabase(String objectId) {
        com.parse.ParseQuery<ParseObject> query = ParseQuery.getQuery("FavoriteModel");

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
}
