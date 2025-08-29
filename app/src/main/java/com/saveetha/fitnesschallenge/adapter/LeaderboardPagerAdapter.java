package com.saveetha.fitnesschallenge;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class LeaderboardPagerAdapter extends FragmentStateAdapter {

    public LeaderboardPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new com.saveetha.fitnesschallenge.WeeklyFragment();
            case 1:
                return new com.saveetha.fitnesschallenge.MonthlyFragment();
            case 2:
                return new com.saveetha.fitnesschallenge.AllTimeFragment();
            default:
                // Return a default fragment just in case
                return new com.saveetha.fitnesschallenge.WeeklyFragment();
        }
    }

    @Override
    public int getItemCount() {
        // We have 3 fragments in total
        return 3;
    }
}