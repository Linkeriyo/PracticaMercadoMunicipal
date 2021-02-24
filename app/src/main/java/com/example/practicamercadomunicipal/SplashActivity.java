package com.example.practicamercadomunicipal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.practicamercadomunicipal.data.AppData;
import com.example.practicamercadomunicipal.models.Store;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private final static String TAG = "SplashActivity";

    protected Context context = this;
    boolean storesLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, StoresActivity.class));
        loadData();
    }

    private void loadData() {
        DatabaseReference storesReference = FirebaseDatabase.getInstance().getReference("stores");
        storesReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                List<Store> stores = new ArrayList<>();
                snapshot.getChildren().forEach(child -> {
                    Store store = child.getValue(Store.class);
                    if (store != null) {
                        stores.add(store);
                    }
                });
                AppData.storeList = stores;
            }
            tryNextActivity();
        });
    }

    private void tryNextActivity() {
        if (storesLoaded) {
            startActivity(new Intent(context, StoresActivity.class));
            finish();
        }
    }

    //      __
    //  ___( o)> (cuak)
    //  \ <_. )
    //   `---'
}
