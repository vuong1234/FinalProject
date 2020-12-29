package com.example.finalproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;
import java.util.UUID;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private TextView txtNameProfile,txtStatusCV;
    private SharedPreferences sharedPreferences;
    private Button btnUpdateProfile;
    private EditText edtEmailProfile,edtPhoneProfile,edtWorkProfile,edtExperienceProfile;
    private CardView cvUploadCV;
    private FirebaseDatabase database;
    private String username ;
    private DatabaseReference reference;
    private CircularImageView imageAvatar;
    private final int PICK_IMAGE_REQUEST = 22;
    private final int PICK_CV_REQUEST = 24;
    private Uri filePathImage,filePathCV;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference  = storage.getReference();
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        mapping(view);

        sharedPreferences = getActivity().getSharedPreferences("username", Context.MODE_PRIVATE);
        //lay username = key
        username = sharedPreferences.getString("username","");
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");
        Query checkUser = reference.orderByChild("username").equalTo(username);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String fullname = dataSnapshot.child(username).child("fullname").getValue(String.class);
                String email = dataSnapshot.child(username).child("email").getValue(String.class);
                String phone = dataSnapshot.child(username).child("phone").getValue(String.class);
                String job = dataSnapshot.child(username).child("job").getValue(String.class);
                String experience = dataSnapshot.child(username).child("experience").getValue(String.class);
                txtNameProfile.setText(fullname);
                edtEmailProfile.setText(email);
                edtPhoneProfile.setText(phone);
                edtWorkProfile.setText(job);
                edtExperienceProfile.setText(experience);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        setUpAvatar();
        return view;
    }


    private void setUpAvatar() {
        storageReference.child("images/"+username).getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Use the bytes to display the image
                // convert byte array to bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                imageAvatar.setImageBitmap(Bitmap.createScaledBitmap(bitmap,imageAvatar.getWidth(),imageAvatar.getHeight(),false));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors

            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnUpdateProfile.setOnClickListener(this);

        imageAvatar.setOnClickListener(this);

        cvUploadCV.setOnClickListener(this);

    }
    private void mapping(View view){
        txtNameProfile = view.findViewById(R.id.txtNameProfile);
        btnUpdateProfile = view.findViewById(R.id.btnUpdateProfile);
        edtEmailProfile = view.findViewById(R.id.edtEmailProfile);
        edtPhoneProfile = view.findViewById(R.id.edtPhoneProfile);
        edtWorkProfile = view.findViewById(R.id.edtWorkProfile);
        edtExperienceProfile = view.findViewById(R.id.edtExperienceProfile);
        imageAvatar = view.findViewById(R.id.imgAvatarProfile);
        cvUploadCV = view.findViewById(R.id.cvUploadCV);
        txtStatusCV = view.findViewById(R.id.txtStatusCV);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnUpdateProfile:
                updateProfile();
                break;
            case R.id.imgAvatarProfile:
                selectAvatar();
                break;
            case R.id.cvUploadCV:
                selectCV();
                break;
        }

    }

    private void selectCV() {
        //select file from mobile
        Intent intent = new Intent();
        intent.setType("application/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Vui lòng chon CV ..."),
                PICK_CV_REQUEST);
    }

    private void selectAvatar() {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Vui lòng chon anh..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == Activity.RESULT_OK
                && data != null
                && data.getData() != null){
            // Get the Uri of data
            filePathImage = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(getActivity().getContentResolver(), filePathImage);
                imageAvatar.setImageBitmap(bitmap);
                
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }

        }
        else if (requestCode == PICK_CV_REQUEST
                && resultCode == Activity.RESULT_OK
                && data != null
                && data.getData() != null){
            filePathCV = data.getData();
            Toast.makeText(getContext(),"Upload CV",Toast.LENGTH_SHORT).show();
        }
    }

    private void updateProfile() {
        if (edtEmailProfile.getText().toString().equals("")){
            edtEmailProfile.setError("Field is empty ");
        }
        else if (edtPhoneProfile.getText().toString().equals("")){
            edtPhoneProfile.setError("Field is empty ");
        }
        else if (edtWorkProfile.getText().toString().equals("")){
            edtWorkProfile.setError("Field is empty ");
        }
        else if (edtExperienceProfile.getText().toString().equals("")){
            edtExperienceProfile.setError("Field is empty ");
        }
        else {
            reference.child(username).child("email").setValue(edtEmailProfile.getText().toString());
            reference.child(username).child("phone").setValue(edtPhoneProfile.getText().toString());
            reference.child(username).child("job").setValue(edtWorkProfile.getText().toString());
            reference.child(username).child("experience").setValue(edtExperienceProfile.getText().toString());
            uploadAvatar();
            uploadCV();

            showAlertDialog(R.layout.dialog_update);

        }
    }
    private void showAlertDialog(int layout){
        dialogBuilder = new AlertDialog.Builder(getContext());
        View layoutView = getLayoutInflater().inflate(layout, null);
        Button btnOkDialog = layoutView.findViewById(R.id.btnOkDialog);
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.show();
        btnOkDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    private void uploadCV() {
        if (filePathCV!= null){
            // Code for showing progressDialog while uploading

            StorageReference ref = storageReference.child("CV/" + username.toString());


            ref.putFile(filePathCV).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                }
            });
        }
    }

    private void uploadAvatar() {
        if (filePathImage!= null){
            // Code for showing progressDialog while uploading

            StorageReference ref = storageReference.child("images/" + username.toString());
            // adding listeners on upload
            // or failure of image

            ref.putFile(filePathImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                }
            });
        }
    }

}
