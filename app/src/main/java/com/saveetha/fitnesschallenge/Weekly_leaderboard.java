package com.saveetha.fitnesschallenge;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.tabs.TabLayout;

public class Weekly_leaderboard extends AppCompatActivity {

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_weekly_leaderboard);

        // Apply window insets padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tabLayout = findViewById(R.id.tab_layout);

        // Select the "Weekly" tab when this Activity starts
        TabLayout.Tab tabWeekly = tabLayout.getTabAt(0); // Index 0: Weekly
        if (tabWeekly != null) {
            tabWeekly.select();
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    // Current Activity: Weekly, do nothing
                } else if (position == 1) {
                    // Go to Monthly Leaderboard
                    Intent intent = new Intent(Weekly_leaderboard.this, Monthly_leaderboard.class);
                    startActivity(intent);
                    finish(); // Optional: close current Activity so user can't go "back" here
                } else if (position == 2) {
                    // Go to All Time Leaderboard
                    Intent intent = new Intent(Weekly_leaderboard.this, Alltime_leaderboard.class);
                    startActivity(intent);
                    finish();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
}
