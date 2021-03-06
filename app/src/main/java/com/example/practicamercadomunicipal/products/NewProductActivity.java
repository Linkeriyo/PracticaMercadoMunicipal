package com.example.practicamercadomunicipal.products;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.practicamercadomunicipal.R;
import com.example.practicamercadomunicipal.data.AppData;
import com.example.practicamercadomunicipal.models.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Objects;

public class NewProductActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Toolbar toolbar;
    TextView descTextView, idTextView, priceTextView, stockTextView;
    ImageView imageView;
    Switch kgSwitch;
    Uri imageUri, postImageUri;
    ProgressBar progressBar;
    String storeID;
    List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);
        storeID = getIntent().getStringExtra("storeID");
        productList = AppData.getStoreById(storeID).products;
        setupFirebaseVariables();
        setupViews();
        setupToolbar();
    }

    private void setupFirebaseVariables() {
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    private void setupViews() {
        descTextView = findViewById(R.id.new_product_desc_textview);
        stockTextView = findViewById(R.id.new_product_stock_textview);
        idTextView = findViewById(R.id.new_product_id_textview);
        priceTextView = findViewById(R.id.new_product_price_textview);
        imageView = findViewById(R.id.new_product_image_imageview);
        imageView.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, PICK_IMAGE);
        });
        progressBar = findViewById(R.id.new_product_progressbar);
        kgSwitch = findViewById(R.id.new_product_switch);
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.new_product_toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.edit_product_confirm_option && isProductOk()) {
                if (postImageUri == null) {
                    postImageUri = Uri.EMPTY;
                }
                String id = idTextView.getText().toString();
                String desc = descTextView.getText().toString();
                double price = Double.parseDouble(priceTextView.getText().toString());
                double stock = Double.parseDouble(stockTextView.getText().toString());
                boolean kgUnit = kgSwitch.isChecked();
                Product product = new Product(storeID, id, desc, price, imageUri, postImageUri, stock, kgUnit);
                productList.add(product);
                DatabaseReference storeReference = database.getReference("stores").child(storeID);
                storeReference.setValue(AppData.getStoreById(storeID));
                finish();
            } else {
                Toast.makeText(this,
                        "Los campos no deben estar vacíos",
                        Toast.LENGTH_SHORT
                ).show();
            }
            return true;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (data != null) {
                progressBar.setVisibility(View.VISIBLE);
                Uri uri = data.getData();
                StorageReference fileReference = storage.getReference("images").child(uri.getLastPathSegment());
                fileReference.putFile(uri).continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }
                    return fileReference.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        imageUri = Objects.requireNonNull(task.getResult());
                        if (postImageUri != null) {
                            StorageReference previousFileReference = storage.getReference("images").child(postImageUri.getLastPathSegment());
                            previousFileReference.delete();
                        }
                        putImage(imageUri);
                        postImageUri = uri;
                    }
                });
            }
        }
    }

    private void putImage(Uri imageUri) {
        Glide.with(this)
                .load(imageUri)
                .centerCrop()
                .into(imageView);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private boolean isProductOk() {
        return (!descTextView.getText().toString().isEmpty()
                && !idTextView.getText().toString().isEmpty());
    }
}