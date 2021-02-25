package com.example.practicamercadomunicipal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.practicamercadomunicipal.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //Event Analytics
        final FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString("message", "Full Firebase integration");
        analytics.logEvent("InitScreen", bundle);

        /*Button accept = findViewById(R.id.buttonAcceptCreate);
        final EditText email = findViewById(R.id.editTextEmailAddressCreate);
        final EditText password = findViewById(R.id.editTextPasswordCreate);
        final EditText name = findViewById(R.id.editTextName);
        final EditText phone = findViewById(R.id.editTextPhone);
        final EditText desc = findViewById(R.id.editTextDesc);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty() || name.getText().toString().isEmpty() || phone.getText().toString().isEmpty() || desc.getText().toString().isEmpty()) {
                    Toast empty = Toast.makeText(getApplicationContext(), "You have left a blank", Toast.LENGTH_SHORT);
                    empty.show();
                } else {
                    final FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast correct = Toast.makeText(getApplicationContext(), "User created successfully", Toast.LENGTH_SHORT);
                                correct.show();
                                newUser(auth.getCurrentUser().getUid(), name.getText().toString(), phone.getText().toString(), desc.getText().toString());
                                finish();
                            } else {
                                Toast incorrect = Toast.makeText(getApplicationContext(), "User could not be created, check email and password", Toast.LENGTH_SHORT);
                                incorrect.show();
                            }
                        }
                    });
                }
            }
        });
    }

    public void newUser(String id, String name, String mobile, String desc) {
        User user = new User(id, name, mobile, desc);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(id).setValue(user);*/
    }
}