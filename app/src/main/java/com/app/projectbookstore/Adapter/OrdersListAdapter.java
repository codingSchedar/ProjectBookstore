package com.app.projectbookstore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.projectbookstore.Interface.RecyclerViewInterface;

import com.app.projectbookstore.Models.Orders;
import com.app.projectbookstore.R;

import java.util.ArrayList;

public class OrdersListAdapter extends RecyclerView.Adapter<OrdersListAdapter.OrdersListViewHolder>
{
    private final RecyclerViewInterface recyclerViewInterface;

    Context context;
    ArrayList<Orders> ordersListsArrayList;

    public OrdersListAdapter(Context context, ArrayList<Orders> ordersArrayList, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.ordersListsArrayList = ordersArrayList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public OrdersListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.orders_layout, parent, false);
        return new OrdersListViewHolder(v, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersListViewHolder ordersListViewHolder, int position) {
        Orders adminOrders = ordersListsArrayList.get(position);

        ordersListViewHolder.adminOrderName.setText("Claimee: "+adminOrders.getOrderName());
        ordersListViewHolder.adminOrderPhone.setText("Contact: "+ adminOrders.getOrderPhone());
        ordersListViewHolder.adminOrderTotalPrice.setText("Total Amount: PHP " + adminOrders.getOrderTotalAmount());
        ordersListViewHolder.adminOrderDateTime.setText("Ordered at: "+ adminOrders.getOrderDate() + " " + adminOrders.getOrderTime());
        ordersListViewHolder.adminOrderUserID.setText(adminOrders.getOrderUserID());
    }

    @Override
    public int getItemCount() {
        return ordersListsArrayList.size();
    }

    public static class OrdersListViewHolder extends RecyclerView.ViewHolder {


        TextView adminOrderName, adminOrderPhone, adminOrderTotalPrice, adminOrderDateTime, adminOrderUserID;


        public OrdersListViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            adminOrderName = itemView.findViewById(R.id.order_user_name);
            adminOrderPhone = itemView.findViewById(R.id.order_phone_number);
            adminOrderTotalPrice = itemView.findViewById(R.id.order_total_price);
            adminOrderDateTime = itemView.findViewById(R.id.order_date_time);
            adminOrderUserID = itemView.findViewById(R.id.order_user_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface != null)
                    {
                        int position = getAdapterPosition();

                        if(position != RecyclerView.NO_POSITION)
                        {
                            recyclerViewInterface.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
