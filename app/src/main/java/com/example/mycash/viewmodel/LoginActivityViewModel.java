package com.example.mycash.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mycash.model.User;
import com.example.mycash.services.UserService;
import com.example.mycash.utils.RetrofitListener;

public class LoginActivityViewModel extends ViewModel {
    private MutableLiveData<User> authUser = new MutableLiveData<>();

    private MutableLiveData<String> toastString = new MutableLiveData<>();

    private UserService userService;


    public LoginActivityViewModel(){
        userService = new UserService();

        authUser = userService.getAuthUser();
    }

    public void authUser(String userLogin, String userPassword){
        userService.auth(userLogin, userPassword, new RetrofitListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure() {
                toastString.setValue("Такого пользователя не существует");
            }
        });
    }

    public MutableLiveData<User> getAuthUser() {
        return authUser;
    }

    public MutableLiveData<String> getToastString() {
        return toastString;
    }
}
