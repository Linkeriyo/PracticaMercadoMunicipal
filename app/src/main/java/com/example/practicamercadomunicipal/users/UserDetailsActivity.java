package com.example.practicamercadomunicipal.users;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.practicamercadomunicipal.R;
import com.example.practicamercadomunicipal.data.AppData;
import com.example.practicamercadomunicipal.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class UserDetailsActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Toolbar toolbar;
    TextView nameTextView, emailTextView;
    ImageView imageView;
    Uri imageUri, postImageUri;
    ProgressBar progressBar;
    User user;
    private TextView balanceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        int userNumber = getIntent().getIntExtra("userNumber", 0);
        user = AppData.userList.get(userNumber);

        setupFirebaseVariables();
        setupViews();
        setupToolbar();
        putValues();
    }

    private void setupFirebaseVariables() {
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    private void setupViews() {
        nameTextView = findViewById(R.id.user_details_name_textview);
        emailTextView = findViewById(R.id.user_details_email_textview);
        balanceTextView = findViewById(R.id.user_details_balance_textview);
        imageView = findViewById(R.id.user_details_image_imageview);
        imageView.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, PICK_IMAGE);
        });
        progressBar = findViewById(R.id.user_details_progressbar);
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.user_details_toolbar);
        toolbar.setSubtitle(user.email);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.user_confirm_option) {
                if (isUserOk()) {
                    if (postImageUri == null) {
                        postImageUri = Uri.EMPTY;
                    }
                    double balance = user.balance;
                    try {
                        String balanceString = balanceTextView.getText().toString();
                        if (balanceString.endsWith("€")) {
                            balanceString = balanceString.substring(0, balanceString.length() - 1);
                        }
                        balance = Double.parseDouble(balanceString);
                    } catch (NumberFormatException exception) {
                        Toast.makeText(this,
                                "El balance no es válido, no se ha cambiado.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                    User newUser = new User(
                            user.admin,
                            user.userID,
                            nameTextView.getText().toString(),
                            imageUri,
                            postImageUri,
                            user.email,
                            user.invoices,
                            balance
                    );
                    DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("users");
                    usersReference.child(user.userID).setValue(newUser)
                            .addOnCompleteListener(task -> finish());


                } else {
                    Toast.makeText(this,
                            "Los campos no deben estar vacíos",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            } else if (item.getItemId() == R.id.remove_user_option) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("¿Seguro que quieres eliminar el usuario? Los datos no se podrán recuperar.")
                        .setPositiveButton("Sí", (dialog, which) -> {
                            DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("users");
                            usersReference.child(user.userID).removeValue();

                            if (user.imgStorage != null) {
                                StorageReference imagesReference = FirebaseStorage.getInstance().getReference("images");
                                imagesReference.child(user.imgStorage).delete();
                            }
                            finish();
                        })
                        .setNegativeButton("No", (dialog, which) -> {});
                builder.create().show();
            }
            return true;
        });
    }

    private void putValues() {
        emailTextView.setText(user.email);
        nameTextView.setText(user.name);
        balanceTextView.setText(balanceToString(user.balance));

        if (user.image != null) {
            Glide.with(this).load(user.image).centerCrop().into(imageView);
        }
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

    private boolean isUserOk() {
        return (!nameTextView.getText().toString().isEmpty()
                && !emailTextView.getText().toString().isEmpty());
    }

    private static String balanceToString(double balance) {
        String balanceString = String.valueOf(balance);
        if (balanceString.endsWith(".0")) {
            balanceString = balanceString.substring(0, balanceString.length() - 2);
        }
        return balanceString + "€";
    }
}
