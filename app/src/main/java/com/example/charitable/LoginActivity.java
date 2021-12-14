package com.example.charitable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.charitable.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.charitable.Model.Users;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    EditText usernameEDT,passwordEDT;
    Button loginBTN,registerBTN;
    private ProgressDialog loadingBar;

    private String parentDBName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Accessing buttons from login UI
        loginBTN = (Button) findViewById(R.id.login_btn);
        registerBTN = (Button) findViewById(R.id.register_btn);

        //Accessing input fields from login UI
        usernameEDT = (EditText) findViewById(R.id.username_input);
        passwordEDT = (EditText) findViewById(R.id.password_input);

        //(External Library): Initializing Paper for locally storing login details
        Paper.init(this);

        //(External Library): Declaring progress dialog to show loading animation
        loadingBar = new ProgressDialog(this);

        //Checking if phone memory already have login info saved
        String userUsernameKey = Paper.book().read(Prevalent.userUsernameKey);
        String userPasswordKey = Paper.book().read(Prevalent.userPasswordKey);

        if (!(userUsernameKey == "" && userPasswordKey == "")) {
            if (!(TextUtils.isEmpty(userUsernameKey) && TextUtils.isEmpty(userPasswordKey))) {

                loadingBar.setTitle("Log In");
                loadingBar.setMessage("Restoring previous login session");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                allowAccessToAccount(userUsernameKey,userPasswordKey);
            }
        }

        //Login button onClick function
        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        //Register button onClick function
        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    //Function to login user
    private void loginUser() {

        //Storing data from login UI input fields to local variables
        String user = usernameEDT.getText().toString();
        String pass = passwordEDT.getText().toString();

        //Checking if input fields are empty
        if (TextUtils.isEmpty(user)) {
            Toast.makeText(LoginActivity.this, "Please enter your username", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pass)) {
            Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
        }
        else {
            //Progress Dialog until fetching data from Firebase
            loadingBar.setTitle("Log In");
            loadingBar.setMessage("Checking credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            allowAccessToAccount(user,pass);
        }
    }

    //Function to check credentials from Firebase and allowing access to user
    private void allowAccessToAccount(String user, String pass) {

        //Declaring database reference
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Checking data object in Firebase and getting value in class object
                if (snapshot.child(parentDBName).child(user).exists()) {
                    Users usersData = snapshot.child(parentDBName).child(user).getValue(Users.class);

                    //Validating username and password with Firebase data object
                    if (usersData.getUsername().equals(user)) {
                        if (usersData.getPassword().equals(pass)) {
                            Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            //Redirecting user to homepage after validating user
                            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                            startActivity(intent);

                            /* Storing login credentials to phone memory
                            so that user do not have to login everytime it opens the application */
                            Paper.book().write(Prevalent.userUsernameKey,user);
                            Paper.book().write(Prevalent.userPasswordKey,pass);
                        }
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, "Account does not exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}