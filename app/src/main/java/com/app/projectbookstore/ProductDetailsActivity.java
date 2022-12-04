package com.app.projectbookstore;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private TextView productDetailsName, productDetailsPrice, productDetailsCategory, productDetailsID;
    private ImageView imgProductImage;
    private NumberPicker numberPicker;
    private Button btnAddToCart;
    private String state = "Unclaimed";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productDetailsName = findViewById(R.id.product_name_details);
        productDetailsName.setText(getIntent().getStringExtra("productName"));

        productDetailsPrice = findViewById(R.id.product_price_details);
        productDetailsPrice.setText(getIntent().getStringExtra("productPrice"));

        productDetailsCategory = findViewById(R.id.product_category);
        productDetailsCategory.setText(getIntent().getStringExtra("productCategory"));

        imgProductImage = findViewById(R.id.product_image_details);
        Picasso.get().load(getIntent().getStringExtra("productImage")).into(imgProductImage);

        productDetailsID = findViewById(R.id.product_id);
        productDetailsID.setText(getIntent().getStringExtra("productID"));

        getProductDetails(productDetailsID.getText().toString());

        numberPicker = findViewById(R.id.number_picker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);
        numberPicker.setTextColor(Color.rgb(0,0,0));


        btnAddToCart = findViewById(R.id.btnAddToCart);

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    addToCart();
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        CheckOrderState();
    }

    private void addToCart()
    {

        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String, Object>cartMap = new HashMap<>();
        cartMap.put("productID", productDetailsID.getText().toString());
        cartMap.put("productName",productDetailsName.getText().toString());
        cartMap.put("productPrice",productDetailsPrice.getText().toString());
        cartMap.put("date",currentDate.toString());
        cartMap.put("time",currentTime.toString());
        cartMap.put("productQuantity",Integer.toString(numberPicker.getValue()));

        cartListRef.child("User view").child(Prevalent.currentOnlineUser.getUserID()).child("Products").child(productDetailsID.getText().toString()).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    cartListRef.child("Admin view").child(Prevalent.currentOnlineUser.getUserID())
                            .child("Products").child(productDetailsID.getText().toString())
                            .updateChildren(cartMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(ProductDetailsActivity.this,"Added to cart List",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ProductDetailsActivity.this,StudentHomeScreenActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                }
            }
        });
    }

    private void CheckOrderState()
    {
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getUserID());
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists()){
                    String claimedState = dataSnapshot.child("orderState").getValue().toString();
                    if (claimedState.equals("Claimed")){
                        state ="Order Claimed";
                    }
                    else if (claimedState.equals("Not Claimed")){
                        state ="Order Placed";
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
}