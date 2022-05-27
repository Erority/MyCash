package com.example.mycash.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.example.mycash.databinding.ActivityBillInfoBinding;
import com.example.mycash.model.OperationHistory;
import com.example.mycash.model.User;
import com.example.mycash.services.OperationHistoryService;
import com.example.mycash.services.UserService;
import com.example.mycash.utils.RetrofitListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;

public class BillInfoActivity extends AppCompatActivity {

    private ActivityBillInfoBinding binding;

    private OperationHistory op;

    private User user;

    private UserService userService = new UserService();
    private OperationHistoryService operationHistoryService = new OperationHistoryService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBillInfoBinding.inflate(getLayoutInflater());

        Intent intent = getIntent();

        Gson gson = new Gson();
        String json = intent.getStringExtra("history");
        op = gson.fromJson(json, OperationHistory.class);
        setUI(op);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userJS = preferences.getString("authUser", "");

        user = gson.fromJson(userJS, User.class);


        setListener();

        setContentView(binding.getRoot());
    }

    private void setUI(OperationHistory operationHistory) {
        StringBuilder sb = new StringBuilder();
        sb.append(operationHistory.getAmount());

        binding.textViewCost.setText(sb.toString() + " Р");

        AssetManager am = this.getResources().getAssets();
        try {
            InputStream is = am.open(operationHistory.getOperationCategory().getImage());
            Drawable d = Drawable.createFromStream(is, null);

            binding.imageViewCategory.setImageDrawable(d);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setListener(){
        binding.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(BillInfoActivity.this);

                alertDialog.setTitle("Предупреждение");
                alertDialog.setMessage("Вы действительно хотите удалить данный рецепт ?");

                alertDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        operationHistoryService.deleteOperation(op.getId(), new RetrofitListener() {
                            @Override
                            public void onSuccess() {
                                if(op.getOperationTypeID() == 1){
                                    user.setBalance(user.getBalance() + op.getAmount());
                                    userService.updateUserBalance(user);
                                } else {
                                    user.setBalance(user.getBalance() - op.getAmount());
                                    userService.updateUserBalance(user);
                                }

                                setResult(CostsFragment.DELETE_HISTORY);
                                finish();
                            }

                            @Override
                            public void onFailure() {

                            }
                        });
                    }
                });

                alertDialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog  ad = alertDialog.create();
                        ad.cancel();
                    }
                });

                alertDialog.show();
            }
        });

        binding.buttonGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}