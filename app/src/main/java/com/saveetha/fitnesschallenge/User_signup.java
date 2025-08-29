package com.saveetha.fitnesschallenge;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// =================== NEW IMPORTS START ===================
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
// ===================  NEW IMPORTS END  ===================

import com.saveetha.fitnesschallenge.service.User_signupResponse;
import com.saveetha.fitnesschallenge.service.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User_signup extends AppCompatActivity {

    private static final String TAG = "UserSignup";
    private EditText editName, editEmail, editPassword, confirmPassword;
    private Button btnSignup, btnAdmin;

    // =================== NEW VARIABLES START ===================
    private Button btnGoogleSignup;
    private GoogleSignInClient mGoogleSignInClient;
    // ===================  NEW VARIABLES END  ===================

    // =================== NEW LAUNCHER FOR GOOGLE SIGN-IN START ===================
    private final ActivityResultLauncher<Intent> googleSignInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    handleGoogleSignInResult(task);
                } else {
                    Toast.makeText(this, "Google Sign-In failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
    // ===================  NEW LAUNCHER FOR GOOGLE SIGN-IN END  ===================


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

        // =================== NEW GOOGLE SETUP START ===================
        btnGoogleSignup = findViewById(R.id.btn_google_signup);

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.your_web_client_id)) // Important: See explanation below
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnGoogleSignup.setOnClickListener(v -> signInWithGoogle());
        // ===================  NEW GOOGLE SETUP END  ===================


        // User Signup Button (Existing code)
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

        // Admin Signup Button (Existing code)
        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User_signup.this, Admin_signup.class);
                startActivity(intent);
            }
        });
    }

    // (Existing userSignup method - untouched)
    private void userSignup(String name, String email, String password, String confirmPass) {
        //... your existing code ...
    }


    // =================== NEW GOOGLE SIGN-IN METHODS START ===================
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        googleSignInLauncher.launch(signInIntent);
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d(TAG, "Google Sign-In successful for: " + account.getEmail());

            String idToken = account.getIdToken();
            // IMPORTANT: Now you send this idToken to your backend to register the user
            registerWithGoogleToken(idToken);

        } catch (ApiException e) {
            Log.w(TAG, "Google Sign-In failed, code: " + e.getStatusCode());
            Toast.makeText(this, "Google authentication failed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerWithGoogleToken(String idToken) {
        ProgressDialog p = new ProgressDialog(this);
        p.setMessage("Signing you up...");
        p.setCancelable(false);
        p.show();

        // IMPORTANT: You need to add a method like 'registerWithGoogle' to your Retrofit service
        Call<User_signupResponse> call = RetrofitClient
                .getService()
                .registerWithGoogle(idToken); // This is a new method you must create

        call.enqueue(new Callback<User_signupResponse>() {
            @Override
            public void onResponse(Call<User_signupResponse> call, Response<User_signupResponse> response) {
                p.dismiss();
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(User_signup.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(User_signup.this, Login.class));
                    finish();
                } else {
                    String message = "Server error, please try again.";
                    if (response.body() != null) {
                        message = response.body().getMessage();
                    }
                    Toast.makeText(User_signup.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User_signupResponse> call, Throwable t) {
                p.dismiss();
                Toast.makeText(User_signup.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    // ===================  NEW GOOGLE SIGN-IN METHODS END  ===================
}