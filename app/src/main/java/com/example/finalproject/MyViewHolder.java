package com.example.finalproject;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView txtTitleRecruitment,txtAddressRecruitment;
    ImageButton btnLikeStatus;
    ItemClickListener itemClickListener;
    ImageView imgLogoRecruitment;
    View mVtem;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        txtTitleRecruitment = itemView.findViewById(R.id.txtTitleRecruitment);
        txtAddressRecruitment = itemView.findViewById(R.id.txtAddressRecruitment);
        imgLogoRecruitment = itemView.findViewById(R.id.imgLogoRecruitment);

        btnLikeStatus = itemView.findViewById(R.id.btnLikeStatus);
        itemView.setOnClickListener(this);

        mVtem = itemView;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }

    /*public void setRecruitment(Context context, String title,String address,String image){

        txtTitleRecruitment.setText(title);
        txtAddressRecruitment.setText(address);
        Picasso.get().load(image).into(imgLogoRecruitment);

    }*/
}
