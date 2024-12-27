package com.example.simpleloginapp;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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

    // Function for saving "username":"highscore" key pairs only to a JSON file
//    public static boolean saveHighScores(Context context, String username, int highScore) throws IOException {
//        // Log the high score
//        Log.d("JsonProcessor", "High score file content: " + new BufferedReader(new FileReader(new File(context.getFilesDir(), "highScores.json"))).readLine());
//        // Create a new highScore object
//        highScore newHighScore = new highScore(username, highScore);
//
//        Gson gson = new Gson();
//        String jsonString = gson.toJson(newHighScore);
//
//        // Save the high score to the file
//        try {
//            File file = new File(context.getFilesDir(), "highScores.json");
//
//            // Read existing data if the file exists
//            highScore existingHighScore = new highScore("", 0);
//            if (file.exists()) {
//                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//                    StringBuilder sb = new StringBuilder();
//                    String line;
//                    while ((line = br.readLine()) != null) {
//                        sb.append(line);
//                    }
//                    String existingJsonString = sb.toString();
//
//                    if (!existingJsonString.isEmpty()) { // Check if the file is not empty
//                        existingHighScore = gson.fromJson(existingJsonString, highScore.class);
//                    }
//                } catch (IOException e) {
//                    Log.e("JsonProcessor", "Error reading existing data from file", e);
//                }
//            }
//
//            // Check if the user already has a high score
//            if (existingHighScore.username.equals(username)) {
//                // Update the high score if the new score is higher
//                if (highScore > existingHighScore.highScore) {
//                    existingHighScore.highScore = highScore;
//                    FileWriter writer = new FileWriter(file);
//                    gson.toJson(existingHighScore, writer);
//                    writer.flush();
//                    writer.close();
//                    return true; // Success
//
//                } else {
//                    return false; // Do not update the high score
//                }
//            } else {
//                // Add the new high score to the file
//                // Combine existing and new objects
//                FileWriter writer = new FileWriter(file);
//                gson.toJson(newHighScore, writer);
//                writer.flush();
//                writer.close();
//                return true; // Success
//            }
//
//        } catch (IOException e) {
//            Log.e("JsonProcessor", "Error saving JSON", e);
//            return false; // Failure
//        }
//
//    }


    public static <T> boolean saveHighScores(Context context, String fileName, List<T> newObjects, String mode) {
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
                    Log.d("JsonProcessor", "Existing data: " + jsonString);

                    if (!jsonString.isEmpty()) { // Check if the file is not empty
                        Type listType = TypeToken.getParameterized(ArrayList.class, newObjects.get(0).getClass()).getType();
                        Gson gson = new Gson();
                        existingObjects = gson.fromJson(jsonString, listType);

                    }
                } catch (IOException e) {
                    Log.e("JsonProcessor", "Error reading existing data from file", e);
                }
            }

            // Check if the user already has a high score
            // Get the username from the first object in the list
            String username = (String) newObjects.get(0).getClass().getDeclaredField("username").get(newObjects.get(0));
            // Get the high score from the first object in the list
            int highScore = (int) newObjects.get(0).getClass().getDeclaredField("highScore").get(newObjects.get(0));
            // Get the highScores data
            int existingHighScore = loadHighScores(context, username);
            // If the existing highScore is 0, it means that the user does not have a high score yet
            if (existingHighScore == 0) {
                // Add the new high score to the file
                // Combine existing and new objects
                existingObjects.addAll(newObjects);

                // Write the updated list to the file
                FileWriter writer = new FileWriter(file);
                Gson gson = new Gson();
                gson.toJson(existingObjects, writer);
                writer.flush();
                writer.close();

                return true; // Success
            } else {
                // Update the high score if the new score is higher
                if (highScore > existingHighScore) {
                    // Update the high score
                    // Look for the existing high score object in the list
                    for (T object : existingObjects) {
                        if (object.getClass().getDeclaredField("username").get(object).equals(username)) {
                            // Update the high score
                            object.getClass().getDeclaredField("highScore").set(object, highScore);
                        }
                    }

                    // Write the updated list to the file
                    FileWriter writer = new FileWriter(file);
                    Gson gson = new Gson();
                    gson.toJson(existingObjects, writer);
                    writer.flush();
                    writer.close();

                    return true; // Success
                } else {
                    return false; // Do not update the high score
                }
            }





        } catch (IOException e) {
            Log.e("JsonProcessor", "Error saving JSON", e);
            return false; // Failure
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public static int loadHighScores(Context context, String username) {
        try {
            File file = new File(context.getFilesDir(), "highScores.json");

            // Handle existing file - check if the format is correct - if not, clear the file
            if (file.exists()) {
                try {
                    // Check file content
                    String content = new BufferedReader(new FileReader(file)).readLine();
                    if (content.equals("{}")) {
                        Log.e("JsonProcessor", "File is empty");
                        // Clear the file
                        FileWriter writer = new FileWriter(file);
                        writer.write("[]");
                        writer.close();
                    } else if (content.isEmpty()) {
                        Log.e("JsonProcessor", "File is null or empty");
                        // Clear the file
                        FileWriter writer = new FileWriter(file);
                        writer.write("[]");
                        writer.close();

                    }
                } catch (Exception e) {
                    Log.e("JsonProcessor", "Error reading JSON", e);
                    String fileContent = new BufferedReader(new FileReader(file)).readLine();
                    Log.e("JsonProcessor", "File content: " + fileContent);
                    // Clear the file
                    FileWriter writer = new FileWriter(file);
                    writer.write("[]");
                    writer.close();
                }
            } else {
                // Create a new file
                if (!file.createNewFile()) {
                    Log.e("JsonProcessor", "Error creating JSON file");
                    return 0; // Failure
                }
            }

            // Read the high score from the file
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                String jsonString = sb.toString();

                Gson gson = new Gson();
                highScore highScore = gson.fromJson(jsonString, highScore.class);

                if (highScore.username.equals(username)) {
                    return Integer.parseInt(highScore.highScore);
                } else {
                    return 0;
                }

            } catch (IOException e) {
                Log.e("JsonProcessor", "Error reading JSON", e);
                return 0; // Handle the exception gracefully (e.g., log the error, return an empty list)
            }

        } catch (IOException e) {
            Log.e("JsonProcessor", "Error saving JSON", e);
            return 0; // Failure
        }
    }

}
