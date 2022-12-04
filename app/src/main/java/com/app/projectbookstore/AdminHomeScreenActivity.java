package com.app.projectbookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.projectbookstore.Prevalent.Prevalent;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AdminHomeScreenActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ImageView imgBook, imgUniforms, imgMerchendise;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_screen);

        bottomNavigationView = findViewById(R.id.adminBottomNavView);
        imgBook = findViewById(R.id.books);
        imgUniforms = findViewById(R.id.uniforms);
        imgMerchendise = findViewById(R.id.others);

        imgBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminHomeScreenActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Books");
                startActivity(intent);
            }
        });
        imgUniforms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminHomeScreenActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Uniforms");
                startActivity(intent);
            }
        });


        imgMerchendise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminHomeScreenActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Others");
                startActivity(intent);
            }
        });


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.adminHome:
                        Intent intentHome = new Intent(AdminHomeScreenActivity.this, AdminHomeScreenActivity.class);
                        startActivity(intentHome);
                        finish();
                        break;

                    case R.id.adminOrders:
                        Intent intentOrders= new Intent(AdminHomeScreenActivity.this, AdminNewOrdersActivity.class);
                        startActivity(intentOrders);
                        break;

                    case R.id.adminAddItem:
                        Intent intentCart = new Intent(AdminHomeScreenActivity.this, AdminItemsListActivity.class);
                        startActivity(intentCart);
                        break;

                    case R.id.adminMenuProfile:
                        Intent intent = new Intent(AdminHomeScreenActivity.this, AdminProfileActivity.class);
                        startActivity(intent);
                        break;

                    default:
                }
                return true;
            }
        });
    }
}