package com.example.mycash.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mycash.model.OperationHistory;
import com.example.mycash.services.OperationHistoryService;

import java.util.Date;
import java.util.List;

public class IncomesViewModel extends ViewModel {

    private OperationHistoryService operationHistoryService;

    private MutableLiveData<List<OperationHistory>> usersOperation;

    public IncomesViewModel(){
        operationHistoryService = new OperationHistoryService();

        usersOperation = operationHistoryService.getUserOperationHistory();
    }

    public void getOperationHistory(int userID, Date date){
        operationHistoryService.getUsersOperations(userID, 2, date);
    }

    public void updateOperation(int userID, Date date){
        operationHistoryService.getUsersOperations(userID, 2, date);
    }

    public MutableLiveData<List<OperationHistory>> getUsersOperation() {
        return usersOperation;
    }

}
