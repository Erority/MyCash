package com.example.mycash.services;

import android.text.format.DateUtils;

import androidx.lifecycle.MutableLiveData;

import com.example.mycash.model.OperationCategory;
import com.example.mycash.model.OperationHistory;
import com.example.mycash.model.PostOperationHistory;
import com.example.mycash.model.User;
import com.example.mycash.utils.RetrofitListener;

import org.joda.time.DateTimeComparator;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

 public class OperationHistoryService {

    private Retrofit retrofit;
    private OperationHistoryCall historyCall;
    private static final String BASE_URL = "https://10.0.2.2:5001/api/";

    private MutableLiveData<List<OperationHistory>> userOperationHistory = new MutableLiveData<>();
    private MutableLiveData<OperationCategory> operationCategory = new MutableLiveData<>();

    public OperationHistoryService(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OkHttpBuilder.getUnsafeOkHttpClient().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        historyCall = retrofit.create(OperationHistoryCall.class);
    }

    public void deleteOperation(int id, RetrofitListener retrofitListener){
        historyCall.deleteOperation(id).enqueue(new Callback<OperationHistory>() {
            @Override
            public void onResponse(Call<OperationHistory> call, Response<OperationHistory> response) {
                if(response.isSuccessful())
                    retrofitListener.onSuccess();
                else
                    retrofitListener.onFailure();
            }

            @Override
            public void onFailure(Call<OperationHistory> call, Throwable t) {
                retrofitListener.onFailure();
            }
        });
    }

    public void postOperation(RetrofitListener retrofitListener, PostOperationHistory operationHistory){
        historyCall.postOperation(operationHistory).enqueue(new Callback<OperationHistory>() {
            @Override
            public void onResponse(Call<OperationHistory> call, Response<OperationHistory> response) {
                System.out.println(operationHistory);

                if(response.isSuccessful())
                    retrofitListener.onSuccess();
                else
                    retrofitListener.onFailure();
            }

            @Override
            public void onFailure(Call<OperationHistory> call, Throwable t) {
                retrofitListener.onFailure();
            }
        });
    }

    public void getUsersOperations(int userID, int idOperationType, Date date){
        historyCall.getAllOperationHistory().enqueue(new Callback<List<OperationHistory>>() {
            @Override
            public void onResponse(Call<List<OperationHistory>> call, Response<List<OperationHistory>> response) {
                if(response.isSuccessful()){
                    List<OperationHistory> operations = response.body();
                    List<OperationHistory> usersOperation = new ArrayList<>();

                    for (OperationHistory operationHistory: operations){

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        try {
                            operationHistory.setCreationDateDate(format.parse(operationHistory.getCreationDate()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        DateTimeComparator dateTimeComparator = DateTimeComparator.getDateOnlyInstance();

                        if(userID == operationHistory.getUserID()
                                && operationHistory.getOperationTypeID() == idOperationType
                                && dateTimeComparator.compare(date, operationHistory.getCreationDateDate()) == 0) {
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try  {
                                        OperationCategory category = historyCall.getCategoryByID(operationHistory.getOperationCategoryID()).execute().body();
                                        operationHistory.setOperationCategory
                                                    (category);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            thread.start();
                            try {
                                thread.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            usersOperation.add(operationHistory);
                        }
                    }

                    userOperationHistory.setValue(usersOperation);
                }
            }

            @Override
            public void onFailure(Call<List<OperationHistory>> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<List<OperationHistory>> getUserOperationHistory() {
        return userOperationHistory;
    }
 }
