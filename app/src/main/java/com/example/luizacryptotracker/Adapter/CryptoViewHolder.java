package com.example.luizacryptotracker.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.luizacryptotracker.R;

public class CryptoViewHolder extends RecyclerView.ViewHolder {

    public ImageView icon;
    public TextView symbol, name, price, oneHourChange, twentyFourHourChange, oneWeekChange;

    public CryptoViewHolder(View itemView) {
        super(itemView);
        icon = itemView.findViewById(R.id.coin_icon);
        name = itemView.findViewById(R.id.coin_name);
        symbol = itemView.findViewById(R.id.coin_symbol);
        price = itemView.findViewById(R.id.price_USD);
        oneHourChange = itemView.findViewById(R.id.one_hour);
        twentyFourHourChange = itemView.findViewById(R.id.twentyFour_hour);
        oneWeekChange = itemView.findViewById(R.id.oneWeek);
    }
}
