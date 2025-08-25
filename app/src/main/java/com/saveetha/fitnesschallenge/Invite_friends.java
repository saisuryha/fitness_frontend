package com.yourpackage.yourapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.saveetha.fitnesschallenge.R;
import com.saveetha.fitnesschallenge.adapter.FriendsAdapter;
import com.saveetha.fitnesschallenge.model.Friend;

import java.util.ArrayList;
import java.util.List;

public class Invite_friends extends AppCompatActivity {

    private RecyclerView friendsList;
    private EditText searchFriends;
    private Button inviteButton;
    private ImageView backArrow;

    private FriendsAdapter friendsAdapter;
    private List<Friend> friendList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends); // Use your layout file name

        // Bind views
        backArrow = findViewById(R.id.back_arrow);
        friendsList = findViewById(R.id.friends_list);
        searchFriends = findViewById(R.id.search_bar).findViewById(android.R.id.edit);
        inviteButton = findViewById(R.id.invite_button);

        // Setup back button click
        backArrow.setOnClickListener(v -> finish());

        // Initialize dummy friend data
        friendList = new ArrayList<>();
        loadDummyFriends();

        // Set up RecyclerView
        friendsAdapter = new FriendsAdapter(friendList);
        friendsList.setLayoutManager(new LinearLayoutManager(this));
        friendsList.setAdapter(friendsAdapter);

        // Search functionality
        EditText searchEdit = findViewById(R.id.search_bar).findViewById(android.R.id.edit);
        // Or get EditText directly if you give it an id in XML:
        searchEdit = findViewById(R.id.search_bar);

        if (searchEdit != null) {
            searchEdit.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after){}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                    filterFriends(s.toString());
                }
                @Override public void afterTextChanged(Editable s){}
            });
        }

        // Invite button click
        inviteButton.setOnClickListener(v -> {
            List<Friend> selectedFriends = friendsAdapter.getSelectedFriends();
            // Handle invite logic here
            // For example, send invites or show a message
        });
    }

    private void loadDummyFriends() {
        friendList.add(new Friend("Friend 1", R.drawable.nithi));
        friendList.add(new Friend("Friend 2", R.drawable.srimathi));
        friendList.add(new Friend("Friend 3", R.drawable.aravinth));
        friendList.add(new Friend("Friend 4", R.drawable.santhosh));
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
