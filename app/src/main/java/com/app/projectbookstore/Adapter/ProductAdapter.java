package com.app.projectbookstore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.projectbookstore.Interface.RecyclerViewInterface;
import com.app.projectbookstore.Models.Products;
import com.app.projectbookstore.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<   ProductAdapter.MyViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;

    Context context;
    ArrayList<Products> productsArrayList;

    public void setFilteredList(ArrayList<Products> filteredList)
    {
        this.productsArrayList = filteredList;
        notifyDataSetChanged();
    }

    public ProductAdapter(Context context, ArrayList<Products> productsArrayList, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.productsArrayList = productsArrayList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Products products = productsArrayList.get(position);

        holder.productID.setText(products.getProductID());
        holder.productName.setText(products.getProductName());
        holder.productPrice.setText(products.getProductPrice());
        holder.productCategory.setText(products.getProductCategory());
        Picasso.get().load(products.getProductImage()).into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return productsArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        TextView productName, productPrice, productCategory, productID;
        ImageView productImage;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            productID = itemView.findViewById(R.id.itemProductID);
            productName = itemView.findViewById(R.id.itemProductName);
            productPrice = itemView.findViewById(R.id.itemProductPrice);
            productCategory = itemView.findViewById(R.id.itemProductCategory);
            productImage = itemView.findViewById(R.id.itemProductImage);

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