package com.app.projectbookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.app.projectbookstore.Adapter.ProductAdapter;
import com.app.projectbookstore.Interface.RecyclerViewInterface;
import com.app.projectbookstore.Models.Products;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

public class AdminItemsListActivity extends AppCompatActivity implements RecyclerViewInterface {

    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    ProductAdapter productAdapter;
    ArrayList<Products> productsArrayList;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_items_list);

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView = findViewById(R.id.items_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productsArrayList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productsArrayList, this);
        recyclerView.setAdapter(productAdapter);

        ProductsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Products products = dataSnapshot.getValue(Products.class);
                    productsArrayList.add(products);
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        searchView = findViewById(R.id.search_bar);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(AdminItemsListActivity.this, AdminProductDetailsActivity.class);

        intent.putExtra("productName", productsArrayList.get(position).getProductName());
        intent.putExtra("productPrice", productsArrayList.get(position).getProductPrice());
        intent.putExtra("productCategory", productsArrayList.get(position).getProductCategory());
        intent.putExtra("productImage", productsArrayList.get(position).getProductImage());
        intent.putExtra("productID", productsArrayList.get(position).getProductID());
        startActivity(intent);
    }

    private void filterList(String text)
    {
        ArrayList<Products> filteredList = new ArrayList<>();

        for(Products products : productsArrayList)
        {
            if(products.getProductName().toLowerCase().contains(text.toLowerCase()))
            {
                filteredList.add(products);
            }
        }

        if(filteredList.isEmpty())
        {
            Toast.makeText(this, "Item doesn't exist", Toast.LENGTH_SHORT).show();
        }
        else
        {
            productAdapter.setFilteredList(filteredList);
        }
    }

}