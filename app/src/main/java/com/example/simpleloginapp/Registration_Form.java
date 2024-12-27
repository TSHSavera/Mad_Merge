package com.example.simpleloginapp;

import static com.example.simpleloginapp.Auth.createAccount;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.Objects;

public class Registration_Form extends AppCompatActivity {
    Context context = this;
    TextView loginLink;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration_form);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_register), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Start App
        startUp();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        Auth auth = new Auth(this);
        if (Auth.getInstance(this) == null || Auth.getInstance(this).getUsername() == null) {
            Log.d("User", "User is not signed in.");
        } else {
            new Auth(this);
            Log.d ("User", "User is signed in: " + Objects.requireNonNull(Auth.getInstance(this)).getEmail());
            Intent intent = new Intent(context, game.class);
            startActivity(intent);
            finish();
        }
    }

    private void startUp() {
        // Check if there's a user signed in
        if (Auth.getInstance(this) != null && Auth.getInstance(this).getUsername() != null) {
            Intent intent = new Intent(context, game.class);
            startActivity(intent);
            finish();
        }
        loginLink = findViewById(R.id.loginLink);
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false);
                builder.setMessage("You will be redirected to the Login Page.");
                builder.setTitle("Login Form");
                builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        Button registerButton = findViewById(R.id.registerBtn);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText email = findViewById(R.id.emailET);
                EditText password = findViewById(R.id.passwordET);
                EditText confirmPassword = findViewById(R.id.CFpasswordET);
                EditText username = findViewById(R.id.usernameET);
                createAccountThenPassContext(email.getText().toString(), username.getText().toString(), password.getText().toString(), confirmPassword.getText().toString());
            }
        });

    }
    private void createAccountThenPassContext(String email, String username, String password, String confirmPassword) {
        String status = createAccount(this, email, username, password, confirmPassword);
        if (status.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setMessage("Registration Successful. You will be redirected to the Login Page.");
            builder.setTitle("Registration Success");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setMessage(status);
            builder.setTitle("Registration Failed");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}