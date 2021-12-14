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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameEDT,usernameEDT,passwordEDT;
    private Button signUpBTN;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Accessing input fields from register UI
        nameEDT = (EditText) findViewById(R.id.register_nameinput);
        usernameEDT = (EditText) findViewById(R.id.register_userinput);
        passwordEDT = (EditText) findViewById(R.id.register_passinput);

        //(External Library): Declaring progress dialog to show loading animation
        loadingBar = new ProgressDialog(this);

        //Accessing sign up button from register UI
        signUpBTN = (Button) findViewById(R.id.register_btn);

        //sign up button onClick function
        signUpBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

    }

    //function to create new user account
    private void createAccount() {

        //Storing data from register UI input fields to local variables
        String name = nameEDT.getText().toString();
        String user = usernameEDT.getText().toString();
        String pass = passwordEDT.getText().toString();

        //Checking for empty input fields
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(RegisterActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(user)) {
            Toast.makeText(RegisterActivity.this, "Please enter your username", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pass)) {
            Toast.makeText(RegisterActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
        }
        else {
            //Progress Dialog until fetching data from Firebase
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Checking credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            validateUsername(name,user,pass);
        }
    }

    //Function to check if username entered already exist?
    private void validateUsername(String name, String user, String pass) {

        //Declaring database reference
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                //Checking if username do not exist, then execute the code to register user
                if (!(snapshot.child("Users").child(user).exists())) {

                    //Mapping local variables
                    //HashMap is like an array but save indexes in other data type (e.g. String)
                    //Store items in "key/value" pair.
                    HashMap<String,Object> userdataMap = new HashMap<>();
                    userdataMap.put("Username",user);
                    userdataMap.put("Name",name);
                    userdataMap.put("Password",pass);

                    //Updating database where "Users" is our database and user is our data object
                    //and here we are updating our user data object with values stored in "userdataMap" on Line 103
                    RootRef.child("Users").child(user).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Congratulations! Your account has been registered", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        Toast.makeText(RegisterActivity.this, "Unexpected Error: Please try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(RegisterActivity.this, "This email: "+user+" already exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}