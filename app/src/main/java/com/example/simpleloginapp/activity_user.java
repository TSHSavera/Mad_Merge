package com.example.simpleloginapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class activity_user extends ComponentActivity {

    Context context = this;
    String username = Auth.getInstance(this).getUsername();


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
    }
    private void startUp() {
        // Check if there is a user signed in
        if (Auth.getInstance(this) == null || Auth.getInstance(this).getUsername() == null) {
            // Redirect to Welcome Activity
            Intent intent = new Intent(context, Welcome_page.class);
            startActivity(intent);
        }

        // Change the username to the current user
        android.widget.TextView username = findViewById(R.id.username);
        username.setText(this.username);


        findViewById(R.id.logOutBtn).setOnClickListener(v -> {
            // Logout
            new Auth(this).logout(this);
            // Redirect to Welcome Activity
            Intent intent = new Intent(context, Welcome_page.class);
            startActivity(intent);

        });
    }
}
