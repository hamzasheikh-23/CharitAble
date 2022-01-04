package com.example.charitable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    Button addNewDonationBTN,manageDonationBTN,viewDonationBTN,logoutBTN;
    ImageView change_image, profile_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        addNewDonationBTN = (Button) findViewById(R.id.add_donation_btn);
        manageDonationBTN = (Button) findViewById(R.id.manage_donation_btn);
        viewDonationBTN = (Button) findViewById(R.id.view_donation_btn);
        logoutBTN = (Button) findViewById(R.id.logout_btn);

        change_image = (ImageView) findViewById(R.id.change_profile_image);
        profile_image = (ImageView) findViewById(R.id.profile_image);

        change_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(intent);            }
        });

        addNewDonationBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,AddNewDonation.class);
                startActivity(intent);
            }
        });

        viewDonationBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,ViewDonation.class);
                startActivity(intent);
            }
        });

        logoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();
                Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}