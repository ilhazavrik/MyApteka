package com.example.myapteka;


import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    private ListView listView;
    private DatabaseReference mDatabase;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> productsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        listView = findViewById(R.id.listView);
        productsList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productsList);
        listView.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("products").child("products");

        // Добавляем прослушиватель для данных Firebase
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Очищаем список перед загрузкой новых данных
                productsList.clear();

                // Итерируемся по всем продуктам в базе данных Firebase
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    // Получаем данные о продукте
                    String productName = productSnapshot.child("name").getValue(String.class);
                    String productPrice = productSnapshot.child("price").getValue(String.class);

                    // Добавляем данные о продукте в список
                    productsList.add("Name: " + productName + "\nPrice: " + productPrice);
                }

                // Уведомляем адаптер о изменениях в данных
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // В случае ошибки считываемя из базы данных Firebase
                Log.e("MenuActivity", "Error reading data from Firebase: " + databaseError.getMessage());
            }
        });
    }
}
