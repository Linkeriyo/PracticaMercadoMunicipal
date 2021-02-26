package com.example.practicamercadomunicipal;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practicamercadomunicipal.data.AppData;
import com.example.practicamercadomunicipal.models.Store;
import com.example.practicamercadomunicipal.recycleradapters.StoresAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StoresActivity extends AppCompatActivity implements StoresAdapter.OnStoreClickListener {

    RecyclerView recyclerView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores);
        setupRecyclerView();
        setupToolBar();
        setupDatabaseListener();
    }

    private void setupToolBar() {
        toolbar = findViewById(R.id.stores_toolbar);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.add_store_option) {
                startActivity(new Intent(this, NewStoreActivity.class));
            }
            return true;
        });
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.stores_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new StoresAdapter(AppData.storeList, this, this));
        if (AppData.storeList.isEmpty()) {
            findViewById(R.id.no_stores_textview).setVisibility(View.VISIBLE);
        }
    }

    private void setupDatabaseListener() {
        DatabaseReference storesReference = FirebaseDatabase.getInstance().getReference("stores");
        storesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Store> stores = new ArrayList<>();
                snapshot.getChildren().forEach(child -> {
                    stores.add(child.getValue(Store.class));
                });
                AppData.storeList.clear();
                AppData.storeList.addAll(stores);
                updateRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void updateRecyclerView() {
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.invalidate();
    }

    @Override
    public void onStoreClick(int position) {
        Toast.makeText(this,
                "owo tocaste la posición " + position,
                Toast.LENGTH_SHORT
        ).show();
    }
}