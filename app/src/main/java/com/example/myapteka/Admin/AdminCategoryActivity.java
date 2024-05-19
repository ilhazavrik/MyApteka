package com.example.myapteka.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapteka.MenuActivity;
import com.example.myapteka.R;
import com.example.myapteka.Users.HomeActivity;

public class AdminCategoryActivity extends AppCompatActivity {

    private Button buttonHead;
    private Button buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        buttonHead = findViewById(R.id.button2);
        buttonAdd = findViewById(R.id.button3);

        buttonHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// Открываем AddNewProductActivity при нажатии на кнопку "Товары"
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Товары");
                startActivity(intent);
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// Открываем AdminActivity при нажатии на кнопку "Панель управления"
                Intent intent = new Intent(AdminCategoryActivity.this, MenuActivity.class);
                intent.putExtra("category", "Панель управления");
                startActivity(intent);
            }
        });
    }
}
