package com.example.practicamercadomunicipal.recycleradapters;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.practicamercadomunicipal.EditStoreActivity;
import com.example.practicamercadomunicipal.R;
import com.example.practicamercadomunicipal.data.AppData;
import com.example.practicamercadomunicipal.models.Store;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class StoresAdapter extends RecyclerView.Adapter<StoresAdapter.StoreViewHolder> {

    private final List<Store> storeList;
    private final Context context;

    public StoresAdapter(List<Store> storeList, Context context) {
        this.storeList = storeList;
        this.context = context;
    }

    @NonNull
    @Override
    public StoresAdapter.StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.store_row, parent, false);
        return new StoreViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StoresAdapter.StoreViewHolder holder, int position) {
        Store store = storeList.get(position);
        Glide.with(context).load(store.image).into(holder.storeImageView);
        holder.storeNameTextView.setText(store.name);
        holder.storeIdTextView.setText(store.ID);

        holder.deleteStoreButton.setOnClickListener(v -> {
            DatabaseReference storesReference = FirebaseDatabase.getInstance().getReference("stores");
            storesReference.child(AppData.storeList.get(position).ID).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    notifyDataSetChanged();
                }
            });
        });

        holder.editStoreButton.setOnClickListener(v -> {
            context.startActivity(new Intent(context, EditStoreActivity.class));
        });
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    public static class StoreViewHolder extends RecyclerView.ViewHolder{

        TextView storeNameTextView;
        TextView storeIdTextView;
        ImageView storeImageView;
        ImageButton deleteStoreButton;
        ImageButton editStoreButton;

        public StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            storeNameTextView = itemView.findViewById(R.id.store_name_textview);
            storeIdTextView = itemView.findViewById(R.id.store_id_textview);
            storeImageView = itemView.findViewById(R.id.store_image_imageview);
            deleteStoreButton = itemView.findViewById(R.id.delete_store_button);
            editStoreButton = itemView.findViewById(R.id.edit_store_button);
        }
    }
}
