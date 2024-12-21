package com.example.simpleloginapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Welcome_page extends AppCompatActivity {

    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        startUp();
    }

    private void startUp() {
        TextView username = findViewById(R.id.nameTV);
        String firstName = getIntent().getStringExtra("FIRST_NAME");
        String lastName = getIntent().getStringExtra("LAST_NAME");

        username.setText(firstName + " " + lastName + "!");
        Toast.makeText(this, "Login Successfully!", Toast.LENGTH_LONG).show();

        // Retrieve the byte array from the Intent
        byte[] byteArray = getIntent().getByteArrayExtra("image_data");

        if (byteArray != null) {
            // Convert byte array to Bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            // Display the Bitmap in the ImageView
            ((ImageView) findViewById(R.id.displayImageView)).setImageBitmap(bitmap);
        }

        findViewById(R.id.logoutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}