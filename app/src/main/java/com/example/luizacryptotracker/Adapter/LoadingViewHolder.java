package com.example.luizacryptotracker.Adapter;

import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.example.luizacryptotracker.R;

public class LoadingViewHolder extends RecyclerView.ViewHolder {

    public ProgressBar progressBar;

    public LoadingViewHolder(View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.progress_bar);
    }
}
