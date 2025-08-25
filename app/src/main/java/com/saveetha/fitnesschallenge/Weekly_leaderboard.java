package com.saveetha.fitnesschallenge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Weekly_leaderboard extends AppCompatActivity {

    private Button btnMonth;
    private Button btnAlltime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_weekly_leaderboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnAlltime = findViewById(R.id.Alltime);

        btnAlltime.setOnClickListener(view -> {
            Intent intent = new Intent(Weekly_leaderboard.this, Alltime_leaderboard.class);
            startActivity(intent);
        });
        btnMonth = findViewById(R.id.monthly);

        btnMonth.setOnClickListener(view -> {
            Intent intent = new Intent(Weekly_leaderboard.this, Monthly_leaderboard.class);
            startActivity(intent);
        });
    }
}