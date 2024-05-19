package com.example.myapteka.Users;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapteka.LoginActivity;
import com.example.myapteka.Prevalent.Prevalent;
import com.example.myapteka.R;
import com.example.myapteka.RegisterActivity;


public class MainActivity extends AppCompatActivity {

    private Button joinButton, loginButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinButton =(Button)findViewById(R.id.main_join_btn);
        loginButton =(Button)findViewById(R.id.main_login_btn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
    }
}
