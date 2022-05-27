package com.example.mycash.services;

import com.example.mycash.model.OperationCategory;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface OperationCategoryCall {

    @GET("OperationCategory")
    Call<List<OperationCategory>> getCategories();
}
