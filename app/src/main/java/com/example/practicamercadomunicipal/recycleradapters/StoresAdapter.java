package com.example.practicamercadomunicipal.recycleradapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.practicamercadomunicipal.R;
import com.example.practicamercadomunicipal.models.Store;

import java.util.List;

public class StoresAdapter extends RecyclerView.Adapter<StoresAdapter.StoreViewHolder> {

    private final List<Store> storeList;
    private final Context context;
    private final OnStoreClickListener storeClickListener;

    public StoresAdapter(List<Store> storeList, Context context, OnStoreClickListener storeClickListener) {
        this.storeList = storeList;
        this.context = context;
        this.storeClickListener = storeClickListener;
    }

    @NonNull
    @Override
    public StoresAdapter.StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.store_row, parent, false);
        return new StoreViewHolder(v, storeClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StoresAdapter.StoreViewHolder holder, int position) {
        Store store = storeList.get(position);
        Glide.with(context).load(store.image).into(holder.storeImageView);
        holder.storeNameTextView.setText(store.name);
        holder.storeIdTextView.setText(store.ID);
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    public static class StoreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnStoreClickListener storeClickListener;
        TextView storeNameTextView;
        TextView storeIdTextView;
        ImageView storeImageView;

        public StoreViewHolder(@NonNull View itemView, OnStoreClickListener storeClickListener) {
            super(itemView);
            storeNameTextView = itemView.findViewById(R.id.store_name_textview);
            storeIdTextView = itemView.findViewById(R.id.store_id_textview);
            storeImageView = itemView.findViewById(R.id.store_image_imageview);
            this.storeClickListener = storeClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            storeClickListener.onStoreClick(getAdapterPosition());
        }
    }

    public interface OnStoreClickListener {
        void onStoreClick(int position);
    }
}
