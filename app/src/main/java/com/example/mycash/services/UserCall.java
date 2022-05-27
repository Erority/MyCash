package com.example.mycash.services;

import com.example.mycash.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserCall {

    @GET("Users")
    Call<List<User>> getUser();


    @GET("Users/{id}")
    Call<User> getUserByID(@Path("id") int id);


    @PUT("Users")
    Call<List<User>> putUser(@Body User user);
}
