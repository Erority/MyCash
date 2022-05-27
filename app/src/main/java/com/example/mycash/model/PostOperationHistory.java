package com.example.mycash.model;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PostOperationHistory {
    @SerializedName("operationTypeID")
    private int operationTypeID;
    @SerializedName("operationCategoryID")
    private int operationCategoryID;
    @SerializedName("creationDate")
    private String creationDate;
    @SerializedName("comment")
    private String comment;
    @SerializedName("amount")
    private double amount;
    @SerializedName("userID")
    private int userID;

    private Date creationDateDate;

    @SerializedName("operationTypeID")
    public int getOperationTypeID() {
        return operationTypeID;
    }

    @SerializedName("operationTypeID")
    public void setOperationTypeID(int operationTypeID) {
        this.operationTypeID = operationTypeID;
    }

    @SerializedName("operationCategoryID")
    public int getOperationCategoryID() {
        return operationCategoryID;
    }

    @SerializedName("operationCategoryID")
    public void setOperationCategoryID(int operationCategoryID) {
        this.operationCategoryID = operationCategoryID;
    }

    @SerializedName("creationDate")
    public String getCreationDate() {
        return creationDate;
    }

    @SerializedName("creationDate")
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;

    }

    @SerializedName("comment")
    public String getComment() {
        return comment;
    }

    @SerializedName("comment")
    public void setComment(String comment) {
        this.comment = comment;
    }

    @SerializedName("amount")
    public double getAmount() {
        return amount;
    }

    @SerializedName("amount")
    public void setAmount(double amount) {
        this.amount = amount;
    }

    @SerializedName("userID")
    public int getUserID() {
        return userID;
    }

    @SerializedName("userID")
    public void setUserID(int userID) {
        this.userID = userID;
    }
}
