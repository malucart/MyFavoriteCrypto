package com.luiza.luizacryptotracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CryptoAdapter extends RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder> {

    public static final String TAG = "CryptoAdapter";
    private static DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private ArrayList<CryptoModel> cryptoModels;
    private ArrayList<Favorite> favorites;
    private Context context; // interface to global information about an application environment
    private Favorite favorite;

    public CryptoAdapter(ArrayList<CryptoModel> cryptoModels, Context context) {
        this.cryptoModels = cryptoModels;
        this.context = context;
    }

    @NonNull
    @Override
    public CryptoAdapter.CryptoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // the idea here is to inflate the layout file that we created for our recycler view
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item, parent, false);
        return new CryptoAdapter.CryptoViewHolder(view); // inflating our layout file
    }

    @Override
    public void onBindViewHolder(@NonNull CryptoViewHolder holder, int position) {
        // setting data to our item of recycler view
        CryptoModel model = cryptoModels.get(position);
        holder.tvName.setText(model.getName());
        holder.tvSymbol.setText(model.getSymbol());
        holder.tvPrice.setText("$ " + decimalFormat.format(model.getPrice()));
        holder.tvOneHour.setText(decimalFormat.format((model.getOneHour())) + "%");
        holder.tv24Hour.setText(decimalFormat.format((model.getTwentyFourHour())) + "%");
        holder.tvOneWeek.setText(decimalFormat.format((model.getOneWeek())) + "%");

        String imageUrl = model.getLogoURL();
        Glide.with(context).load(imageUrl).into(holder.ivLogo);

        // changing color for positive and negative numbers
        holder.tvOneHour.setTextColor(model.getOneHour().toString().contains("-")?
                Color.parseColor("#FF0000"):Color.parseColor("#32CD32"));
        holder.tv24Hour.setTextColor(model.getTwentyFourHour().toString().contains("-")?
                Color.parseColor("#FF0000"):Color.parseColor("#32CD32"));
        holder.tvOneWeek.setTextColor(model.getOneWeek().toString().contains("-")?
                Color.parseColor("#FF0000"):Color.parseColor("#32CD32"));

        holder.ibLike.getTag(R.drawable.ic_baseline_favorite_24);
    }

    @Override
    public int getItemCount() {
        // returning the size of the array list
        return cryptoModels.size();
    }

    // creating the view holder class that will be used to initialize each view of the layout file
    public class CryptoViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSymbol, tvName, tvPrice, tvOneHour, tv24Hour, tvOneWeek;
        private ImageView ivLogo;
        private ImageButton ibLike;

        public CryptoViewHolder(@NonNull View itemView) {
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
                    CryptoModel model = cryptoModels.get(position);

                    if (model.getFavStatus().equals("0")) {
                        model.setFavStatus("1");
                        ibLike.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                        LikedActivity.createObject();

                    } else {
                        model.setFavStatus("0");
                        ibLike.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
                        LikedActivity.deleteObject();
                    }
                }
            });
        }
    }

    private void deleteObject() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorite");

        // Retrieve the object by id
        query.getInBackground("<PARSE_OBJECT_ID>", (object, e) -> {
            if (e == null) {
                //Object was fetched
                //Deletes the fetched ParseObject from the database
                object.deleteInBackground(e2 -> {
                    if(e2==null){
                        //Toast.makeText(this, "Delete Successful", Toast.LENGTH_SHORT).show();
                    }else{
                        //Something went wrong while deleting the Object
                        //Toast.makeText(this, "Error: "+e2.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                //Something went wrong
                //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createObject() {
        ParseObject entity = new ParseObject("Favorite");

        entity.put("name", "A string");
        entity.put("symbol", "A string");
        entity.put("logoURL", "A string");
        entity.put("price", 1);
        entity.put("oneHour", 1);
        entity.put("twentyFourHour", 1);
        entity.put("oneWeek", 1);
        entity.put("user", ParseUser.getCurrentUser());
        entity.put("favStatus", "A string");

        // Saves the new object.
        // Notice that the SaveCallback is totally optional!
        entity.saveInBackground(e -> {
            if (e==null){
                // save was done
            }else{
                //Something went wrong
                //Toast.makeText(, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
