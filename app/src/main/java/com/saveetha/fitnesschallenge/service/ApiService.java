package com.saveetha.fitnesschallenge.service;

import com.saveetha.fitnesschallenge.User_signup;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @FormUrlEncoded
    @POST("login.php")
    Call<LoginResponse> login(@Field("email") String email, @Field("password")String password);

    @FormUrlEncoded
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


}
