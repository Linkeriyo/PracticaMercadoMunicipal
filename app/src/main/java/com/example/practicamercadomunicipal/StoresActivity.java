package com.example.practicamercadomunicipal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practicamercadomunicipal.data.AppData;
import com.example.practicamercadomunicipal.recycleradapters.StoresAdapter;

public class StoresActivity extends AppCompatActivity implements StoresAdapter.OnStoreClickListener {

    RecyclerView recyclerView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores);
        setupRecyclerView();
        setupToolBar();
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

    @Override
    public void onStoreClick(int position) {
        Toast.makeText(this,
                "owo tocaste la posición " + position,
                Toast.LENGTH_SHORT
        ).show();
    }
}