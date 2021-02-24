package com.example.practicamercadomunicipal.recycleradapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practicamercadomunicipal.R;
import com.example.practicamercadomunicipal.models.Store;

import java.util.List;

public class StoresAdapter extends RecyclerView.Adapter<StoresAdapter.StoreViewHolder> {

    private List<Store> storeList;
    private Context context;
    private OnStoreClickListener storeClickListener;

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
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    public static class StoreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final OnStoreClickListener storeClickListener;

        public StoreViewHolder(@NonNull View itemView, OnStoreClickListener storeClickListener) {
            super(itemView);
            this.storeClickListener = storeClickListener;
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
