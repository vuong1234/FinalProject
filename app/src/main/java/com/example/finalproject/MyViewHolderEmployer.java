package com.example.finalproject;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class MyViewHolderEmployer extends RecyclerView.ViewHolder implements View.OnClickListener{
    TextView txtNameEmployer;
    ImageView imgEmployer;
    ItemClickListener itemClickListener;
    public MyViewHolderEmployer(@NonNull View itemView) {
        super(itemView);
        txtNameEmployer = itemView.findViewById(R.id.txtNameEmployer);
        imgEmployer = itemView.findViewById(R.id.imgEmployer);
        itemView.setOnClickListener(this);
    }
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
