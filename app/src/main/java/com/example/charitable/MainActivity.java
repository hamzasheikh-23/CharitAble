package com.example.charitable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText usernameTXT,passwordTXT;
    Button loginBTN,registerBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBTN = (Button) findViewById(R.id.login_btn);
        registerBTN = (Button) findViewById(R.id.register_btn);

        usernameTXT = (EditText) findViewById(R.id.username_input);
        passwordTXT = (EditText) findViewById(R.id.password_input);

        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}