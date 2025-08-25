package com.saveetha.fitnesschallenge;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Monthly_leaderboard extends AppCompatActivity {
    private Button btnAlltime;
    private Button btnWeekly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_monthly_leaderboard);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnAlltime = findViewById(R.id.Alltime);

        btnAlltime.setOnClickListener(view -> {
            Intent intent = new Intent(Monthly_leaderboard.this, Alltime_leaderboard.class);
            startActivity(intent);
        });
        btnWeekly = findViewById(R.id.trophy);

        btnWeekly.setOnClickListener(view -> {
            Intent intent = new Intent(Monthly_leaderboard.this, Weekly_leaderboard.class);
            startActivity(intent);
        });
    } // <- This closes onCreate

} // <- This closes the class
