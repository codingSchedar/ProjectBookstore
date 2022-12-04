package com.app.projectbookstore.Prevalent;


import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.projectbookstore.Interface.MyItemClickListener;
import com.app.projectbookstore.R;
import com.app.projectbookstore.Interface.MyItemClickListener;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProductID, txtProductName,txtProductPrice,txtProductQuantity;
    private MyItemClickListener myItemClickListener;

    public CartViewHolder(View itemView) {
        super(itemView);
        txtProductID = itemView.findViewById(R.id.cart_product_id);
        txtProductName = itemView.findViewById(R.id.cart_product_name);
        txtProductPrice = itemView.findViewById(R.id.cart_product_price);
        txtProductQuantity = itemView.findViewById(R.id.cart_product_quantity);
    }

    @Override
    public void onClick(View view) {
        myItemClickListener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListner(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }
}


