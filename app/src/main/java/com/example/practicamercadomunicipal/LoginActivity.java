package com.example.practicamercadomunicipal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Si el usuario está logeado, procede a la siguiente actividad.
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            nextActivity();
        }

        setContentView(R.layout.activity_login);

        final EditText emailSign = findViewById(R.id.editTextEmailAddressLogin);
        final EditText passwordSign = findViewById(R.id.editTextPasswordLogin);
        final TextView create = findViewById((R.id.textViewRegister));
        Button login = findViewById(R.id.buttonLoginEmail);

        login.setOnClickListener(v -> {
            if (emailSign.getText().toString().isEmpty() || passwordSign.getText().toString().isEmpty()) {
                Toast empty = Toast.makeText(getApplicationContext(), "Has dejado uno de los campo en blanco", Toast.LENGTH_SHORT);
                empty.show();
            } else {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailSign.getText().toString(), passwordSign.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast correct = Toast.makeText(getApplicationContext(), "Inicio de sesión satisfactorio", Toast.LENGTH_SHORT);
                            correct.show();
                            nextActivity();
                        } else {
                            Toast incorrect = Toast.makeText(getApplicationContext(), "No se pudo iniciar sesion, revisa los campos", Toast.LENGTH_SHORT);
                            incorrect.show();
                        }
                    }
                });
            }
        });

        create.setOnClickListener(v -> {
            Intent nextActivityIntent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(nextActivityIntent);
        });
    }

    public void nextActivity() {
        Intent nextActivityIntent = new Intent(getApplicationContext(), StoresActivity.class);
        startActivity(nextActivityIntent);
    }
}