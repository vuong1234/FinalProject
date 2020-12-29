package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailEmployerActivity extends AppCompatActivity {
    private ImageView imgLogoEmployer;

    private TextView txtNameEmployerDetail,txtAddressEmployerDetail,txtWelfareEmployerDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_employer);

        imgLogoEmployer = findViewById(R.id.imgLogoEmployer);
        txtNameEmployerDetail = findViewById(R.id.txtNameEmployerDetail);
        txtAddressEmployerDetail = findViewById(R.id.txtAddressEmployerDetail);
        txtWelfareEmployerDetail = findViewById(R.id.txtWelfareEmployerDetail);

        Bundle bundle = getIntent().getExtras();
        txtNameEmployerDetail.setText(bundle.getString("nameEmpoyer").toString());
        txtAddressEmployerDetail.setText(bundle.getString("addressEmpoyer"));
        txtWelfareEmployerDetail.setText(bundle.getString("welfareEmployer"));
        Picasso.get().load(bundle.getString("logoEmployer")).into(imgLogoEmployer);
    }
}
