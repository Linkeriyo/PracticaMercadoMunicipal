package com.example.practicamercadomunicipal.stores;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practicamercadomunicipal.LoginActivity;
import com.example.practicamercadomunicipal.R;
import com.example.practicamercadomunicipal.data.AppData;
import com.example.practicamercadomunicipal.models.Store;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StoresActivity extends AppCompatActivity{

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
            } else if (item.getItemId() == R.id.sign_out_option) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
            return true;
        });
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.stores_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new StoresAdapter(AppData.storeList, this));
        if (AppData.storeList.isEmpty()) {
            findViewById(R.id.no_stores_textview).setVisibility(View.VISIBLE);
        }
        recyclerView.getAdapter().registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (!AppData.storeList.isEmpty()) {
                    findViewById(R.id.no_stores_textview).setVisibility(View.INVISIBLE);
                } else {
                    findViewById(R.id.no_stores_textview).setVisibility(View.VISIBLE);
                }
            }
        });
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
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}