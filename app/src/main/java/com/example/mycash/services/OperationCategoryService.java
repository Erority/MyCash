package com.example.mycash.services;

import androidx.lifecycle.MutableLiveData;

import com.example.mycash.model.OperationCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OperationCategoryService {

    private Retrofit retrofit;
    private OperationCategoryCall categoryCall;
    private static final String BASE_URL = "https://10.0.2.2:5001/api/";


    private MutableLiveData<List<OperationCategory>> costsCategories = new MutableLiveData<>();

    public OperationCategoryService(boolean isCosts){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OkHttpBuilder.getUnsafeOkHttpClient().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        categoryCall = retrofit.create(OperationCategoryCall.class);

        categoryCall.getCategories().enqueue(new Callback<List<OperationCategory>>() {
            @Override
            public void onResponse(Call<List<OperationCategory>> call, Response<List<OperationCategory>> response) {
                List<OperationCategory> operationCategories = new ArrayList<>();
                if (isCosts) {
                    if (response.isSuccessful()) {
                        for (int i = 0; i < 7; i++) {
                            operationCategories.add(response.body().get(i));
                        }
                    }
                } else {

                    if (response.isSuccessful()) {
                        for (int i = 7; i < response.body().size(); i++) {
                            operationCategories.add(response.body().get(i));
                        }
                    }
                }

                costsCategories.setValue(operationCategories);
            }

            @Override
            public void onFailure(Call<List<OperationCategory>> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<List<OperationCategory>> getCategories() {
        return costsCategories;
    }
}
