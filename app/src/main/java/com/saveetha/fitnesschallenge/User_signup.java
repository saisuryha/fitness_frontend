package com.saveetha.fitnesschallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.saveetha.fitnesschallenge.service.User_signupResponse;
import com.saveetha.fitnesschallenge.service.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User_signup extends AppCompatActivity {

    private static final String TAG = "UserSignup";
    private EditText editName, editEmail, editPassword, confirmPassword;
    private Button btnSignup, btnAdmin;// ✅ Added btnAdmin

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);

        // Initialize Views
        editName = findViewById(R.id.edit_fullname);
        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);
        confirmPassword = findViewById(R.id.edit_confirm_password);
        btnSignup = findViewById(R.id.btn_signup);
        btnAdmin = findViewById(R.id.btn_admin);
        // User Signup Button
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString().trim();
                String email = editEmail.getText().toString().trim();
                String password = editPassword.getText().toString().trim();
                String confirmPass = confirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    editName.setError("Enter your name");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    editEmail.setError("Enter your email");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    editPassword.setError("Enter your password");
                    return;
                }
                if (TextUtils.isEmpty(confirmPass)) {
                    confirmPassword.setError("Confirm your password");
                    return;
                }
                if (!password.equals(confirmPass)) {
                    confirmPassword.setError("Passwords do not match");
                    return;
                }

                userSignup(name, email, password, confirmPass);
            }
        });

        // ✅ Admin Signup Button
        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User_signup.this, Admin_signup.class);
                startActivity(intent);
            }
        });

    }

    private void userSignup(String name, String email, String password, String confirmPass) {
        Log.d(TAG, "Attempting signup with: " + name + ", " + email);
        ProgressDialog p = new ProgressDialog(this);
        p.setMessage("Please Wait");
        p.setCanceledOnTouchOutside(false);
        p.setCancelable(false);
        p.show();

        Call<User_signupResponse> call = RetrofitClient
                .getService()
                .registerUser(name, email, password, confirmPass);

        call.enqueue(new Callback<User_signupResponse>() {
            @Override
            public void onResponse(Call<User_signupResponse> call, Response<User_signupResponse> response) {
                p.dismiss();
                Log.d(TAG, "API Response Code: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    User_signupResponse signupResponse = response.body();
                    if (signupResponse.isSuccess()) {
                        Toast.makeText(User_signup.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(User_signup.this, Login.class));
                        finish();
                    } else {
                        Toast.makeText(User_signup.this, signupResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(User_signup.this, "Server error, please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User_signupResponse> call, Throwable t) {
                p.dismiss();
                Log.e(TAG, "API Call Failed", t);
                Toast.makeText(User_signup.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
