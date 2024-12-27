package com.example.simpleloginapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    Context context = this;
    TextView registerLink;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @SuppressLint({"ResourceAsColor", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        startUp();

        mAuth = FirebaseAuth.getInstance();

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
            Intent intent = new Intent(context, saved_game.class);
            startActivity(intent);
            finish();
        }
    }

//    private void loginUser(String email, String password) {
//        // Show progress bar
//        progressBar.setVisibility(View.VISIBLE);
//
//        // Perform checks
//        if (email.isEmpty() || password.isEmpty()) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setCancelable(false);
//            builder.setMessage("Please fill in all fields.");
//            builder.setTitle("Login Failed");
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    progressBar.setVisibility(View.GONE);
//                    dialogInterface.dismiss();
//                }
//            });
//            AlertDialog dialog = builder.create();
//            dialog.show();
//            return;
//        }
//
//        // Authenticate user
//        AuthAPI auth = new AuthAPI();
//        if (auth.login(this, email, password).isEmpty()) {
//            // Successful login
//            Intent intent = new Intent(context, saved_game.class);
//            startActivity(intent);
//            finish();
//        } else {
//            // Failed login
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setCancelable(false);
//            builder.setMessage("Invalid email or password.");
//            builder.setTitle("Login Failed");
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    progressBar.setVisibility(View.GONE);
//                    dialogInterface.dismiss();
//                }
//            });
//            AlertDialog dialog = builder.create();
//            dialog.show();
//        }
//
//
//    }


    private void startUp() {
        progressBar = findViewById(R.id.loginProgressBar);
        progressBar.setVisibility(View.GONE);
        registerLink = findViewById(R.id.registerLink);
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false);
                builder.setMessage("You will be redirected to the Registration Page.");
                builder.setTitle("Registration Form");
                builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(context, Registration_Form.class);
                        startActivity(intent);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        Button loginButton = findViewById(R.id.loginBtn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText email = findViewById(R.id.usernameET);
                EditText password = findViewById(R.id.passwordET);
                loginThenPassContext(email.getText().toString(), password.getText().toString());
            }
        });
    }
    private void loginThenPassContext(String email, String password) {
        String status = new Auth(this).login(this,email, password);
        if (status.isEmpty()) {
            Intent intent = new Intent(context, saved_game.class);
            startActivity(intent);
            finish();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setMessage(status);
            builder.setTitle("Login Failed");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    progressBar.setVisibility(View.GONE);
                    dialogInterface.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}