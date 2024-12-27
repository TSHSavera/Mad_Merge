package com.example.simpleloginapp;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonProcessor {
    public static <User> List<User> readJsonFromFile(File file, Class<User> classOfT) {
        List<User> objects = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String jsonString = sb.toString();

            Gson gson = new Gson();
            Type listType = TypeToken.getParameterized(ArrayList.class, classOfT).getType();
            objects = gson.fromJson(jsonString, listType);

        } catch (IOException e) {
            Log.e("JsonProcessor", "Error reading JSON", e);
            // Handle the exception gracefully (e.g., log the error, return an empty list)
            return new ArrayList<>();
        }

        return objects;
    }

    public static <T> boolean saveListToJSON(Context context, String fileName, List<T> objects) {
        Log.d("JsonProcessor", "Saving to file: " + fileName);
        try {
            File file = new File(context.getFilesDir(), fileName);

            // Handle existing file - check if the format is correct - if not, clear the file
            if (file.exists()) {
                try {
                    // Check file content
                    String content = new BufferedReader(new FileReader(file)).readLine();
                    if (content.equals("{}")) {
                        Log.e("JsonProcessor", "File is empty");
                    } else if (content.isEmpty()) {
                        Log.e("JsonProcessor", "File is null or empty");
                        // Clear the file
                        FileWriter writer = new FileWriter(file);
                        writer.write("{}");
                        writer.close();
                    }
                } catch (Exception e) {
                    Log.e("JsonProcessor", "Error reading JSON", e);
                    String fileContent = new BufferedReader(new FileReader(file)).readLine();
                    Log.e("JsonProcessor", "File content: " + fileContent);
                    // Clear the file
                    FileWriter writer = new FileWriter(file);
                    writer.write("{}");
                    writer.close();
                }
            } else {
                // Create a new file
                if (!file.createNewFile()) {
                    Log.e("JsonProcessor", "Error creating JSON file");
                    return false; // Failure
                }
            }
            Log.d("JsonProcessor", "Writing to file: " + file.getAbsolutePath());

            FileWriter writer = new FileWriter(file);
            Gson gson = new Gson();
            gson.toJson(objects, writer);
            writer.flush();
            writer.close();

            return true; // Success

        } catch (IOException e) {
            Log.e("JsonProcessor", "Error saving JSON", e);
            return false; // Failure
        }
    }

    public static <T> boolean saveListToJSON(Context context, String fileName, List<T> newObjects, String mode) {
        Log.d("JsonProcessor", "Saving to file: " + fileName);
        try {
            File file = new File(context.getFilesDir(), fileName);

            // Read existing data if the file exists
            List<T> existingObjects = new ArrayList<>();
            if (file.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    String jsonString = sb.toString();

                    if (!jsonString.isEmpty()) { // Check if the file is not empty
                        Type listType = TypeToken.getParameterized(ArrayList.class, newObjects.get(0).getClass()).getType();
                        Gson gson = new Gson();
                        existingObjects = gson.fromJson(jsonString, listType);
                    }
                } catch (IOException e) {
                    Log.e("JsonProcessor", "Error reading existing data from file", e);
                }
            }

            // Combine existing and new objects
            existingObjects.addAll(newObjects);

            // Write the updated list to the file
            FileWriter writer = new FileWriter(file);
            Gson gson = new Gson();
            gson.toJson(existingObjects, writer);
            writer.flush();
            writer.close();

            return true; // Success

        } catch (IOException e) {
            Log.e("JsonProcessor", "Error saving JSON", e);
            return false; // Failure
        }
    }
}
