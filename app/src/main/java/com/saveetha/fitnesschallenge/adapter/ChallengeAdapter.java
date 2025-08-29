

// ChallengeAdapter.java
package com.saveetha.fitnesschallenge.adapter; // Make sure this matches your package

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.saveetha.fitnesschallenge.R;
import com.saveetha.fitnesschallenge.model.Challenge;

import java.util.List;

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder> {

    private List<Challenge> challengeList;

    // Constructor to pass in the data list
    public ChallengeAdapter(List<Challenge> challengeList) {
        this.challengeList = challengeList;
    }

    // This is where the list_item_challenge.xml is inflated
    @NonNull
    @Override
    public ChallengeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_challenge, parent, false);
        return new ChallengeViewHolder(view);
    }

    // This method binds the data from your Challenge object to the views
    @Override
    public void onBindViewHolder(@NonNull ChallengeViewHolder holder, int position) {
        Challenge challenge = challengeList.get(position);

        holder.userName.setText(challenge.getUserName());
        holder.challengeTitle.setText(challenge.getChallengeTitle());
        holder.challengeGoal.setText(challenge.getChallengeGoal());
        holder.duration.setText(challenge.getDuration());
        holder.userAvatar.setImageResource(challenge.getUserAvatarResId());
        holder.challengeIcon.setImageResource(challenge.getChallengeIconResId());

        // You can also set an OnClickListener for the join button here
        holder.joinButton.setOnClickListener(v -> {
            // Handle the join button click for this specific challenge
            // For example: Toast.makeText(v.getContext(), "Joining " + challenge.getChallengeTitle(), Toast.LENGTH_SHORT).show();
        });
    }

    // This returns the total number of items in the list
    @Override
    public int getItemCount() {
        return challengeList.size();
    }


    // The ViewHolder class holds the UI components for a single list item
    public static class ChallengeViewHolder extends RecyclerView.ViewHolder {
        ImageView userAvatar, challengeIcon;
        TextView userName, challengeTitle, challengeGoal, duration;
        MaterialButton joinButton;

        public ChallengeViewHolder(@NonNull View itemView) {
            super(itemView);
            userAvatar = itemView.findViewById(R.id.image_user_avatar);
            challengeIcon = itemView.findViewById(R.id.icon_challenge_type);
            userName = itemView.findViewById(R.id.text_user_name);
            challengeTitle = itemView.findViewById(R.id.text_challenge_title);
            challengeGoal = itemView.findViewById(R.id.text_challenge_goal);
            duration = itemView.findViewById(R.id.text_duration);
            joinButton = itemView.findViewById(R.id.button_join);
        }
    }
}