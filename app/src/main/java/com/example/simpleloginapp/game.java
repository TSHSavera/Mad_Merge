package com.example.simpleloginapp;

import static com.example.simpleloginapp.JsonProcessor.saveHighScores;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class game extends ComponentActivity {

    Context context = this;
    ImageView user_icon;
    Button saveBtn;
    Button resignBtn;
    String username = Auth.getInstance(this).getUsername();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_game), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        startUp();

    }
    private void startUp() {
        // Load high scores to the screen
        TextView bestScoreLabel = findViewById(R.id.bestScoreLabel);
        try {
            int bestScore = JsonProcessor.getHighScore(this);
            bestScoreLabel.setText(String.valueOf(bestScore));
        } catch (Exception e) {
            Log.e("GameEngine", "Error loading high scores", e);
            // Set the best score to 0
            bestScoreLabel.setText("0");
        }

        // Set the username to the username display element
        TextView usernameLabel = findViewById(R.id.username);
        usernameLabel.setText(username);

        user_icon = findViewById(R.id.user_icon);
        user_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, activity_user.class);
                startActivity(intent);
            }
        });

//        saveBtn = findViewById(R.id.saveBtn);
//        saveBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle("Save Game");
//                builder.setCancelable(false);
//                builder.setMessage("Are you sure you want to save this?");
//                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//
//                AlertDialog dialog = builder.create();
//                dialog.show();
//            }
//        });


        resignBtn = findViewById(R.id.resignBtn);
        resignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Resign");
                builder.setCancelable(false);
                builder.setMessage("Are you sure you want to leave?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            saveScore(username, R.id.bestScoreLabel);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // Get all the buttons
        Button[] buttons = new Button[16];
        buttons[0] = findViewById(R.id.button1);
        buttons[1] = findViewById(R.id.button2);
        buttons[2] = findViewById(R.id.button3);
        buttons[3] = findViewById(R.id.button4);
        buttons[4] = findViewById(R.id.button5);
        buttons[5] = findViewById(R.id.button6);
        buttons[6] = findViewById(R.id.button7);
        buttons[7] = findViewById(R.id.button8);
        buttons[8] = findViewById(R.id.button9);
        buttons[9] = findViewById(R.id.button10);
        buttons[10] = findViewById(R.id.button11);
        buttons[11] = findViewById(R.id.button12);
        buttons[12] = findViewById(R.id.button13);
        buttons[13] = findViewById(R.id.button14);
        buttons[14] = findViewById(R.id.button15);
        buttons[15] = findViewById(R.id.button16);

        // Store the selected button
        Button[] selectedButtonA = {null};
        Button[] selectedButtonB = {null};

        // Get the score text
        TextView scoreText = findViewById(R.id.scoreLabel);

        // Choose a random button to add a value
        int random = (int) (Math.random() * 16);
        // Get the button
        Button randomButton = buttons[random];
        // Check if the button is empty
        if (randomButton.getText().toString().isEmpty()) {
            // Set the button to the current player
            randomButton.setText("0");
        }
        // Check if the button has a value - if yes, choose another button
        while (!randomButton.getText().toString().equals("0")) {
            random = (int) (Math.random() * 16);
            randomButton = buttons[random];
        }
        // If we've successfully found a button, set the value to either 2 or 4
        int value = (int) (Math.random() * 2) == 0 ? 2 : 4;
        // Set the value
        randomButton.setText(String.valueOf(value));

        // Do it again
        random = (int) (Math.random() * 16);
        // Get the button
        randomButton = buttons[random];
        // Check if the button is empty
        if (randomButton.getText().toString().isEmpty()) {
            // Set the button to the current player
            randomButton.setText("0");
        }
        // Check if the button has a value - if yes, choose another button
        while (!randomButton.getText().toString().equals("0")) {
            random = (int) (Math.random() * 16);
            randomButton = buttons[random];
        }
        // If we've successfully found a button, set the value to either 2 or 4
        value = (int) (Math.random() * 2) == 0 ? 2 : 4;
        // Set the value
        randomButton.setText(String.valueOf(value));



        for (Button button : buttons) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Check if the button is empty
                    if (button.getText().toString().isEmpty()) {
                        // Set the button to the current player
                        button.setText("0");
                    }

                    // On click, store this button as the selected button
                    if (selectedButtonA[0] == null) {
                        selectedButtonA[0] = button;
                        // Set the style to the selected
                        button.setBackgroundResource(R.drawable.custom_cube_btn_active);
                        button.setTextColor(getResources().getColor(R.color.md_theme_onSecondary));
                    } else {
                        selectedButtonB[0] = button;
                        // Set the style to the selected
                        button.setBackgroundResource(R.drawable.custom_cube_btn_active);
                        button.setTextColor(getResources().getColor(R.color.md_theme_onSecondary));
                        // Check if selectedButtonA is the same as selectedButtonB
                        if (selectedButtonA[0].equals(selectedButtonB[0])) {
                            // Set the style to the default
                            selectedButtonA[0].setBackgroundResource(R.drawable.custom_cube_btn);
                            selectedButtonB[0].setBackgroundResource(R.drawable.custom_cube_btn);
                            selectedButtonA[0].setTextColor(getResources().getColor(R.color.md_theme_onPrimary));
                            selectedButtonB[0].setTextColor(getResources().getColor(R.color.md_theme_onPrimary));
                            // If they are the same, clear the selected buttons
                            selectedButtonA[0] = null;
                            selectedButtonB[0] = null;
                            return;
                        }
                        // Since this is the 2nd button, check if they are the same
                        if (selectedButtonA[0].getText().toString().equals(selectedButtonB[0].getText().toString())) {
                            // Store the values of the buttons
                            String valueA = selectedButtonA[0].getText().toString();
                            String valueB = selectedButtonB[0].getText().toString();

                            // Add two values
                            int sum = Integer.parseInt(valueA) + Integer.parseInt(valueB);

                            // If they are the same, remove the text from first button
                            selectedButtonA[0].setText("0");
                            // Then set the text of the second button to the sum
                            selectedButtonB[0].setText(String.valueOf(sum));

                            // Add the sum to the score
                            int score = Integer.parseInt(scoreText.getText().toString());
                            score += sum;

                            // Set the new score
                            scoreText.setText(String.valueOf(score));

                            // Clear the selected buttons
                            selectedButtonA[0] = null;
                            selectedButtonB[0] = null;

                            // Select a random button to add a new value
                            int random = (int) (Math.random() * 16);
                            // Get the button
                            Button randomButton = buttons[random];
                            // Check if the button is empty
                            if (randomButton.getText().toString().isEmpty()) {
                                // Set the button to the current player
                                randomButton.setText("0");
                            }
                            // Check if the button has a value - if yes, choose another button
                            while (!randomButton.getText().toString().equals("0")) {
                                random = (int) (Math.random() * 16);
                                randomButton = buttons[random];
                                // Add a check to see if the board is full
                                boolean isFull = true;
                                for (Button button : buttons) {
                                    if (button.getText().toString().equals("0")) {
                                        isFull = false;
                                        break;
                                    }
                                }
                            }

                            // Assign either 2 or 4 to the button - 50% chance
                            int value = (int) (Math.random() * 2) == 0 ? 2 : 4;
                            // Set the value
                            randomButton.setText(String.valueOf(value));

                            // Reset the style of the buttons
                            for (Button button : buttons) {
                                button.setBackgroundResource(R.drawable.custom_cube_btn);
                                button.setTextColor(getResources().getColor(R.color.md_theme_onPrimary));
                            }

                        }
                        // If there are no same numbers on the board, allow the user to select a button with 0 value then move the value of the selected button to the new button
                        else if (selectedButtonA[0].getText().toString().equals("0")) {
                            // Store the value of the second button
                            String valueB = selectedButtonB[0].getText().toString();
                            // Set the value of the first button to the value of the second button
                            selectedButtonA[0].setText(valueB);
                            // Clear the value of the second button
                            selectedButtonB[0].setText("0");
                            // Clear the selected buttons
                            selectedButtonA[0] = null;
                            selectedButtonB[0] = null;
                            // Select a random button to add a new value
                            int random = (int) (Math.random() * 16);
                            // Get the button
                            Button randomButton = buttons[random];
                            // Check if the button is empty
                            if (randomButton.getText().toString().isEmpty()) {
                                // Set the button to the current player
                                randomButton.setText("0");
                            }
                            // Check if the button has a value - if yes, choose another button
                            while (!randomButton.getText().toString().equals("0")) {
                                random = (int) (Math.random() * 16);
                                randomButton = buttons[random];
                                // Add a check to see if the board is full
                                boolean isFull = true;
                                for (Button button : buttons) {
                                    if (button.getText().toString().equals("0")) {
                                        isFull = false;
                                        break;
                                    }
                                }
                            }

                            // Assign either 2 or 4 to the button - 50% chance
                            int value = (int) (Math.random() * 2) == 0 ? 2 : 4;
                            // Set the value
                            randomButton.setText(String.valueOf(value));

                            // Reset the style of the buttons
                            for (Button button : buttons) {
                                button.setBackgroundResource(R.drawable.custom_cube_btn);
                                button.setTextColor(getResources().getColor(R.color.md_theme_onPrimary));
                            }

                        } else if (selectedButtonB[0].getText().toString().equals("0")) {
                            // Store the value of the first button
                            String valueA = selectedButtonA[0].getText().toString();
                            // Set the value of the second button to the value of the first button
                            selectedButtonB[0].setText(valueA);
                            // Clear the value of the first button
                            selectedButtonA[0].setText("0");
                            // Clear the selected buttons
                            selectedButtonA[0] = null;
                            selectedButtonB[0] = null;
                            // Select a random button to add a new value
                            int random = (int) (Math.random() * 16);
                            // Get the button
                            Button randomButton = buttons[random];
                            // Check if the button is empty
                            if (randomButton.getText().toString().isEmpty()) {
                                // Set the button to the current player
                                randomButton.setText("0");
                            }
                            // Check if the button has a value - if yes, choose another button
                            while (!randomButton.getText().toString().equals("0")) {
                                random = (int) (Math.random() * 16);
                                randomButton = buttons[random];
                                // Add a check to see if the board is full
                                boolean isFull = true;
                                for (Button button : buttons) {
                                    if (button.getText().toString().equals("0")) {
                                        isFull = false;
                                        break;
                                    }
                                }
                            }

                            // Assign either 2 or 4 to the button - 50% chance
                            int value = (int) (Math.random() * 2) == 0 ? 2 : 4;
                            // Set the value
                            randomButton.setText(String.valueOf(value));

                            // Reset the style of the buttons
                            for (Button button : buttons) {
                                button.setBackgroundResource(R.drawable.custom_cube_btn);
                                button.setTextColor(getResources().getColor(R.color.md_theme_onPrimary));

                            }
                        }
                    }
                }
            });
        }
    }

    private void saveScore(String username, int bestScoreLabel) throws IOException {
        // Get the best score
        int bestScore = Integer.parseInt(((TextView) findViewById(bestScoreLabel)).getText().toString());
        // Get the current score
        int currentScore = Integer.parseInt(((TextView) findViewById(R.id.scoreLabel)).getText().toString());
        // Check if the current score is greater than the best score
        if (currentScore > bestScore) {
            Log.i("GameEngine", "New best score: " + currentScore);
            // Save the new best score
            if (saveHighScores(this, currentScore)) {
                // Alert the user
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("New Best Score");
                builder.setMessage("Congratulations! You have a new best score of " + currentScore);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Intent intent = new Intent(context, gameOver.class);
                        intent.putExtra("SCORE", String.valueOf(currentScore));
                        startActivity(intent);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        } else {
            Intent intent = new Intent(context, gameOver.class);
            Log.d("GameEngine", "Score: " + currentScore);
            intent.putExtra("SCORE", String.valueOf(currentScore));
            startActivity(intent);
        }
    }
}
