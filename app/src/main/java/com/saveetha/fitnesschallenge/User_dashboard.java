package com.saveetha.fitnesschallenge;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class User_dashboard extends AppCompatActivity {

    private Button btnStartChallenge;
    private Button btnjoinfriends;
    private ImageView trophyIcon;
    private ImageView bellIcon;
    private ImageView profileIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_dashboard);

        // Apply window insets padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_user), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Buttons
        btnStartChallenge = findViewById(R.id.btn_start_challenge);
        btnjoinfriends = findViewById(R.id.btn_join_friend);
        trophyIcon = findViewById(R.id.trophy);
        bellIcon=findViewById(R.id.bell);
        profileIcon=findViewById(R.id.profile);

        btnStartChallenge.setOnClickListener(view -> {
            Intent intent = new Intent(User_dashboard.this, Available_challenges.class);
            startActivity(intent);
        });

        btnjoinfriends.setOnClickListener(view -> {
            Intent intent = new Intent(User_dashboard.this, Join_friends_challenge.class);
            startActivity(intent);
        });

        trophyIcon.setOnClickListener(view -> {
            Intent intent = new Intent(User_dashboard.this, Weekly_leaderboard.class);
            startActivity(intent);
        });
        bellIcon.setOnClickListener(view -> {
            Intent intent = new Intent(User_dashboard.this, User_notifications.class);
            startActivity(intent);
        });
        profileIcon.setOnClickListener(view -> {
            Intent intent = new Intent(User_dashboard.this, User_profile.class);
            startActivity(intent);
        });
    }
}
