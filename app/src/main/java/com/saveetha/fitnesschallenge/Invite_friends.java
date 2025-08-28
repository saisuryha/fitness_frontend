package com.saveetha.fitnesschallenge;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.saveetha.fitnesschallenge.adapter.FriendsAdapter;
import com.saveetha.fitnesschallenge.model.Friend;

import java.util.ArrayList;
import java.util.List;

public class Invite_friends extends AppCompatActivity {

    private RecyclerView friendsList;
    private Button inviteButton;
    private ImageView backArrow;
    private TextView challengeTitle;

    private FriendsAdapter friendsAdapter;
    private List<Friend> friendList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);

        // Bind views
        backArrow = findViewById(R.id.back_arrow);
        friendsList = findViewById(R.id.friends_list);
        inviteButton = findViewById(R.id.invite_button);
        challengeTitle = findViewById(R.id.challenge_title);

        // ✅ Get the challenge name from Intent
        String challengeName = getIntent().getStringExtra("challenge_name");

        if (challengeName != null && !challengeName.trim().isEmpty()) {
            challengeTitle.setText(challengeName);
        } else {
            challengeTitle.setText("Challenge"); // fallback text
        }

        // Back button click
        backArrow.setOnClickListener(v -> finish());

        // Initialize dummy friend data
        friendList = new ArrayList<>();
        loadDummyFriends();

        // Setup RecyclerView
        friendsAdapter = new FriendsAdapter(friendList);
        friendsList.setLayoutManager(new LinearLayoutManager(this));
        friendsList.setAdapter(friendsAdapter);

        // Search functionality
        EditText searchText = findViewById(R.id.search_src_text);
        if (searchText != null) {
            searchText.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                    filterFriends(s.toString());
                }
                @Override public void afterTextChanged(Editable s) {}
            });
        }

        // Invite button click
        // Invite button click
        inviteButton.setOnClickListener(v -> {
            List<Friend> selectedFriends = friendsAdapter.getSelectedFriends();

            if (selectedFriends.isEmpty()) {
                new AlertDialog.Builder(Invite_friends.this)
                        .setTitle("No Friends Selected ⚠️")
                        .setMessage("Please select at least one friend to invite.")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
            } else {
                new AlertDialog.Builder(Invite_friends.this)
                        .setTitle("Success ✅")
                        .setMessage("Friends invited successfully!")
                        .setPositiveButton("OK", (dialog, which) -> {
                            // ✅ Send result back instead of starting a new activity
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("invited", true);
                            setResult(RESULT_OK, resultIntent);
                            finish(); // close InviteFriends
                        })
                        .show();
            }
        });


    }

    private void loadDummyFriends() {
        friendList.add(new Friend("Nithi", R.drawable.nithi));
        friendList.add(new Friend("Srimathi", R.drawable.srimathi));
        friendList.add(new Friend("Aravinth", R.drawable.aravinth));
        friendList.add(new Friend("Santhosh", R.drawable.santhosh));
        friendList.add(new Friend("Friend 5", R.drawable.profile));
    }

    private void filterFriends(String query) {
        List<Friend> filteredList = new ArrayList<>();
        for (Friend friend : friendList) {
            if (friend.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(friend);
            }
        }
        friendsAdapter.updateList(filteredList);
    }
}
