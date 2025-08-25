package com.saveetha.fitnesschallenge.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saveetha.fitnesschallenge.R;
import com.saveetha.fitnesschallenge.model.Friend;

import java.util.ArrayList;
import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendViewHolder> {

    private List<Friend> friendsList;

    public FriendsAdapter(List<Friend> friends) {
        this.friendsList = new ArrayList<>(friends);
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        Friend friend = friendsList.get(position);
        holder.friendName.setText(friend.getName());
        holder.friendImage.setImageResource(friend.getImageResId());
        holder.friendCheckBox.setChecked(friend.isSelected());

        holder.friendCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            friend.setSelected(isChecked);
        });

        // Optional: clicking on the whole item toggles the checkbox
        holder.itemView.setOnClickListener(v -> {
            boolean newState = !holder.friendCheckBox.isChecked();
            holder.friendCheckBox.setChecked(newState);
        });
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public void updateList(List<Friend> filteredFriends) {
        this.friendsList = new ArrayList<>(filteredFriends);
        notifyDataSetChanged();
    }

    public List<Friend> getSelectedFriends() {
        List<Friend> selected = new ArrayList<>();
        for (Friend f : friendsList) {
            if (f.isSelected()) {
                selected.add(f);
            }
        }
        return selected;
    }

    static class FriendViewHolder extends RecyclerView.ViewHolder {
        ImageView friendImage;
        TextView friendName;
        CheckBox friendCheckBox;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            friendImage = itemView.findViewById(R.id.img_friend);
            friendName = itemView.findViewById(R.id.tv_friend_name);
            friendCheckBox = itemView.findViewById(R.id.check_friend);
        }
    }
}
