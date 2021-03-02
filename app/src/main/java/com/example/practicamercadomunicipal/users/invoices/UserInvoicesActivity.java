package com.example.practicamercadomunicipal.users.invoices;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practicamercadomunicipal.R;
import com.example.practicamercadomunicipal.data.AppData;
import com.example.practicamercadomunicipal.models.Invoice;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserInvoicesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Toolbar toolbar;
    String uid;
    public static UserInvoicesActivity activity;
    TextView noInvoicesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = getIntent().getStringExtra("uid");
        setContentView(R.layout.activity_invoices);
        if (AppData.invoiceList == null) {
            loadInvoicesIfNull();
        } else {
            setupViews();
            setupRecyclerView();
            setupToolBar();
            setupDatabaseListener();
        }
        activity = this;
    }

    private void setupViews() {
        noInvoicesTextView = findViewById(R.id.no_invoices_textview);
    }

    private void loadInvoicesIfNull() {
        DatabaseReference invoicesReference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("invoices");
        invoicesReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Invoice> invoices = new ArrayList<>();
                task.getResult().getChildren().forEach(child -> {
                    invoices.add(child.getValue(Invoice.class));
                });
                AppData.invoiceList = invoices;
            }
            if (AppData.invoiceList == null) {
                AppData.invoiceList = new ArrayList<>();
            }
            setupViews();
            setupRecyclerView();
            setupToolBar();
            setupDatabaseListener();
        });
    }

    @SuppressLint("NonConstantResourceId")
    private void setupToolBar() {
        toolbar = findViewById(R.id.invoices_toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.invoices_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new InvoicesAdapter(AppData.invoiceList, this, uid));
    }

    private void setupDatabaseListener() {
        DatabaseReference invoicesReference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("invoices");
        invoicesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Invoice> invoices = new ArrayList<>();
                snapshot.getChildren().forEach(child -> {
                    invoices.add(child.getValue(Invoice.class));
                });
                AppData.invoiceList.clear();
                AppData.invoiceList.addAll(invoices);
                recyclerView.getAdapter().notifyDataSetChanged();
                if (AppData.invoiceList.isEmpty()) {
                    noInvoicesTextView.setVisibility(View.VISIBLE);
                } else {
                    noInvoicesTextView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void notifyRecyclerView() {
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}