package com.example.practicamercadomunicipal;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {
    private final static String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    //      __
    //  ___( o)> (cuak)
    //  \ <_. )
    //   `---'
}
