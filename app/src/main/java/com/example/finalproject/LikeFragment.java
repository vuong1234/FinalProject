package com.example.finalproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LikeFragment extends Fragment {
    private RecyclerView recyclerViewLike;
    private MyAdapter adt;
    private List<Recruitment> dataRecruitments = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    String username;
    DatabaseReference reference  ,referenceRecruitment;
    final List<String> arrLike = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_like,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewLike = view.findViewById(R.id.recyclerLike);
        adt = new MyAdapter(getContext(),dataRecruitments,getActivity());
        recyclerViewLike.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration1 = new DividerItemDecoration(getContext(), LinearLayout.VERTICAL);
        recyclerViewLike.addItemDecoration(dividerItemDecoration1);
        recyclerViewLike.setAdapter(adt);
        //get usename
        sharedPreferences = getContext().getSharedPreferences("username", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username","");
        //get data
        reference = FirebaseDatabase.getInstance().getReference("Users/"+username);
        reference.child("Likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    arrLike.add(data.getKey());
                }
                referenceRecruitment = FirebaseDatabase.getInstance().getReference("Recruitments");
                referenceRecruitment.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()){
                            Recruitment recruitment = data.getValue(Recruitment.class);
                            if (arrLike.contains(recruitment.getKey())){
                                dataRecruitments.add(recruitment);
                            }
                        }
                        adt.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
