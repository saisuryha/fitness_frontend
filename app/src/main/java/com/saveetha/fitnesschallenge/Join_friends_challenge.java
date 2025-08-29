
package com.saveetha.fitnesschallenge;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.saveetha.fitnesschallenge.adapter.ChallengeAdapter;
import com.saveetha.fitnesschallenge.model.Challenge;

import java.util.ArrayList;
import java.util.List;

public class Join_friends_challenge extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChallengeAdapter adapter;
    private List<Challenge> challengeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_friends_challenge);

        // 1. Find the RecyclerView in your layout
        recyclerView = findViewById(R.id.recycler_view_challenges);

        // 2. Create your data list (you would normally get this from a database or API)
        loadChallengeData();

        // 3. Create and set the adapter
        adapter = new ChallengeAdapter(challengeList);

        // 4. Set a Layout Manager (this tells the RecyclerView how to arrange items)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 5. Connect the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);

        // You can also set up listeners for your toolbar, search bar, and chips here
    }

    // This is a helper method to create some sample data
    private void loadChallengeData() {
        challengeList = new ArrayList<>();
        challengeList.add(new Challenge("Mike Chen", "May Steps Sprint", "10,000 steps daily", "12 days left", R.drawable.ic_profile, R.drawable.ic_steps));
        challengeList.add(new Challenge("Nithi", "Calorie Burn Challenge", "500 kcal daily", "Ends May 31st", R.drawable.nithi, R.drawable.ic_fire));
        challengeList.add(new Challenge("Santhosh", "Workout Warrior", "5 workouts weekly", "2 weeks left", R.drawable.santhosh, R.drawable.ic_dumbbell));
        // Add as many challenges as you want...
    }
}