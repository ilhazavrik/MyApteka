package com.example.myapteka.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapteka.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminAddNewProductActivity extends AppCompatActivity {

    private EditText productNameEditText, productPriceEditText;
    private Spinner productSpinner;
    private Button btnAdd, btnUpdate, btnDelete;
    private DatabaseReference mDatabase;
    private ArrayAdapter<String> productAdapter;
    private List<String> productIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("products");

        productNameEditText = findViewById(R.id.product_name);
        productPriceEditText = findViewById(R.id.product_price);
        productSpinner = findViewById(R.id.product_spinner);
        btnAdd = findViewById(R.id.btn_add);
        btnUpdate = findViewById(R.id.update_panel);
        btnDelete = findViewById(R.id.delete_panel);

        productIds = new ArrayList<>();
        productAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, productIds);
        productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productSpinner.setAdapter(productAdapter);

        loadProducts();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProduct();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct();
            }
        });
    }


    private void loadProducts() {
        mDatabase.child("products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    productIds.clear(); // Очистим список перед добавлением новых элементов
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String productName = snapshot.child("name").getValue(String.class);
                        if (productName != null && !productName.isEmpty()) {
                            productIds.add(productName); // Добавим название товара в список
                        }
                    }
                    productAdapter.notifyDataSetChanged(); // Обновим адаптер Spinner
                } else {
                    Toast.makeText(AdminAddNewProductActivity.this, "Список продуктов пуст", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminAddNewProductActivity.this, "Ошибка при загрузке товаров: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addProduct() {
        final String productName = productNameEditText.getText().toString().trim();
        final String productPrice = productPriceEditText.getText().toString().trim();

        if (!productName.isEmpty() && !productPrice.isEmpty()) {
            DatabaseReference newProductRef = mDatabase.child("products").push(); // Генерируем новый идентификатор товара
            newProductRef.child("name").setValue(productName);
            newProductRef.child("price").setValue(productPrice)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AdminAddNewProductActivity.this, "Товар успешно добавлен", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminAddNewProductActivity.this, "Ошибка при добавлении товара: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Введите все данные о товаре", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateProduct() {
        final int selectedPosition = productSpinner.getSelectedItemPosition();
        if (selectedPosition == Spinner.INVALID_POSITION) {
            Toast.makeText(this, "Выберите товар для обновления", Toast.LENGTH_SHORT).show();
            return;
        }

        final String selectedProductName = productIds.get(selectedPosition); // Получаем название выбранного товара
        final String newProductName = productNameEditText.getText().toString().trim(); // Получаем новое название товара
        final String newProductPrice = productPriceEditText.getText().toString().trim(); // Получаем новую цену товара

        if (TextUtils.isEmpty(newProductName) || TextUtils.isEmpty(newProductPrice)) {
            Toast.makeText(this, "Введите название и цену товара для обновления", Toast.LENGTH_SHORT).show();
            return;
        }

        mDatabase.child("products").orderByChild("name").equalTo(selectedProductName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                        String productId = productSnapshot.getKey();
                        DatabaseReference productRef = mDatabase.child("products").child(productId);
                        productRef.child("name").setValue(newProductName); // Обновляем название товара
                        productRef.child("price").setValue(newProductPrice) // Обновляем цену товара
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(AdminAddNewProductActivity.this, "Товар успешно обновлен", Toast.LENGTH_SHORT).show();
                                        clearFields();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AdminAddNewProductActivity.this, "Ошибка при обновлении товара: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } else {
                    Toast.makeText(AdminAddNewProductActivity.this, "Товар с таким названием не найден", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminAddNewProductActivity.this, "Ошибка при обновлении товара: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void deleteProduct() {
        final int selectedPosition = productSpinner.getSelectedItemPosition();
        if (selectedPosition == Spinner.INVALID_POSITION) {
            Toast.makeText(this, "Выберите товар для удаления", Toast.LENGTH_SHORT).show();
            return;
        }

        final String productName = productIds.get(selectedPosition);

        mDatabase.child("products").orderByChild("name").equalTo(productName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                        productSnapshot.getRef().removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(AdminAddNewProductActivity.this, "Товар успешно удален", Toast.LENGTH_SHORT).show();
                                        clearFields();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AdminAddNewProductActivity.this, "Ошибка при удалении товара: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } else {
                    Toast.makeText(AdminAddNewProductActivity.this, "Товар с таким названием не найден", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminAddNewProductActivity.this, "Ошибка при удалении товара: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearFields() {
        productNameEditText.getText().clear();
        productPriceEditText.getText().clear();
    }
}
