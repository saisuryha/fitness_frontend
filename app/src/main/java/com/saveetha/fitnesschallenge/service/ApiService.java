package com.saveetha.fitnesschallenge.service;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @FormUrlEncoded
    @POST("login.php")
    Call<LoginResponse> login(@Field("email") String email, @Field("password")String password);

    @FormUrlEncoded // ✅ THIS LINE IS NOW CORRECTED
    @POST("signup.php")
    Call<User_signupResponse> registerUser(
            @Field("full_name") String fullName,
            @Field("email") String email,
            @Field("password") String password,
            @Field("confirm_password") String confirmPassword
    );
    @FormUrlEncoded
    @POST("admin_signup.php")  // or your actual endpoint
    Call<User_signupResponse> registerAdmin(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password
    );

    /**
     * Sends the Google ID Token to the backend for registration/login.
     * @param idToken The token received from Google Sign-In.
     * @return A response object indicating success or failure.
     */
    @FormUrlEncoded
    @POST("google_signup.php") // ❗ IMPORTANT: Make sure this matches your backend endpoint
    Call<User_signupResponse> registerWithGoogle(@Field("idToken") String idToken);
}