package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar mToolBar;
    private SharedPreferences sharedPreferences;
    private TextView txtFullnameHeader;
    private NavigationView navigationView;
    private String username;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private MyAdapter adtRecruitment1,adtRecruitment2;
    private List<Recruitment> dataRecruitment1,dataRecruitment2;
    private List<Employer> dataEmployer;
    private RecyclerView recyclerViewRecommend,recyclerViewFeatured, recyclerViewInterested;
    private MyAdapterEmployer adtEmployer;
    private CircularImageView imgUsername;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference  = storage.getReference();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*De hien nut home */
        setHasOptionsMenu(true);

        return inflater.inflate(R.layout.fragment_home,container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpNavigationviewAndToolbar(view);
        setFullnameHeader();
        setUpAvatar();

        recyclerViewRecommend = view.findViewById(R.id.recyclerRecommend);
        recyclerViewFeatured = view.findViewById(R.id.recyclerFeatured);
        recyclerViewInterested = view.findViewById(R.id.recyclerInterested);

        dataRecruitment1 = new ArrayList<>();
        dataRecruitment2 = new ArrayList<>();
        recyclerViewRecommend.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration1 = new DividerItemDecoration(getContext(),LinearLayout.VERTICAL);
        recyclerViewRecommend.addItemDecoration(dividerItemDecoration1);
        database= FirebaseDatabase.getInstance();
        reference = database.getReference("Recruitments");
        //get data from DB
        database.getReference("Recruitments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 1;
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Recruitment recruitment = data.getValue(Recruitment.class);
                    if (i%2 ==0 ){
                        dataRecruitment1.add(recruitment);
                    }
                    else {
                        dataRecruitment2.add(recruitment);
                    }
                    i++;

                }
                adtRecruitment1 = new MyAdapter(getView().getContext(),dataRecruitment1,(AppCompatActivity) getActivity());
                recyclerViewRecommend.setLayoutManager(new LinearLayoutManager(getContext()));
                DividerItemDecoration dividerItemDecoration1 = new DividerItemDecoration(getContext(),LinearLayout.VERTICAL);
                recyclerViewRecommend.addItemDecoration(dividerItemDecoration1);
                recyclerViewRecommend.setAdapter(adtRecruitment1);

                adtRecruitment2 = new MyAdapter(getView().getContext(),dataRecruitment2,(AppCompatActivity) getActivity());
                recyclerViewInterested.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerViewInterested.addItemDecoration(dividerItemDecoration1);
                recyclerViewInterested.setAdapter(adtRecruitment2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        dataEmployer = new ArrayList<>();
        database.getReference("Employers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Employer employer = data.getValue(Employer.class);
                    dataEmployer.add(employer);
                }
                adtEmployer = new MyAdapterEmployer(getView().getContext(),dataEmployer,getActivity());
                recyclerViewFeatured.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
                recyclerViewFeatured.setAdapter(adtEmployer);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private void setFullnameHeader(){
        sharedPreferences = this.getActivity().getSharedPreferences("username",Context.MODE_PRIVATE);

        username = sharedPreferences.getString("username","");
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");
        Query checkUser = reference.orderByChild("username").equalTo(username);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String fullname = dataSnapshot.child(username).child("fullname").getValue(String.class);

                txtFullnameHeader.setText(fullname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setUpNavigationviewAndToolbar(View view){
        mToolBar = (Toolbar) view.findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer);
        mToolBar.setTitle("FindJob");
        //lay navigation view -> lay headerview -> moi set textview duoc
        navigationView = (NavigationView) view.findViewById(R.id.navigationView);

        View headerView = navigationView.getHeaderView(0);
        txtFullnameHeader = headerView.findViewById(R.id.txtFullnameHeader);
        imgUsername = headerView.findViewById(R.id.imgUsername);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolBar);
        toggle = new ActionBarDrawerToggle(getActivity(),drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)){
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpAvatar() {
        storageReference.child("images/"+username).getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Use the bytes to display the image
                // convert byte array to bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                imgUsername.setImageBitmap(Bitmap.createScaledBitmap(bitmap,imgUsername.getWidth(),imgUsername.getHeight(),false));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors

            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        return true;
    }
}
