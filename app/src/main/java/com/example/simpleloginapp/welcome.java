package com.example.simpleloginapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        startUp();
    }

    private void startUp() {
        Bundle extras = getIntent().getExtras();

        // Put the received data from the login activity to textview with id "name"
        String name = Objects.requireNonNull(extras).getString("name");
        ((TextView) findViewById(R.id.nameTV)).setText(name);

        // Get the passed image data and convert it to bitmap to display it in the imageview
        byte[] image_data = extras.getByteArray("image");
        Bitmap pfp = BitmapFactory.decodeByteArray(image_data, 0, Objects.requireNonNull(image_data).length);
        ((ImageView) findViewById(R.id.displayImageView)).setImageBitmap(pfp);


        // Add a click listener to the back to home button
        findViewById(R.id.logoutBtn).setOnClickListener(v -> {
            finish();
        });

    }
}