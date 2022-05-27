package com.example.mycash.services;

import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;

import com.example.mycash.model.OperationCategory;
import com.example.mycash.model.User;
import com.example.mycash.utils.RetrofitListener;

import java.io.IOException;
import java.security.cert.CertificateException;
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

public class UserService {

    private Retrofit retrofit;
    private UserCall userCall;

    private static final String BASE_URL = "https://10.0.2.2:5001/api/";

    private MutableLiveData<User> authUser = new MutableLiveData<>();

    public UserService(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OkHttpBuilder.getUnsafeOkHttpClient().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userCall = retrofit.create(UserCall.class);
    }

    public User updateUser(int userID) throws IOException {
        return userCall.getUserByID(userID).execute().body();
    }


    public void auth(String login, String password, RetrofitListener retrofitListener){
        userCall.getUser().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> users = response.body();
                try {
                    for (User user : users) {
                        if (user.getUserLogin().equals(login) && user.getUserPassword().equals(password)) {
                            retrofitListener.onSuccess();
                            authUser.setValue(user);
                            break;
                        }
                    }
                    retrofitListener.onFailure();
                } catch (Exception e){
                    retrofitListener.onFailure();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                authUser.setValue(null);
            }
        });
    }

    public void updateUserBalance(User user){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    userCall.putUser(user).execute().body();
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
    }

    public MutableLiveData<User> getAuthUser() {
        return authUser;
    }
}
