package com.example.simpleloginapp;

import static com.example.simpleloginapp.JsonProcessor.saveListToJSON;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Auth {
    private static final String AUTH_FILE_NAME = "auth.json";
    private static final String SESSION_FILE_NAME = "session.json";
    private static User instance;
    private static final Gson gson = new GsonBuilder().create();

    public Auth(Context context) {
        // On creation, check if the file exists
        File authFile = new File(context.getFilesDir(), AUTH_FILE_NAME);
        if (!authFile.exists()) {
            try {
                // Create the file
                FileWriter writer = new FileWriter(authFile);
                writer.write("[]");
                writer.close();
            } catch (IOException e) {
                Log.e("Auth", "Error creating auth file", e);
            }
        } else {
            // To ensure that data is available, read the file and log the contents
            Log.d("Auth", "Auth file exists: " + authFile.getAbsolutePath());
            Log.d("Auth", "Auth file contents: " + JsonProcessor.readJsonFromFile(authFile, User.class).toString());
        }

        // Check if the session file exists
        File sessionFile = new File(context.getFilesDir(), SESSION_FILE_NAME);
        if (!sessionFile.exists()) {
            try {
                // Create the file
                FileWriter writer = new FileWriter(sessionFile);
                writer.write("{}");
                writer.close();
            } catch (IOException e) {
                Log.e("Auth", "Error creating session file", e);
            }
        } else {
            // To ensure that data is available, read the file and log the contents
            Log.d("Auth", "Session file exists: " + sessionFile.getAbsolutePath());
            try {
                Log.d("Auth", "Session file contents: " + new BufferedReader(new FileReader(sessionFile)).readLine());
            } catch (Exception e) {
                Log.d("Auth", "Session file is empty");
            }
        }

        // Try to read if the data isn't corrupted or malformed
        try {
            // Using Gson to read the file
            FileReader reader = new FileReader(authFile);
            User[] users = gson.fromJson(reader, User[].class);
            reader.close();
        } catch (Exception e) {
            Log.e("Auth", "Error reading auth file", e);
            // Clear the file
            try {
                FileWriter writer = new FileWriter(authFile);
                writer.write("[]");
                writer.close();
            } catch (IOException ex) {
                Log.e("Auth", "Error clearing auth file", ex);
            }
        }

        // Try to read if the data isn't corrupted or malformed
        try {
            // Try to read the raw data first
            String raw = new BufferedReader(new FileReader(sessionFile)).readLine();
            List<User> users;
            if (raw.isEmpty() || raw.equals("{}")) {
                Log.i("Auth", "Session file is empty");
                users = null;
            } else {
                // Read the session file
                users = JsonProcessor.readJsonFromFile(sessionFile, User.class);
            }

            // If it's not null, then the user is logged in
            if (users != null) {
                // Set the user as the logged in user
                instance = users.get(0);

            }
        } catch (Exception e) {
            Log.e("Auth", "Error reading session file", e);
            // Clear the file
            try {
                FileWriter writer = new FileWriter(sessionFile);
                writer.write("{}");
                writer.close();
                Log.d("Auth", "Session file cleared and fixed");
            } catch (IOException ex) {
                Log.e("Auth", "Error clearing session file", ex);
            }
        }


    }


    public static User getInstance(Context context) {
        // Check if there's a user logged in
        if (instance != null) {
            // Log to the console the value of instance
            Log.d("Auth126", "User logged in: " + instance.getUsername());
            return instance;
        } else {
            // If there's no user logged in, return null
            return null;
        }
    }

    public static String createAccount(Context context, String email, String username, String password, String confirmPassword) {
        // Perform checks
        if (email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            return "Please fill in all fields";
        }
        Log.d("Auth", "All fields filled");

        if (!password.equals(confirmPassword)) {
            return "Passwords do not match";
        }
        Log.d("Auth", "Passwords match");

        // Check the password format and length
        if (password.length() < 8) {
            return "Password must be at least 8 characters long";
        }
        Log.d("Auth", "Password is at least 8 characters long");

        if (!password.matches(".*\\d.*")) {
            return "Password must contain at least one digit";
        }
        Log.d("Auth", "Password contains at least one digit");

        if (!password.matches(".*[a-z].*")) {
            return "Password must contain at least one lowercase letter";
        }
        Log.d("Auth", "Password contains at least one lowercase letter");

        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter";
        }
        Log.d("Auth", "Password contains at least one uppercase letter");

        if (!password.matches(".*[!@#$%^&*].*")) {
            return "Password must contain at least one special character";
        }
        Log.d("Auth", "Password contains at least one special character");

        // Check if the email is valid
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Invalid email address";
        }
        Log.d("Auth", "Email is valid");

        // Check if the username is valid
        if (username.length() < 3) {
            return "Username must be at least 3 characters long";
        }
        Log.d("Auth", "Username is at least 3 characters long");

        // Check if username is taken
        List<User> users = new ArrayList<>();
        try {
            // Read the file
            File file = new File(context.getFilesDir(), AUTH_FILE_NAME);
            users = JsonProcessor.readJsonFromFile(file, User.class);

            // Check if the email is already taken
            for (User user : users) {
                if (user.getEmail().equals(email)) {
                    return "Email is already taken";
                }
            }
            Log.d("Auth", "Email is not taken");

            // Check if the username is already taken
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    return "Username is already taken";
                }
            }
            Log.d("Auth", "Username is not taken");


        } catch (JsonSyntaxException e) {
            Log.e("Auth", "Error parsing JSON", e);
            return "Error parsing JSON";
        }


        // Create a new user
        User submit = new User(email, username, password);
        // Create list
        List<User> submits = new ArrayList<>();
        submits.add(submit);

        // Save the user to the auth file
        boolean status = saveListToJSON(context, AUTH_FILE_NAME, submits, "Multiple");

        if (status) {
            Log.d("Auth", "User saved successfully");
            return "";
        } else {
            Log.e("Auth", "Error saving user");
            return "Error saving user";
        }
    }

    public String login(Context context, String email, String password) {
        // Get the list of users
        List<User> users = new ArrayList<>();
        try {
            // Read the file
            File file = new File(context.getFilesDir(), AUTH_FILE_NAME);
            users = JsonProcessor.readJsonFromFile(file, User.class);
        }  catch (JsonSyntaxException e) {
            Log.e("Auth", "Error parsing JSON", e);
            return "Error parsing JSON";
        }

        // Check if the user exists
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                Log.d("Auth", "User found: " + user.getUsername());
                // Save the user to the session file
                // Create a new user
                User submit = new User(user.getEmail(), user.getUsername(), user.getPassword());
                // Create list
                List<User> submits = new ArrayList<>();
                submits.add(submit);

                // Save the user to the auth file
                boolean status = saveListToJSON(context, SESSION_FILE_NAME, submits);

                if (status) {
                    Log.d("Auth", "User logged in successfully");
                    return "";
                } else {
                    Log.e("Auth", "Error logging in user");
                    return "Error logging in user";
                }
            }
        }
        // Log to console the values of email and password entered and the auth.json file
        Log.d("Auth", "Email: " + email + ", Password: " + password);
        Log.d("Auth", "Users: " + users);
        return "Invalid email or password";
    }

    public String logout(Context context) {
        // Clear the session file
        File sessionFile = new File(context.getFilesDir(), SESSION_FILE_NAME);
        try {
            FileWriter writer = new FileWriter(sessionFile);
            writer.write("{}");
            writer.close();
            Log.d("Auth", "Session file cleared");
            Log.d("Auth", "Session file contents: " + new BufferedReader(new FileReader(sessionFile)).readLine());
            // Clear the instance
            instance = null;
            return "";

        } catch (IOException e) {
            Log.e("Auth", "Error clearing session file", e);
            return "Error clearing session file";
        }
    }
}
