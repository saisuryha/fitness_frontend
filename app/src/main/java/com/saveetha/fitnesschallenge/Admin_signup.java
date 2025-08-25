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

public class Admin_signup extends AppCompatActivity {

    private static final String TAG = "AdminSignup";
    private EditText editName, editEmail, editPassword, confirmPassword;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signup); // 👈 separate layout for admin

        // Initialize Views
        editName = findViewById(R.id.edit_admin_fullname);
        editEmail = findViewById(R.id.edit_admin_email);
        editPassword = findViewById(R.id.edit_admin_password);
        confirmPassword = findViewById(R.id.edit_admin_confirm_password);
        btnSignup = findViewById(R.id.btn_admin_signup);

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
                if (!password.equals(confirmPass)) {
                    confirmPassword.setError("Passwords do not match");
                    return;
                }

                // Call API
                adminSignup(name, email, password);
            }
        });
    }

    private void adminSignup(String name, String email, String password) {
        Log.d(TAG, "Admin signup attempt: " + name + ", " + email);
        ProgressDialog p = new ProgressDialog(this);
        p.setMessage("Please Wait");
        p.setCanceledOnTouchOutside(false);
        p.setCancelable(false);
        p.show();

        // 👇 Modify Retrofit API call
        Call<User_signupResponse> call = RetrofitClient
                .getService()
                .registerAdmin(name, email, password); // 👈 create this in API interface

        call.enqueue(new Callback<User_signupResponse>() {
            @Override
            public void onResponse(Call<User_signupResponse> call, Response<User_signupResponse> response) {
                p.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    User_signupResponse signupResponse = response.body();
                    if (signupResponse.isSuccess()) {
                        Toast.makeText(Admin_signup.this, "Admin Registered Successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Admin_signup.this, Login.class));
                        finish();
                    } else {
                        Toast.makeText(Admin_signup.this, signupResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Admin_signup.this, "Server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User_signupResponse> call, Throwable t) {
                p.dismiss();
                Toast.makeText(Admin_signup.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
