package com.example.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private List<Recruitment> data;
    private Activity activity ;
    private SharedPreferences sharedPreferences;
    String username;
    DatabaseReference reference  ;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference  = storage.getReference();
    final List<String> arrLikes = new ArrayList<>();

    public MyAdapter(Context context, List<Recruitment> data, Activity activity) {
        this.context = context;
        this.data = data;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView =inflater.inflate(R.layout.layout_item_recyclerview,parent,false);
        getUsernameFromSF();
        return new MyViewHolder(itemView);
    }

    private void getUsernameFromSF() {
        sharedPreferences = context.getSharedPreferences("username",Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username","");
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        reference = FirebaseDatabase.getInstance().getReference("Users/"+username);

        holder.txtTitleRecruitment.setText(data.get(position).getTitle().toString());
        holder.txtAddressRecruitment.setText(data.get(position).getAddress().toString());
        Picasso.get().load(data.get(position).getImage()).into(holder.imgLogoRecruitment);

        holder.btnLikeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.btnLikeStatus.isActivated()){
                    //remove data DB
                    reference.child("Likes").child(data.get(position).getKey()).removeValue();
                }
                else {
                    //add data FB
                    reference.child("Likes").child(data.get(position).getKey()).setValue("");
                }
                holder.btnLikeStatus.setActivated(!holder.btnLikeStatus.isActivated());
            }
        });


        //click item
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(activity,DetailRecruitmentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("titleRecruitment",data.get(position).getTitle());
                bundle.putString("addressRecruitment",data.get(position).getAddress());
                bundle.putString("salaryRecruitment",data.get(position).getSalary());
                bundle.putString("dateRecruitment",data.get(position).getDate());
                bundle.putString("reasonRecruitment",data.get(position).getReasonApply());
                //put them username tu sharedpreference
                bundle.putString("username",username);
                bundle.putString("keyRecruitment",data.get(position).getKey());
                intent.putExtras(bundle);
                activity.startActivity(intent);

            }
        });

    }

    /*private List<String> getDataLike() {
        final List<String> temp = new ArrayList<>();
        reference.child("Likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    temp.add(data.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return temp;
    }*/

    @Override
    public int getItemCount() {
        return data.size();
    }


}
