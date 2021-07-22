package com.example.luizacryptotracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class CryptoAdapter extends RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder> {

    private static DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private ArrayList<CryptoModel> cryptoModels;
    private Context context; // interface to global information about an application environment

    public CryptoAdapter(ArrayList<CryptoModel> cryptoModels, Context context) {
        this.cryptoModels = cryptoModels;
        this.context = context;
    }

    // method to filter/organize the crypto list
    public void betterList(ArrayList<CryptoModel> list) {
        // adding filtered list to the CryptoModel array list
        cryptoModels = list;
        notifyDataSetChanged();
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
        }
    }
}
