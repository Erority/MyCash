package com.example.mycash.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.example.mycash.MainActivity;
import com.example.mycash.databinding.ActivityLoginBinding;
import com.example.mycash.model.User;
import com.example.mycash.viewmodel.LoginActivityViewModel;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {

    private LoginActivityViewModel viewModel;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(LoginActivityViewModel.class);


        setListener();
        setObserver();


        setContentView(binding.getRoot());
    }

    private void setListener(){

        binding.buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validation())
                    return;

                viewModel.authUser(binding.editTextLogin.getText().toString(), binding.editTextPassword.getText().toString());
            }
        });
    }

    private void setObserver(){
        viewModel.getAuthUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user != null) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();

                    Gson gson = new Gson();

                    editor.putString("authUser", gson.toJson(user)); // changed from liked
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                }
            }
        });

        viewModel.getToastString().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(LoginActivity.this, s, Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validation() {
        if(binding.editTextLogin.getText().toString().equals("")){
            Toast.makeText(LoginActivity.this, "Заполните логин", Toast.LENGTH_LONG).show();
            return false;
        } else if (binding.editTextPassword.getText().toString().equals("")){
            Toast.makeText(LoginActivity.this, "Заполните пароль", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}