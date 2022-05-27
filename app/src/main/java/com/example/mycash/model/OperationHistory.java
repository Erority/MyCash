package com.example.mycash.model;

import com.example.mycash.services.OkHttpBuilder;
import com.example.mycash.services.OperationHistoryCall;
import com.example.mycash.services.OperationHistoryService;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OperationHistory {
    private int id;
    private int operationTypeID;
    private int operationCategoryID;
    private String creationDate;
    private String comment;
    private int amount;
    private int userID;

    private Date creationDateDate;

    private OperationCategory operationCategory;

    public OperationCategory getOperationCategory() {
        return operationCategory;
    }

    public void setOperationCategory(OperationCategory operationCategory) {
        this.operationCategory = operationCategory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOperationTypeID() {
        return operationTypeID;
    }

    public void setOperationTypeID(int operationTypeID) {
        this.operationTypeID = operationTypeID;
    }

    public int getOperationCategoryID() {
        return operationCategoryID;
    }

    public void setOperationCategoryID(int operationCategoryID) {
        this.operationCategoryID = operationCategoryID;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setCreationDateDate(Date creationDateDate) {
        this.creationDateDate = creationDateDate;
    }

    public Date getCreationDateDate() {
        return creationDateDate;
    }
}
