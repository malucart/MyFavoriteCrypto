package com.luiza.luizacryptotracker;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CryptoAdapter extends RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder> {

    public static final String TAG = "CryptoAdapter";
    private static DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private ArrayList<CryptoModel> cryptoModels;
    private Context context; // interface to global information about an application environment

    public CryptoAdapter(ArrayList<CryptoModel> cryptoModels, Context context) {
        this.cryptoModels = cryptoModels;
        this.context = context;
    }

    @NonNull
    @Override
    public CryptoAdapter.CryptoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // the idea here is to inflate the layout file that we created for our recycler view.
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

        /*
        holder.ibLike.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(holder.ibLike.getTag() == R.drawable.) {
                    unfav(position);
                    holder.ibLike.setImageResource(R.drawable.unfav_icon);
                    holder.ibLike.setTag(R.drawable.unfav_icon);
                }
                else {
                    fav(position);
                    holder.ibLike.setImageResource(R.drawable.fav_icon);
                    holder.ibLike.setTag(R.drawable.fav_icon);
                }
            }
        });

         */
    }

    /*private void fav(int position) {
        final DB_Sqlit db_sqlit = new DB_Sqlit(context);
        final String name = getDataAdapter.get(position).name;
        final String img = getDataAdapter.get(position).img;
        final String url = getDataAdapter.get(position).url;
        final String num = getDataAdapter.get(position).num;
        final String size = getDataAdapter.get(position).size;

        int count = db_sqlit.get_check_List_Favorite(name);
        if (count > 0) {
            Toast.makeText(context, "already exist", Toast.LENGTH_SHORT).show();
        }else{
            Boolean add = db_sqlit.Insert_to_favorite(name, img, url, num, size);
            if (add) {
                Toast.makeText(context, "added to favourite", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
            }

        }
    }*/

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
            //ibLike = (ImageButton) itemView.findViewById(R.id.ibLike);
        }
    }
}
