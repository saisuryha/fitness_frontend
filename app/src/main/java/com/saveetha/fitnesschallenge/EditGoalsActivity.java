package com.saveetha.fitnesschallenge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;

public class EditGoalsActivity extends AppCompatActivity {

    private ArrayList<User_profile.Goal> goalsList;
    private EditGoalsAdapter adapter;

    // --- Adapter for the EDITABLE RecyclerView ---
    // Inside EditGoalsActivity.java

    // --- NEW AND IMPROVED Adapter for the EDITABLE RecyclerView ---
    public static class EditGoalsAdapter extends RecyclerView.Adapter<EditGoalsAdapter.EditGoalViewHolder> {
        private final ArrayList<User_profile.Goal> localGoalsList;

        public EditGoalsAdapter(ArrayList<User_profile.Goal> goalsList) {
            this.localGoalsList = goalsList;
        }

        @NonNull
        @Override
        public EditGoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goal_editable, parent, false);
            // Pass the list to the ViewHolder so the TextWatcher can modify it
            return new EditGoalViewHolder(view, localGoalsList);
        }

        @Override
        public void onBindViewHolder(@NonNull EditGoalViewHolder holder, int position) {
            // This method tells the ViewHolder which item to display
            holder.bind(localGoalsList.get(position), position);

            // Listener to delete a goal
            holder.deleteButton.setOnClickListener(v -> {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    localGoalsList.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                }
            });
        }

        @Override
        public int getItemCount() {
            return localGoalsList.size();
        }

        public static class EditGoalViewHolder extends RecyclerView.ViewHolder {
            EditText goalTitle;
            ImageButton deleteButton;
            MyTextWatcher textWatcher;

            public EditGoalViewHolder(@NonNull View itemView, ArrayList<User_profile.Goal> goalsList) {
                super(itemView);
                goalTitle = itemView.findViewById(R.id.et_goal_title);
                deleteButton = itemView.findViewById(R.id.btn_delete_goal);
                // Create one TextWatcher per ViewHolder and pass the list to it
                this.textWatcher = new MyTextWatcher(goalsList);
            }

            // A helper method to connect the data to the ViewHolder
            public void bind(User_profile.Goal goal, int position) {
                // Remove the listener before setting text to prevent loops
                goalTitle.removeTextChangedListener(textWatcher);

                // Update the watcher's position and set the text
                textWatcher.updatePosition(position);
                goalTitle.setText(goal.title);

                // Add the listener back
                goalTitle.addTextChangedListener(textWatcher);
            }
        }

        // A custom TextWatcher to update the data list as the user types
        private static class MyTextWatcher implements TextWatcher {
            private int position;
            private final ArrayList<User_profile.Goal> goalsList;

            public MyTextWatcher(ArrayList<User_profile.Goal> goalsList) {
                this.goalsList = goalsList;
            }

            public void updatePosition(int position) {
                this.position = position;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Update the goal's title in our data list as the user types
                if (position < goalsList.size()) {
                    goalsList.get(position).title = s.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed here
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_goals);

        Toolbar toolbar = findViewById(R.id.toolbar_edit_goals);
        toolbar.setNavigationOnClickListener(v -> finish()); // Just close on back press

        goalsList = (ArrayList<User_profile.Goal>) getIntent().getSerializableExtra("GOALS_LIST");
        if (goalsList == null) {
            goalsList = new ArrayList<>();
        }

        RecyclerView rvEditGoals = findViewById(R.id.rv_edit_goals);
        adapter = new EditGoalsAdapter(goalsList);
        rvEditGoals.setLayoutManager(new LinearLayoutManager(this));
        rvEditGoals.setAdapter(adapter);

        MaterialButton btnAddGoal = findViewById(R.id.btn_add_goal);
        MaterialButton btnSaveGoals = findViewById(R.id.btn_save_goals);

        btnAddGoal.setOnClickListener(v -> {
            goalsList.add(new User_profile.Goal("", 0)); // Add a blank goal
            adapter.notifyItemInserted(goalsList.size() - 1);
        });

        btnSaveGoals.setOnClickListener(v -> {
            // Create a new Intent to hold the result
            Intent resultIntent = new Intent();
            // Put the UPDATED list into the intent
            resultIntent.putExtra("UPDATED_GOALS", goalsList);
            // Set the result to OK and attach the intent
            setResult(Activity.RESULT_OK, resultIntent);
            // Close this activity and go back to the profile
            finish();
        });
    }
}