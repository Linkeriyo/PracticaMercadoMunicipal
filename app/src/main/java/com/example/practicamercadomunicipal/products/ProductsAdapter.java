package com.example.practicamercadomunicipal.products;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.practicamercadomunicipal.R;
import com.example.practicamercadomunicipal.data.AppData;
import com.example.practicamercadomunicipal.models.Product;
import com.example.practicamercadomunicipal.products.EditProductActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private final List<Product> productList;
    private final Context context;

    public ProductsAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductsAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.product_row, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        Glide.with(context).load(product.image).into(holder.productImageView);
        holder.productNameTextView.setText(product.desc);
        holder.productIdTextView.setText(product.ID);

        holder.deleteProductButton.setOnClickListener(v -> {
            //Borrar la imagen de firebase.
            Uri storageUri = Uri.parse(productList.get(position).imgStorage);
            StorageReference imagesReference = FirebaseStorage.getInstance().getReference("images");
            imagesReference.child(storageUri.getLastPathSegment()).delete();

            //Borrar el local de la base de datos.
            DatabaseReference productsReference = FirebaseDatabase.getInstance().getReference("products");
            productsReference.child(productList.get(position).ID).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    notifyDataSetChanged();
                }
            });
        });

        holder.editProductButton.setOnClickListener(v -> {
            context.startActivity(new Intent(context, EditProductActivity.class).putExtra("productNumber", position));
        });

        holder.itemView.setOnClickListener(v -> {
            context.startActivity(new Intent(context, ProductsActivity.class).putExtra("productNumber", position));
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder{

        TextView productNameTextView;
        TextView productIdTextView;
        ImageView productImageView;
        ImageButton deleteProductButton;
        ImageButton editProductButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.product_name_textview);
            productIdTextView = itemView.findViewById(R.id.product_id_textview);
            productImageView = itemView.findViewById(R.id.product_image_imageview);
            deleteProductButton = itemView.findViewById(R.id.delete_product_button);
            editProductButton = itemView.findViewById(R.id.edit_product_button);
        }
    }
}
