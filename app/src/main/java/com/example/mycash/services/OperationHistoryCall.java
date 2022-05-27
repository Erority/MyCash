package com.example.mycash.services;

import com.example.mycash.model.OperationCategory;
import com.example.mycash.model.OperationHistory;
import com.example.mycash.model.PostOperationHistory;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OperationHistoryCall {

    @GET("OperationHistory")
    Call<List<OperationHistory>> getAllOperationHistory();


    @GET("OperationCategory/{id}")
    Call<OperationCategory> getCategoryByID(@Path("id") int id);

    @POST("OperationHistory")
    Call<OperationHistory> postOperation(@Body PostOperationHistory operationHistory);

    @DELETE("OperationHistory/{id}")
    Call<OperationHistory> deleteOperation(@Path("id") int id);

}
