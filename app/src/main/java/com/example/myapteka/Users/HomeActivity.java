package com.example.myapteka.Users;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapteka.MenuActivity;
import com.example.myapteka.R;

public class HomeActivity extends AppCompatActivity {

    private Button homebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homebutton = findViewById(R.id.button);

        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent productsIntent = new Intent(HomeActivity.this, MenuActivity.class);
                startActivity(productsIntent);
            }
        });
    }
}