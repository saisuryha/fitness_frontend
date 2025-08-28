package com.saveetha.fitnesschallenge;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Squats_start_challenge extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_squats_start_challenge);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button inviteBtn = findViewById(R.id.btn_invite_friends);
        inviteBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Squats_start_challenge.this, Invite_friends.class);
            intent.putExtra("from_page", "squats"); // ✅ fixed
            intent.putExtra("challenge_name", "Squats Challenge");
            startActivityForResult(intent, 100); // 100 = request code
        });
    }

    // ✅ Step 3: Handle result from Invite_friends
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            boolean invited = data.getBooleanExtra("invited", false);
            if (invited) {
                Toast.makeText(this, "Friends invited to Squats Challenge!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
