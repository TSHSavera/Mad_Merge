package com.example.simpleloginapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
        int score = getIntent().getIntExtra("SCORE", 0);
        int bestScore = getIntent().getIntExtra("BEST_SCORE", 0);

        // Update the UI with the scores
        TextView scoreTextView = findViewById(R.id.scoreLabel);
        TextView bestScoreTextView = findViewById(R.id.bestScoreLabel);

        scoreTextView.setText(String.valueOf(score));
        bestScoreTextView.setText(String.valueOf(bestScore));

        startUp();
    }

    private void startUp() {
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

        username.setText(Auth.getInstance(this).getUsername());
        usernameMidContent.setText(Auth.getInstance(this).getUsername());
    }
}
