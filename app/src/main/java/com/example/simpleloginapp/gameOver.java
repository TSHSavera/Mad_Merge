package com.example.simpleloginapp;

import static com.example.simpleloginapp.JsonProcessor.getHighScore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

public class gameOver extends ComponentActivity {

    Context context = this;
    ImageView user_icon;
    Button newGameBtn;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_over);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_game_over), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get the scores from the Intent
        String score = getIntent().getStringExtra("SCORE");
        Log.d("GameEngine", "Score: " + score);
        // Update the UI with the scores
        TextView scoreTextView = findViewById(R.id.scoreLabel);
        scoreTextView.setText(score);

        // Get the best score
        int bestScore = getHighScore(this);
        TextView bestScoreLabel = findViewById(R.id.bestScoreLabel);
        bestScoreLabel.setText(String.valueOf(bestScore));

        // Check if the user is signed in
        if (Auth.getInstance(this) == null || Auth.getInstance(this).getUsername() == null) {
            // Redirect to Welcome Activity
            Intent intent = new Intent(context, Welcome_page.class);
            startActivity(intent);
        }
        startUp();
    }

    private void startUp() {
        // Check if user is signed in (non-null) and update UI accordingly.
        Auth auth = new Auth(this);
        if (Auth.getInstance(this) == null || Auth.getInstance(this).getUsername() == null) {
            Log.i("User", "User is not signed in.");
        } else {
            new Auth(this);
            Log.i("User", "User is signed in: " + Objects.requireNonNull(Auth.getInstance(this)).getEmail());
            TextView username = findViewById(R.id.username);
            username.setText(Objects.requireNonNull(Auth.getInstance(this)).getUsername());
        }



        user_icon = findViewById(R.id.user_icon);
        user_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, activity_user.class);
                startActivity(intent);
            }
        });

        newGameBtn = findViewById(R.id.newGameBtn);
        newGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, game.class);
                startActivity(intent);
            }
        });

        // Change the username to the current user
        TextView username = findViewById(R.id.username);
        TextView usernameMidContent = findViewById(R.id.usernameMidContent);

        username.setText(Objects.requireNonNull(Auth.getInstance(this)).getUsername());
        usernameMidContent.setText(Auth.getInstance(this).getUsername());

    }
}
