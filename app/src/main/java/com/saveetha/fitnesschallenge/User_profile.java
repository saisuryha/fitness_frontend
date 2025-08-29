package com.saveetha.fitnesschallenge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User_profile extends AppCompatActivity {

    // --- Make these member variables to access them later ---
    private List<Goal> goals;
    private GoalsAdapter adapter;

    // --- Launcher to get results back from EditGoalsActivity ---
    private final ActivityResultLauncher<Intent> editGoalsLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    // This code runs when EditGoalsActivity sends back a result
                    ArrayList<Goal> updatedGoals = (ArrayList<Goal>) result.getData().getSerializableExtra("UPDATED_GOALS");
                    if (updatedGoals != null) {
                        this.goals.clear();
                        this.goals.addAll(updatedGoals);
                        this.adapter.notifyDataSetChanged(); // Refresh the list on the profile screen
                    }
                }
            });

    // --- Data Model: A simple class to hold the data for one goal ---
    // Make it Serializable to pass it between activities
    public static class Goal implements Serializable {
        String title;
        int progress;

        public Goal(String title, int progress) {
            this.title = title;
            this.progress = progress;
        }
    }

    // --- Adapter for the RecyclerView ---
    // This connects your data to the list
    public static class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.GoalViewHolder> {
        private final List<Goal> goalList;

        public GoalsAdapter(List<Goal> goalList) {
            this.goalList = goalList;
        }

        @NonNull
        @Override
        public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goal, parent, false);
            return new GoalViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull GoalViewHolder holder, int position) {
            Goal currentGoal = goalList.get(position);
            holder.goalTitle.setText(currentGoal.title);
            holder.goalProgressText.setText(currentGoal.progress + "%");
            holder.goalProgressBar.setProgress(currentGoal.progress);
        }

        @Override
        public int getItemCount() {
            return goalList.size();
        }

        public static class GoalViewHolder extends RecyclerView.ViewHolder {
            TextView goalTitle;
            TextView goalProgressText;
            LinearProgressIndicator goalProgressBar;

            public GoalViewHolder(@NonNull View itemView) {
                super(itemView);
                goalTitle = itemView.findViewById(R.id.tv_goal_title);
                goalProgressText = itemView.findViewById(R.id.tv_goal_progress_text);
                goalProgressBar = itemView.findViewById(R.id.progress_goal);
            }
        }
    }

    // --- Main Activity Code ---
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);

        // Setup for Toolbar and Edge-to-Edge display
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // --- RecyclerView Setup ---
        RecyclerView goalsRecyclerView = findViewById(R.id.rv_goals);

        // Initialize member variables with sample data
        goals = new ArrayList<>();
        goals.add(new Goal("Run 100 miles this month", 75));
        goals.add(new Goal("Complete 5 group challenges", 60));
        goals.add(new Goal("Maintain daily workout streak", 90));

        adapter = new GoalsAdapter(goals);
        goalsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        goalsRecyclerView.setAdapter(adapter);

        // --- Handle Edit Goals Button Click ---
        ImageButton editGoalsButton = findViewById(R.id.btn_edit_goals);
        editGoalsButton.setOnClickListener(v -> {
            Intent intent = new Intent(User_profile.this, EditGoalsActivity.class);
            // Pass the current list of goals to the new activity
            intent.putExtra("GOALS_LIST", (ArrayList<Goal>) goals);
            // Launch the activity and wait for a result
            editGoalsLauncher.launch(intent);
        });

        // =================== NEW CODE ADDED HERE ===================
        // --- Handle About TextView Click ---
        TextView aboutTextView = findViewById(R.id.menu_about);
        aboutTextView.setOnClickListener(v -> {
            Intent intent = new Intent(User_profile.this, About.class);
            startActivity(intent);
        });
        // =========================================================
    }
}