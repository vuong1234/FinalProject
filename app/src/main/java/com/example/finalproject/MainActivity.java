package com.example.finalproject;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
/*import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;*/
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton btRegister;
    private TextView tvLogin;
    private Button btnLogin;
    private EditText edtUsernameLogin, edtPasswordLogin;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btRegister  = findViewById(R.id.btRegister);
        tvLogin     = findViewById(R.id.tvLogin);
        btnLogin = findViewById(R.id.btnLogin);
        edtUsernameLogin = findViewById(R.id.edtUsernameLogin);
        edtPasswordLogin= findViewById(R.id.edtPasswordLogin);


        btRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    private void loginAccount() {
        if (!validateUserame() | !validatePassword()){
            return;
        }
        else {
            isUser();
        }
    }

    private void isUser() {
        final String usernameEnter = edtUsernameLogin.getText().toString().trim();
        final String passwordEnter = edtPasswordLogin.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        Query checkUser = reference.orderByChild("username").equalTo(usernameEnter);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String passwordFromDB = dataSnapshot.child(usernameEnter).child("password").getValue(String.class);
                    if (passwordFromDB.equals(passwordEnter)){
                        /*Intent intent = new Intent(MainActivity.this,LoginSuccessful.class);*/
                        //lay username gui qua
                        Intent intent = new Intent(MainActivity.this,MainContentActivity.class);
                        intent.putExtra("username",usernameEnter);
                        startActivity(intent);
                    }
                    else {
                        edtPasswordLogin.setError("Mật khẩu không đúng ");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private Boolean validateUserame(){
        String username = edtUsernameLogin.getText().toString().trim();

        if(username.isEmpty()){
            edtUsernameLogin.setError("Tài khoản trống ");
            return false;
        }
        else {
            edtUsernameLogin.setError(null);
            return true;
        }
    }
    private boolean validatePassword(){
        String password = edtPasswordLogin.getText().toString().trim();
        if(password.isEmpty()){
            edtPasswordLogin.setError("Mật khẩu trống ");
            return false;
        }
        else {
            edtPasswordLogin.setError(null);
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btRegister:
                Intent intent   = new Intent(MainActivity.this,RegisterActivity.class);
                Pair[] pairs    = new Pair[1];
                pairs[0] = new Pair<View, String>(tvLogin,"tvLogin");
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                startActivity(intent,activityOptions.toBundle());
                break;
            case R.id.btnLogin:
                loginAccount();
                break;
        }


    }
}
