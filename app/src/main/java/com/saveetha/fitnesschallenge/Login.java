package com.saveetha.fitnesschallenge;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.saveetha.fitnesschallenge.service.LoginResponse;
import com.saveetha.fitnesschallenge.service.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private EditText email, password;
    private Button loginButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        loginButton = findViewById(R.id.btn_login);

        loginButton.setOnClickListener(view -> {
            String emailString = email.getText().toString().trim();
            String passwordString = password.getText().toString().trim();
            if(emailString.isEmpty() || passwordString.isEmpty()) {
                Toast.makeText(Login.this, "Enter email and password", Toast.LENGTH_SHORT).show();
            } else {
                login(emailString, passwordString);
            }
        });

    }

    private void login(String email, String password) {
        ProgressDialog p = new ProgressDialog(this);
        p.setMessage("Please Wait");
        p.setCanceledOnTouchOutside(false);
        p.setCancelable(false);
        p.show();
        RetrofitClient.getService().login(email, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                p.dismiss();
                if(response.isSuccessful() && response.body() != null) {
                    LoginResponse res = response.body();
                    if(res.getStatus().equalsIgnoreCase("success")) {
                        Toast.makeText(Login.this, res.getMessage(), Toast.LENGTH_SHORT).show();

                        // Get user role from response
                        String role = "";
                        if(res.getUser() != null && res.getUser().getRole() != null) {
                            role = res.getUser().getRole();
                        }

                        if ("admin".equalsIgnoreCase(role)) {
                            Intent intent = new Intent(Login.this, Admin_dashboard.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(Login.this, User_dashboard.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(Login.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login.this, "Server Error or Empty Response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                p.dismiss();
                Toast.makeText(Login.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
