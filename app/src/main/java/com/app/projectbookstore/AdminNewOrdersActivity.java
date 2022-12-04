package com.app.projectbookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.app.projectbookstore.Adapter.OrdersListAdapter;
import com.app.projectbookstore.Interface.RecyclerViewInterface;

import com.app.projectbookstore.Models.Orders;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminNewOrdersActivity extends AppCompatActivity implements RecyclerViewInterface {

    private RecyclerView recyclerView;
    private DatabaseReference ordersRef;
    OrdersListAdapter ordersListAdapter;
    ArrayList<Orders> adminOrdersArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        recyclerView = findViewById(R.id.orders_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adminOrdersArrayList = new ArrayList<>();
        ordersListAdapter = new OrdersListAdapter(this, adminOrdersArrayList, this);
        recyclerView.setAdapter(ordersListAdapter);

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Orders adminOrders = dataSnapshot.getValue(Orders.class);
                    adminOrdersArrayList.add(adminOrders);
                }
                ordersListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    /*
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersRef, AdminOrders.class)
                .build();

        FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrdersViewHolder adminOrdersViewHolder, final int i, @NonNull AdminOrders adminOrders) {
                        adminOrdersViewHolder.adminOrderName.setText("Name: "+adminOrders.getName());
                        adminOrdersViewHolder.adminOrderPhone.setText("Name: "+adminOrders.getPhone());
                        adminOrdersViewHolder.adminOrderTotalPrice.setText("Total Ammount = Rs."+adminOrders.getTotalAmount());
                        adminOrdersViewHolder.adminOrderDateTime.setText("Order at: "+adminOrders.getDate()+" "+ adminOrders.getTime());
                        adminOrdersViewHolder.btnShowOrders.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String uID = getRef(i).getKey();
                                Intent intent = new Intent(AdminNewOrdersActivity.this,AdminUserProductsActivity.class);
                                intent.putExtra("uid",uID);
                                startActivity(intent);
                            }
                        });

                        adminOrdersViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence options[] =new CharSequence[]{
                                        "Yes",
                                        "No"

                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                                builder.setTitle("Is this order claimed already?");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (i==0){
                                            String uID = getRef(i).getKey();
                                            RemoverOrder(uID);

                                        }
                                        else {
                                            finish();
                                        }

                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                        return new AdminOrdersViewHolder(view);
                    }
                };
        ordersList.setAdapter(adapter);
        adapter.startListening();
    }


    private void RemoverOrder(String orderItemID) {
        ordersRef.child(orderItemID).removeValue();
    }

     */

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(AdminNewOrdersActivity.this, AdminUserProductsActivity.class);

        intent.putExtra("adminOrderName", adminOrdersArrayList.get(position).getOrderName());
        intent.putExtra("adminOrderPhone", adminOrdersArrayList.get(position).getOrderPhone());
        intent.putExtra("adminOrderTotalPrice", adminOrdersArrayList.get(position).getOrderTotalAmount());
        intent.putExtra("adminOrderDateTime", adminOrdersArrayList.get(position).getOrderDate() + " " + adminOrdersArrayList.get(position).getOrderTime());
        intent.putExtra("clickedItem", adminOrdersArrayList.get(position).getOrderUserID());
        startActivity(intent);
    }
}