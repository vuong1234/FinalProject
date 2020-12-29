package com.example.finalproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
/*import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;*/
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private RelativeLayout rlayout;
    private Animation animation;
    private FirebaseDatabase database;
    private EditText edtFullnameRegister,edtUsernameRegister,edtPasswordRegister,edtRetypePassword;
    private Button btnSignUp;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtFullnameRegister = findViewById(R.id.edtFullnameRegister);
        edtUsernameRegister = findViewById(R.id.edtUsernameRegister);
        edtPasswordRegister = findViewById(R.id.edtPasswordRegister);
        edtRetypePassword = findViewById(R.id.edtRetypePassword);
        btnSignUp = findViewById(R.id.btnSignUp);


        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rlayout = findViewById(R.id.rlayout);
        animation = AnimationUtils.loadAnimation(this,R.anim.uptodowndiagonal);
        rlayout.setAnimation(animation);
        //get instance firebaseAuth


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAccount();
            }
        });
    }

    private void registerAccount() {
        String fullname = edtFullnameRegister.getText().toString().trim();
        String username = edtUsernameRegister.getText().toString().trim();
        String password = edtPasswordRegister.getText().toString().trim();;
        String retypePassword = edtRetypePassword.getText().toString().trim();

        if(fullname.equals("")){
            edtFullnameRegister.setError("Vui long nhap ten");
        }
        else if(username.equals("")){
            edtUsernameRegister.setError("Tai khoan  bi trong");
        }
        else if(password.equals("") || password.length()<= 6){
            edtPasswordRegister.setError("Password phai lon hon 6 ki tu");
        }
        else if (!retypePassword.equals(password)){
            edtRetypePassword.setError("Password khong khop ");
        }
        else {
            Account account = new Account();
            account.setUsername(username);
            account.setPassword(password);
            account.setFullname(fullname);
            database.getInstance().getReference().child("Users").child(username).setValue(account);

           showAlertDialog(R.layout.dialog_register);

        }

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
                finish();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
