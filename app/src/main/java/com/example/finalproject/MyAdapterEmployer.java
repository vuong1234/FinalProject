package com.example.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapterEmployer extends RecyclerView.Adapter<MyViewHolderEmployer> {
    private Context context;
    private List<Employer> data;
    private Activity activity ;

    public MyAdapterEmployer(Context context, List<Employer> data,Activity activity) {
        this.context = context;
        this.data = data;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolderEmployer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView =inflater.inflate(R.layout.layout_item_employer,parent,false);

        return new MyViewHolderEmployer(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderEmployer holder, int position) {
        holder.txtNameEmployer.setText(data.get(position).getName());
        Picasso.get().load(data.get(position).getImage()).into(holder.imgEmployer);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(activity,DetailEmployerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("nameEmpoyer",data.get(position).getName());
                bundle.putString("addressEmpoyer",data.get(position).getAddress());
                bundle.putString("welfareEmployer",data.get(position).getWelfare());
                bundle.putString("logoEmployer",data.get(position).getImage());
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
