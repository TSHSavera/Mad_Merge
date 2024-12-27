package com.example.simpleloginapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class activity_user extends ComponentActivity {

    Context context = this;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_user), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        startUp();

        mAuth = FirebaseAuth.getInstance();
    }
    private void startUp() {
//        Button savedGameBtn = findViewById(R.id.savedGameBtn);
//        savedGameBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, saved_game.class);
//                startActivity(intent);
//            }
//        });

        findViewById(R.id.logOutBtn).setOnClickListener(v -> {
            // Logout
            new Auth(this).logout(this);
            // Redirect to Welcome Activity
            Intent intent = new Intent(context, Welcome_page.class);
            startActivity(intent);

        });
    }
}
