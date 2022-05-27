package com.example.mycash.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mycash.model.OperationCategory;
import com.example.mycash.model.OperationHistory;
import com.example.mycash.model.PostOperationHistory;
import com.example.mycash.model.User;
import com.example.mycash.services.OperationCategoryService;
import com.example.mycash.services.OperationHistoryService;
import com.example.mycash.services.UserService;
import com.example.mycash.utils.RetrofitListener;

import java.util.List;

public class AddCostsViewModel extends ViewModel {

    private OperationCategoryService operationCategoryService = new OperationCategoryService(true);
    private OperationHistoryService operationHistoryService = new OperationHistoryService();
    private UserService userService = new UserService();


    private MutableLiveData<List<OperationCategory>> categoryList = new MutableLiveData<>();

    public AddCostsViewModel(){
        categoryList = operationCategoryService.getCategories();
    }

    public void postOperation(final RetrofitListener retrofitListener, PostOperationHistory operationHistory) {
        operationHistoryService.postOperation(retrofitListener, operationHistory);
    }

    public void changeUserBalance(User user){
        userService.updateUserBalance(user);
    }


    public MutableLiveData<List<OperationCategory>> getCategoryList() {
        return categoryList;
    }
}
