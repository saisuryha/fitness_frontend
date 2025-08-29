package com.saveetha.fitnesschallenge;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class LeaderboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        // Find the views from our layout
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager2 viewPager = findViewById(R.id.view_pager);

        // Create and set our new adapter
        com.saveetha.fitnesschallenge.LeaderboardPagerAdapter pagerAdapter = new com.saveetha.fitnesschallenge.LeaderboardPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        // This is the magic that links the tabs to the sliding pages
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Set the text for each tab based on its position
            switch (position) {
                case 0:
                    tab.setText("Weekly");
                    break;
                case 1:
                    tab.setText("Monthly");
                    break;
                case 2:
                    tab.setText("All Time");
                    break;
            }
        }).attach();
    }
}