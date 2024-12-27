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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
        // Check if user is signed in (non-null) and update UI accordingly.
        Auth auth = new Auth(this);
        if (auth.getInstance(this) == null || auth.getInstance(this).getUsername() == null) {
            Log.d("User", "User is not signed in.");
        } else {
            Log.d ("User", "User is signed in: " + Objects.requireNonNull(new Auth(this).getInstance(this)).getEmail());
            Intent intent = new Intent(context, saved_game.class);
            startActivity(intent);
            finish();
        }
    }

//    private void createAccount(String email, String password, String confirmPassword, String username) {
//        // Show a ProgressBar
//        ProgressBar progressBar = findViewById(R.id.registrationProgressBar);
//        progressBar.setVisibility(View.VISIBLE);
//
//        // Perform checks
//        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || username.isEmpty()) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setCancelable(false);
//            builder.setMessage("Please fill in all fields.");
//            builder.setTitle("Registration Failed");
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    dialogInterface.dismiss();
//                }
//            });
//            AlertDialog dialog = builder.create();
//            dialog.show();
//            // Hide ProgressBar
//            progressBar.setVisibility(View.GONE);
//            return;
//        }
//        // Password check
//        if (password.length() < 8) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setCancelable(false);
//            builder.setMessage("Password must be at least 6 characters.");
//            builder.setTitle("Registration Failed");
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    dialogInterface.dismiss();
//                }
//            });
//            AlertDialog dialog = builder.create();
//            dialog.show();
//            // Hide ProgressBar
//            progressBar.setVisibility(View.GONE);
//            return;
//        }
//        // Password Strength check - one uppercase, one lowercase, one number, one special character
//        if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$")) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setCancelable(false);
//            builder.setMessage("Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character.");
//            builder.setTitle("Registration Failed");
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    dialogInterface.dismiss();
//                }
//            });
//            AlertDialog dialog = builder.create();
//            dialog.show();
//            // Hide ProgressBar
//            progressBar.setVisibility(View.GONE);
//            return;
//        }
//        // Password match check
//        if (!password.equals(confirmPassword)) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setCancelable(false);
//            builder.setMessage("Passwords do not match.");
//            builder.setTitle("Registration Failed");
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    dialogInterface.dismiss();
//                }
//            });
//            AlertDialog dialog = builder.create();
//            dialog.show();
//            // Hide ProgressBar
//            progressBar.setVisibility(View.GONE);
//            return;
//        }
//        // Minimum username length check
//        if (username.length() < 4) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setCancelable(false);
//            builder.setMessage("Username must be at least 4 characters.");
//            builder.setTitle("Registration Failed");
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    dialogInterface.dismiss();
//                }
//            });
//            AlertDialog dialog = builder.create();
//            dialog.show();
//            // Hide ProgressBar
//            progressBar.setVisibility(View.GONE);
//            return;
//        }
//
//        // Check if Username is already taken - if false means username is taken
//        if (!checkUsername(username)) {
//            return;
//        }
//
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Upload username to Firestore
//                            Map<String, Object> user = new HashMap<>();
//                            user.put("username", username);
//
//                            db.collection("users").document(email)
//                                    .set(user)
//                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            Log.d("Registration_Form", "DocumentSnapshot successfully written!");
//                                            // Hide ProgressBar
//                                            ProgressBar progressBar = findViewById(R.id.registrationProgressBar);
//                                            // Redirect to saved_game
//                                            Intent intent = new Intent(context, saved_game.class);
//                                        }
//                                    })
//                                    .addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            Log.w("Registration_Form", "Error writing document", e);
//                                            // Show AlertDialog
//                                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                                            builder.setCancelable(false);
//                                            builder.setMessage("Registration Failed. Please try again.");
//                                            builder.setTitle("Registration Failed");
//                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialogInterface, int i) {
//                                                    dialogInterface.dismiss();
//                                                }
//                                            });
//                                            AlertDialog dialog = builder.create();
//                                            // Hide ProgressBar
//                                            ProgressBar progressBar = findViewById(R.id.registrationProgressBar);
//                                            progressBar.setVisibility(View.GONE);
//                                            dialog.show();
//
//                                        }
//                                    });
//                        }
//                    }
//                });
//    }
//
//    private void createUserAccount(String email, String password, String confirmPassword, String username) {
//        // Show a ProgressBar
//        ProgressBar progressBar = findViewById(R.id.registrationProgressBar);
//        progressBar.setVisibility(View.VISIBLE);
//
//        // Perform checks
//        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setCancelable(false);
//            builder.setMessage("Please fill in all fields.");
//            builder.setTitle("Registration Failed");
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    dialogInterface.dismiss();
//                }
//            });
//            AlertDialog dialog = builder.create();
//            dialog.show();
//            // Hide ProgressBar
//            progressBar.setVisibility(View.GONE);
//            return;
//        }
//        // Password check
//        if (password.length() < 8) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setCancelable(false);
//            builder.setMessage("Password must be at least 6 characters.");
//            builder.setTitle("Registration Failed");
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    dialogInterface.dismiss();
//                }
//            });
//            AlertDialog dialog = builder.create();
//            dialog.show();
//            // Hide ProgressBar
//            progressBar.setVisibility(View.GONE);
//            return;
//        }
//
//        // Password match check
//        if (!password.equals(confirmPassword)) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setCancelable(false);
//            builder.setMessage("Passwords do not match.");
//            builder.setTitle("Registration Failed");
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    dialogInterface.dismiss();
//                }
//            });
//            AlertDialog dialog = builder.create();
//            dialog.show();
//            // Hide ProgressBar
//            progressBar.setVisibility(View.GONE);
//            return;
//        }
//        // Minimum username length check
//        if (username.length() < 4) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setCancelable(false);
//            builder.setMessage("Username must be at least 4 characters.");
//            builder.setTitle("Registration Failed");
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    dialogInterface.dismiss();
//                }
//            });
//            AlertDialog dialog = builder.create();
//            dialog.show();
//            // Hide ProgressBar
//            progressBar.setVisibility(View.GONE);
//            return;
//        }
//
//        // Check if Username is already taken - if false means username is taken
//        if (!usernameLookup(username)) {
//            return;
//        }
//
//        // Call createAccount from AuthAPI
//        AuthAPI auth = new AuthAPI();
//        if (auth.createAccount(this, email, password, username)) {
//            // Successful registration
//            // Start session
//            if(auth.login(this, email, password).isEmpty()) {
//                Intent intent = new Intent(context, saved_game.class);
//                startActivity(intent);
//                finish();
//            } else {
//                // Failed login
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setCancelable(false);
//                builder.setMessage("Invalid email or password.");
//                builder.setTitle("Login Failed");
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        progressBar.setVisibility(View.GONE);
//                        dialogInterface.dismiss();
//                    }
//                });
//                AlertDialog dialog = builder.create();
//                dialog.show();
//            }
//
//        } else {
//            // Failed registration
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setCancelable(false);
//            builder.setMessage("Registration Failed. Please try again.");
//            builder.setTitle("Registration Failed");
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
//    }
//    // Check if Username is already taken
//    private boolean checkUsername(String username) {
//        // Status
//        AtomicBoolean status = new AtomicBoolean(false);
//        // Check if Username is already taken - get specific document base on username value
//        db.collection("users")
//                .whereEqualTo("username", username)
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    if (queryDocumentSnapshots.isEmpty()) {
//                        // Username is not taken
//                        status.set(true);
//                    } else {
//                        // Username is taken
//                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                        builder.setCancelable(false);
//                        builder.setMessage("Username is already taken.");
//                        builder.setTitle("Registration Form");
//                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.dismiss();
//                            }
//                        });
//                        AlertDialog dialog = builder.create();
//                        // Hide ProgressBar
//                        ProgressBar progressBar = findViewById(R.id.registrationProgressBar);
//                        progressBar.setVisibility(View.GONE);
//                        dialog.show();
//                        status.set(false);
//                    }
//                });
//        return status.get();
//    }
//
//    private boolean usernameLookup (String username) {
//        // Call the function to read JSON
//        File dir = context.getFilesDir();
//        File file = new File(dir, "auth.json");
//        int entries = countEntries(file);
//        // Check if the user exists
//        for (int i = 0; i < entries; i++) {
//            // Get the user
//            User user = JsonSaver.readJson(file, i);
//            // Check if the user exists
//            assert user != null;
//            if (user.getUsername().equals(username)) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setCancelable(false);
//                builder.setMessage("Username is already taken.");
//                builder.setTitle("Registration Form");
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//                AlertDialog dialog = builder.create();
//                // Hide ProgressBar
//                ProgressBar progressBar = findViewById(R.id.registrationProgressBar);
//                progressBar.setVisibility(View.GONE);
//                dialog.show();
//                return false;
//            }
//        }
//        return true;
//    }

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
                createAccountThenPassContext(email.getText().toString(), username.getText().toString(), password.getText().toString(), confirmPassword.getText().toString());
            }
        });

    }
    private void createAccountThenPassContext(String email, String username, String password, String confirmPassword) {
        String status = createAccount(this, email, username, password, confirmPassword);
        if (status.isEmpty()) {
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            finish();
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