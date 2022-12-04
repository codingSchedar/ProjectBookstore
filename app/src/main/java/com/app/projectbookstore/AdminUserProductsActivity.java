package com.app.projectbookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.app.projectbookstore.Models.Cart;
import com.app.projectbookstore.Models.Users;
import com.app.projectbookstore.Prevalent.CartViewHolder;
import com.app.projectbookstore.Prevalent.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AdminUserProductsActivity extends AppCompatActivity {
    private RecyclerView productsList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;
    private String userID = "", totalPrice = "";
    private Button btnClaimed;
    public Integer pointsToUpdate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);

        userID = getIntent().getStringExtra("clickedItem");
        totalPrice = getIntent().getStringExtra("adminOrderTotalPrice");

        productsList = findViewById(R.id.products_list);
        productsList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        productsList.setLayoutManager(layoutManager);
        cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin view").child(userID).child("Products");

        btnClaimed = findViewById(R.id.btnClaimed);
        btnClaimed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOrder();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Cart> options=
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef,Cart.class)
                        .build();
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {

                holder.txtProductQuantity.setText("Quantity: "+model.getProductQuantity());
                holder.txtProductPrice.setText("Price: PHP " + model.getProductPrice());
                holder.txtProductName.setText("Product: " + model.getProductName());
                holder.txtProductID.setText(model.getProductID());
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };
        productsList.setAdapter(adapter);
        adapter.startListening();
    }

    private void updateOrder()
    {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin view");

        //updating points of the user who ordered
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    if(snapshot.child(userID).child("userPoints").exists())
                    {
                        Users users = snapshot.getValue(Users.class);
                        pointsToUpdate = users.getUserPoints();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("userPoints", pointsToUpdate + Integer.parseInt(totalPrice) / 100);
        usersRef.child(userID).updateChildren(userMap);

        orderRef.child(userID).removeValue();
        adminRef.child(userID).removeValue();

        startActivity(new Intent(AdminUserProductsActivity.this, AdminHomeScreenActivity.class));
        Toast.makeText(AdminUserProductsActivity.this, "Order Updated", Toast.LENGTH_SHORT).show();
        finish();
    }
}