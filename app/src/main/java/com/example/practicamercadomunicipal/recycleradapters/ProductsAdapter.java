package com.example.practicamercadomunicipal.recycleradapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practicamercadomunicipal.R;
import com.example.practicamercadomunicipal.models.Product;

import java.util.List;


public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>{

    private List<Product> productList;
    private Context context;
    private ProductsAdapter.OnProductClickListener productClickListener;

    public ProductsAdapter(List<Product> productList, Context context, ProductsAdapter.OnProductClickListener productClickListener) {
        this.productList = productList;
        this.context = context;
        this.productClickListener = productClickListener;
    }

    @NonNull
    @Override
    public ProductsAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.product_row, parent, false);
        return new ProductsAdapter.ProductViewHolder(v, productClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.ProductViewHolder holder, int position) {
        Product product = productList.get(position);
    }

    @Override
    public int getItemCount() {return productList.size();}

    public static class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ProductsAdapter.OnProductClickListener productClickListener;

        public ProductViewHolder(@NonNull View itemView, ProductsAdapter.OnProductClickListener productClickListener) {
            super(itemView);
            this.productClickListener = productClickListener;
        }

        @Override
        public void onClick(View v) {
            productClickListener.onProductClick(getAdapterPosition());
        }
    }

    public interface OnProductClickListener {
        void onProductClick(int position);
    }
}
