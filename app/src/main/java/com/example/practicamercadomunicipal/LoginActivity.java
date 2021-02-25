package com.example.practicamercadomunicipal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText emailSign = (EditText) findViewById(R.id.editTextEmailAddressLogin);
        final EditText passwordSign = (EditText) findViewById(R.id.editTextPasswordLogin);
        Button login = (Button) findViewById(R.id.buttonLoginEmail);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailSign.getText().toString().isEmpty() || passwordSign.getText().toString().isEmpty()) {
                    Toast empty = Toast.makeText(getApplicationContext(), "Te has dejado uno de los campo en blanco", Toast.LENGTH_SHORT);
                    empty.show();
                } else {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(emailSign.getText().toString(), passwordSign.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast correct = Toast.makeText(getApplicationContext(), "Se inició sesion satisfactoriamente", Toast.LENGTH_SHORT);
                                correct.show();
                                Intent nextActivityIntent = new Intent(getApplicationContext(), StoresActivity.class);
                                startActivity(nextActivityIntent);

                            } else {
                                Toast incorrect = Toast.makeText(getApplicationContext(), "No se pudo iniciar sesion, revisa los datos", Toast.LENGTH_SHORT);
                                incorrect.show();
                            }
                        }
                    });
                }
            }
        });
    }
}