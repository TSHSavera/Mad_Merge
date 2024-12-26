package com.example.simpleloginapp;



import static android.content.ContentValues.TAG;

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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


public class Registration_Form extends AppCompatActivity {


    Context context = this;
    TextView loginLink;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        // Firebase Auth
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(Registration_Form.this, game.class);
            startActivity(intent);
            finish();
        }
    }

    private void createAccount(String email, String password, String confirmPassword, String username) {
        // Show a ProgressBar
        ProgressBar progressBar = findViewById(R.id.registrationProgressBar);
        progressBar.setVisibility(View.VISIBLE);

        // Perform checks
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || username.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setMessage("Please fill in all fields.");
            builder.setTitle("Registration Failed");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            // Hide ProgressBar
            progressBar.setVisibility(View.GONE);
            return;
        }
        // Password check
        if (password.length() < 8) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setMessage("Password must be at least 6 characters.");
            builder.setTitle("Registration Failed");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            // Hide ProgressBar
            progressBar.setVisibility(View.GONE);
            return;
        }
        // Password Strength check - one uppercase, one lowercase, one number, one special character
        if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setMessage("Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character.");
            builder.setTitle("Registration Failed");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            // Hide ProgressBar
            progressBar.setVisibility(View.GONE);
            return;
        }
        // Password match check
        if (!password.equals(confirmPassword)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setMessage("Passwords do not match.");
            builder.setTitle("Registration Failed");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            // Hide ProgressBar
            progressBar.setVisibility(View.GONE);
            return;
        }
        // Minimum username length check
        if (username.length() < 4) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setMessage("Username must be at least 4 characters.");
            builder.setTitle("Registration Failed");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            // Hide ProgressBar
            progressBar.setVisibility(View.GONE);
            return;
        }

        // Check if Username is already taken
        if (!checkUsername(username)) {
            // Hide ProgressBar
            progressBar.setVisibility(View.GONE);
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Upload username to Firebase Firestore
                        Map<String, Object> user = new HashMap<>();
                        user.put("username", username);

                        db.collection("users")
                                .document(email)
                                .set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Registration_Form", "DocumentSnapshot successfully written!");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setCancelable(false);
                                        builder.setMessage("Registration Successful! Welcome to the Game.");
                                        builder.setTitle("Registration Form");
                                        builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent = new Intent(context, MainActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                        // Hide ProgressBar
                                        progressBar.setVisibility(View.GONE);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("Registration_Form", "Error writing document", e);
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setCancelable(false);
                                        builder.setMessage("Error saving username. Please try again.");
                                        builder.setTitle("Registration Failed");
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        });
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                        // Hide ProgressBar
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setCancelable(false);
                        builder.setMessage("Registration Failed. Please try again.");
                        builder.setTitle("Registration Form");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        // Hide ProgressBar
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    // Check if Username is already taken
    private boolean checkUsername(String username) {
        // Status
        AtomicBoolean status = new AtomicBoolean(false);
        // Check if Username is already taken - get specific document base on username value
        db.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        // Username is not taken
                        status.set(true);
                    } else {
                        // Username is taken
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setCancelable(false);
                        builder.setMessage("Username is already taken.");
                        builder.setTitle("Registration Form");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        status.set(false);
                    }
                });
        return status.get();
    }
    private void startUp() {
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
                createAccount(email.getText().toString(), password.getText().toString(), confirmPassword.getText().toString(), username.getText().toString());
            }
        });
    }
}