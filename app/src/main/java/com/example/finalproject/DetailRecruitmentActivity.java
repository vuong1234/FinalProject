package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailRecruitmentActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txtSalary,txtDate,txtReason,txtTile,txtAddress;
    Button btnSave,btnApply;
    String username,keyRecruiment ;
    DatabaseReference reference  ;
    StorageReference storageReference  ;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recruitment);
        txtSalary = findViewById(R.id.txtSalary);
        txtDate = findViewById(R.id.txtDate);
        txtReason = findViewById(R.id.txtReason);
        txtTile = findViewById(R.id.txtTitleDetail);
        txtAddress = findViewById(R.id.txtAddressDetail);
        btnSave = findViewById(R.id.btnSave);
        btnApply = findViewById(R.id.btnApply);
        Bundle bundle = getIntent().getExtras();

        txtSalary.setText(bundle.getString("salaryRecruitment"));
        txtDate.setText(bundle.getString("dateRecruitment"));
        txtReason.setText(bundle.getString("reasonRecruitment"));
        txtTile.setText(bundle.getString("titleRecruitment"));
        txtAddress.setText(bundle.getString("addressRecruitment"));
        username = bundle.getString("username");
        keyRecruiment = bundle.getString("keyRecruitment");

        btnSave.setOnClickListener(this);
        btnApply.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSave:
                saveRecruitment();
                break;
            case R.id.btnApply:
                applyRecruitment();
                break;
        }
    }

    private void applyRecruitment() {
        //gui cv cua username cho Recruitment do
        reference = FirebaseDatabase.getInstance().getReference("Recruitments/"+keyRecruiment);
        storageReference = FirebaseStorage.getInstance().getReference("CV");
        showAlertDialog(R.layout.dialog_apply_recruitment);

    }

    private void saveRecruitment() {
        reference = FirebaseDatabase.getInstance().getReference("Users/"+username);
        reference.child("Likes").child(keyRecruiment).setValue("");
        showAlertDialog(R.layout.dialog_save_recruitment);
    }

    private void showAlertDialog(int layout){
        dialogBuilder = new AlertDialog.Builder(this);
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
}
