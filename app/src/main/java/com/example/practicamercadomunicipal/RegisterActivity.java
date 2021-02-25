package com.example.practicamercadomunicipal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.practicamercadomunicipal.models.Invoice;
import com.example.practicamercadomunicipal.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    String imageUri = "";
    ImageView imageView;
    ProgressBar progressBar;
    private static final int PICK_IMAGE = 1;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //Event Analytics
        final FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString("message", "Full Firebase integration");
        analytics.logEvent("InitScreen", bundle);

        TextView accept = findViewById(R.id.textViewCreate);
        final EditText email = findViewById(R.id.editTextEmailAddressRegister);
        final EditText password = findViewById(R.id.editTextPasswordRegister);
        final EditText name = findViewById(R.id.editTextNameRegister);
        final EditText saldo = findViewById(R.id.editTextBalanceRegister);
        List<Invoice> invoiceList = new ArrayList<Invoice>();
        imageView = findViewById(R.id.imageButton);
        imageView.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, PICK_IMAGE);
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty() || name.getText().toString().isEmpty() || saldo.getText().toString().isEmpty()) {
                    Toast empty = Toast.makeText(getApplicationContext(), "Dejaste algun campo en blanco", Toast.LENGTH_SHORT);
                    empty.show();
                } else {
                    final FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast correct = Toast.makeText(getApplicationContext(), "El usuario se creó satisfactoriamente", Toast.LENGTH_SHORT);
                                correct.show();
                                newUser(true, auth.getCurrentUser().getUid(), name.getText().toString(), imageUri, email.getText().toString(), invoiceList, Double.parseDouble(saldo.getText().toString()));
                                finish();
                            } else {
                                Toast incorrect = Toast.makeText(getApplicationContext(), "El usuario no se pudo crear, revise que la contraseña sea mas de 5 dígitos y el correo este bien escrito", Toast.LENGTH_SHORT);
                                incorrect.show();
                            }
                        }
                    });
                }
            }
        });
    }

    public void newUser(boolean admin, String id, String name, String image, String email, List<Invoice> invoiceList, Double saldo) {
        User user = new User(admin, id, name, image, email, invoiceList, saldo);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(id).setValue(user);
    }

    private void progressBar() {
        progressBar = findViewById(R.id.new_store_progressbar);
        progressBar.setProgress(200);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setMax(1000);
        ObjectAnimator.ofInt(progressBar, "progress", 999)
                .setDuration(2000)
                .start();
    }

    private void putImage(String imageUri) {
        Glide.with(this)
                .load(imageUri)
                .centerCrop()
                .into(imageView);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            assert data != null;
            progressBar();
            Uri uri = data.getData();
            StorageReference fileReference = storage.getReference("images").child(uri.getLastPathSegment());
            fileReference.putFile(uri).continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                return fileReference.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    imageUri = Objects.requireNonNull(task.getResult()).toString();
                    putImage(imageUri);
                }
            });
        }
    }
}