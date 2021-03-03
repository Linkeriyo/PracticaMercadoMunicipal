package com.example.practicamercadomunicipal.users.invoices;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practicamercadomunicipal.R;
import com.example.practicamercadomunicipal.data.AppData;
import com.example.practicamercadomunicipal.models.Invoice;
import com.example.practicamercadomunicipal.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InvoiceDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Toolbar toolbar;
    Invoice invoice;
    Button payButton;
    String uid;
    Button cancelButton;
    TextView totalTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = getIntent().getStringExtra("uid");
        setContentView(R.layout.activity_invoice_details);
        totalTextView = findViewById(R.id.invoice_details_total_textview);
        invoice = AppData.getInvoiceByNumber(getIntent().getIntExtra("invoiceNumber", 0));
        setupToolBar();
        setupRecyclerView();
        setupButtons();
    }

    @SuppressLint("NonConstantResourceId")
    private void setupToolBar() {
        toolbar = findViewById(R.id.invoice_details_toolbar);
        toolbar.setSubtitle("Número de factura: " + invoice.number);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.invoice_details_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new InvoiceLinesAdapter(invoice.lines, this, invoice));
        if (invoice.lines.isEmpty()) {
            findViewById(R.id.no_invoice_details_textview).setVisibility(View.VISIBLE);
        }
        totalTextView.setText("Total: " + invoice.calculateTotal());
        recyclerView.getAdapter().registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (!invoice.lines.isEmpty()) {
                    findViewById(R.id.no_invoice_details_textview).setVisibility(View.INVISIBLE);
                } else {
                    findViewById(R.id.no_invoice_details_textview).setVisibility(View.VISIBLE);
                }
                totalTextView.setText("Total: " + invoice.calculateTotal());
            }
        });
    }

    private void setupButtons() {
        payButton = findViewById(R.id.pay_invoice_button);
        cancelButton = findViewById(R.id.cancel_invoice_button);

        if (invoice.lines.isEmpty()) {
            payButton.setEnabled(false);
            cancelButton.setEnabled(false);
        }

        if (invoice.cancelled) {
            payButton.setVisibility(View.INVISIBLE);
            cancelButton.setVisibility(View.INVISIBLE);
        } else {
            if (invoice.paid) {
                payButton.setVisibility(View.INVISIBLE);
            }
        }

        payButton.setOnClickListener(v -> {
            invoice.paid = true;
            saveChanges();
        });

        cancelButton.setOnClickListener(v -> {
            invoice.cancelled = true;
            saveChanges();
        });
    }

    private void saveChanges() {
        User user = AppData.getUserById(uid);
        user.invoices = AppData.invoiceList;
        DatabaseReference userReference = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uid);
        userReference.setValue(user).addOnCompleteListener(task -> {
            UserInvoicesActivity.activity.notifyRecyclerView();
            finish();
        });
    }
}