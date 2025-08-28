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

public class Available_challenges extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_available_challenges);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 👇 Pushup challenge navigation
        Button pushupBtn = findViewById(R.id.challenge3);
        pushupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Available_challenges.this, Pushup_start_challenge.class);
                startActivity(intent);
            }
        });
        Button SquatsBtn = findViewById(R.id.challenge2);
        SquatsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Available_challenges.this, Squats_start_challenge.class);
                startActivity(intent);
            }
        });
        Button StepsBtn = findViewById(R.id.challenge1);
        StepsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Available_challenges.this, Steps_challenge_start.class);
                startActivity(intent);
            }
        });
    }
}
