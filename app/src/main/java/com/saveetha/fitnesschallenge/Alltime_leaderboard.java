package com.saveetha.fitnesschallenge;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.tabs.TabLayout;

public class Alltime_leaderboard extends AppCompatActivity {

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alltime_leaderboard);

        // Apply window insets padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tabLayout = findViewById(R.id.tab_layout);

        // Select the "All Time" tab when this Activity starts
        TabLayout.Tab tabAlltime = tabLayout.getTabAt(2); // Index 2: All Time tab
        if (tabAlltime != null) {
            tabAlltime.select();
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    // Go to Weekly Leaderboard
                    Intent intent = new Intent(Alltime_leaderboard.this, LeaderboardActivity.class);
                    startActivity(intent);
                    finish();
                } else if (position == 1) {
                    // Go to Monthly Leaderboard
                    Intent intent = new Intent(Alltime_leaderboard.this, Monthly_leaderboard.class);
                    startActivity(intent);
                    finish();
                } else if (position == 2) {
                    // Current Activity: All Time, do nothing
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }
}
