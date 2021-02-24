package com.example.practicamercadomunicipal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.practicamercadomunicipal.data.AppData;
import com.example.practicamercadomunicipal.models.Store;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        Thread waiter = new Thread(new WaiterForNextActivity());
        waiter.start();
        try {
            waiter.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        DatabaseReference storesReference = FirebaseDatabase.getInstance().getReference("stores");
        storesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Store> stores = new ArrayList<>();
                snapshot.getChildren().forEach(child -> {
                    Store store = child.getValue(Store.class);
                    if (store != null) {
                        stores.add(store);
                    }
                });
                AppData.storeList = stores;
                storesLoaded = true;
                storesReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    class WaiterForNextActivity implements Runnable {
        @Override
        public void run() {
            while (!storesLoaded) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            startActivity(new Intent(context, StoresActivity.class));
            finish();
        }
    }

    //      __
    //  ___( o)> (cuak)
    //  \ <_. )
    //   `---'
}
