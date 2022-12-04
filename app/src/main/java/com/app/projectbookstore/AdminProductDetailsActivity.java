package com.app.projectbookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.app.projectbookstore.Models.Products;
import com.app.projectbookstore.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminProductDetailsActivity extends AppCompatActivity {

    private EditText productDetailsName, productDetailsPrice, productDetailsCategory;
    private TextView productDetailsID;
    private ImageView imgProductImage;
    private Button btnRemove;
    private TextView btnClose, btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_details);


        productDetailsName = findViewById(R.id.admin_product_name_details);
        productDetailsName.setText(getIntent().getStringExtra("productName"));

        productDetailsPrice = findViewById(R.id.admin_product_price_details);
        productDetailsPrice.setText(getIntent().getStringExtra("productPrice"));

        productDetailsCategory = findViewById(R.id.admin_product_category_details);
        productDetailsCategory.setText(getIntent().getStringExtra("productCategory"));

        imgProductImage = findViewById(R.id.admin_product_image_details);
        Picasso.get().load(getIntent().getStringExtra("productImage")).into(imgProductImage);

        productDetailsID = findViewById(R.id.admin_product_id);
        productDetailsID.setText(getIntent().getStringExtra("productID"));

        getProductDetails(productDetailsID.getText().toString());

        btnClose = findViewById(R.id.admin_close_settings_btn);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });


        btnUpdate = findViewById(R.id.admin_update_product_info);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                updateProductInfo();
            }
        });


        btnRemove = findViewById(R.id.btnRemoveProduct);

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove the item from the database
                removeProduct();
            }
        });
    }

    private void getProductDetails(String productID) {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Products products=dataSnapshot.getValue(Products.class);
                    productDetailsName.setText(products.getProductName());
                    productDetailsPrice.setText(products.getProductPrice());
                    productDetailsCategory.setText(products.getProductCategory());
                    productDetailsID.setText(products.getProductID());
                    Picasso.get().load(products.getProductImage()).into(imgProductImage);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void removeProduct()
    {
        final DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productRef.child(productDetailsID.getText().toString())
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(AdminProductDetailsActivity.this,"Product removed",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AdminProductDetailsActivity.this, AdminItemsListActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    private void updateProductInfo()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Products");

        HashMap<String, Object> productMap = new HashMap<>();
        productMap. put("productName", productDetailsName.getText().toString());
        productMap. put("productPrice", productDetailsPrice.getText().toString());
        productMap.put("productCategory", productDetailsCategory.getText().toString());
        ref.child(productDetailsID.getText().toString()).updateChildren(productMap);
        startActivity(new Intent(AdminProductDetailsActivity.this, AdminHomeScreenActivity.class));
        Toast.makeText(AdminProductDetailsActivity.this, "Product Updated", Toast.LENGTH_SHORT).show();
        finish();
    }
}