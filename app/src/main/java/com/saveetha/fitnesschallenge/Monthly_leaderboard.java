package com.saveetha.fitnesschallenge;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.tabs.TabLayout;

public class Monthly_leaderboard extends AppCompatActivity {

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_monthly_leaderboard);

        // Apply window insets padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tabLayout = findViewById(R.id.tab_layout);

        // Select the "Monthly" tab when this Activity starts
        TabLayout.Tab tabMonthly = tabLayout.getTabAt(1); // Index 1: Monthly tab
        if (tabMonthly != null) {
            tabMonthly.select();
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    // Go to Weekly Leaderboard
                    Intent intent = new Intent(Monthly_leaderboard.this, LeaderboardActivity.class);
                    startActivity(intent);
                    finish();
                } else if (position == 1) {
                    // Current Activity: Monthly, do nothing
                } else if (position == 2) {
                    // Go to All Time Leaderboard
                    Intent intent = new Intent(Monthly_leaderboard.this, Alltime_leaderboard.class);
                    startActivity(intent);
                    finish();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }
}
