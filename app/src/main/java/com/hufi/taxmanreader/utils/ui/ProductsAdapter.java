package com.hufi.taxmanreader.utils.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hufi.taxmanreader.GroomApplication;
import com.hufi.taxmanreader.R;
import com.hufi.taxmanreader.model.Product;

import java.util.List;


public class ProductsAdapter extends RecyclerView.Adapter<ProductsHolder> {

    private List<Product> products;

    public ProductsAdapter(List<Product> products) {
        this.products = products;
    }

    @Override
    public ProductsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items, parent, false);
        return new ProductsHolder(v);
    }

    @Override
    public void onBindViewHolder(ProductsHolder holder, int position) {
        if (position < getItemCount()) {
            Product product = products.get(position);

            if (product != null) {
                holder.id.setText(String.valueOf(product.getId()));
                holder.name.setText(product.getName());
                holder.price.setText(String.format("%s%s", product.getPrice(), "€"));
            }
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
