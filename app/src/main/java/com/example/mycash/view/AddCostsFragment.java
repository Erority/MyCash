package com.example.mycash.view;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.mycash.R;
import com.example.mycash.adapter.OperationCategoryAdapter;
import com.example.mycash.adapter.OperationHistoryAdapter;
import com.example.mycash.databinding.FragmentAddCostsBinding;
import com.example.mycash.model.OperationCategory;
import com.example.mycash.model.OperationHistory;
import com.example.mycash.model.PostOperationHistory;
import com.example.mycash.model.User;
import com.example.mycash.utils.AdapterItemClickListener;
import com.example.mycash.utils.RetrofitListener;
import com.example.mycash.viewmodel.AddCostsViewModel;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddCostsFragment extends Fragment implements AdapterItemClickListener {

    private FragmentAddCostsBinding binding;
    private AddCostsViewModel viewModel;

    private OperationCategoryAdapter adapter;

    private User user;
    private OperationCategory selectedCategory;


    final Calendar myCalendar= Calendar.getInstance();
    private Date selectedDate = Calendar.getInstance().getTime();

    public AddCostsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        viewModel = new ViewModelProvider(this).get(AddCostsViewModel.class);
        binding = FragmentAddCostsBinding.inflate(inflater, container, false);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String json = preferences.getString("authUser", "");

        Gson gson = new Gson();
        user = gson.fromJson(json, User.class);


        AssetManager am = getContext().getResources().getAssets();
        try {
            InputStream is = am.open("calendardate.png");
            Drawable d = Drawable.createFromStream(is, null);

            binding.buttonCalendar.setBackground(d);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setUpRecyclerView();
        setObservers();
        setListener();
        return binding.getRoot();
    }

    private void setObservers(){
        viewModel.getCategoryList().observe(getActivity(), new Observer<List<OperationCategory>>() {
            @Override
            public void onChanged(List<OperationCategory> operationCategories) {
                adapter.setItems(operationCategories);
            }
        });
    }

    private void setListener(){

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);

                selectedDate = myCalendar.getTime();
            }
        };

        binding.buttonCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!validation())
                    return;

                PostOperationHistory operationHistoryToAdd = new PostOperationHistory();
                double cost =  Double.parseDouble(binding.editTextCost.getText().toString());
                operationHistoryToAdd.setAmount(cost);
                operationHistoryToAdd.setComment(binding.editTextComment.getText().toString());

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                
                operationHistoryToAdd.setCreationDate(format.format(selectedDate));
                operationHistoryToAdd.setOperationCategoryID(selectedCategory.getId());
                operationHistoryToAdd.setOperationTypeID(1);
                operationHistoryToAdd.setUserID(user.getId());


                viewModel.postOperation(new RetrofitListener() {
                    @Override
                    public void onSuccess() {
                        User changing = user;
                        changing.setBalance(user.getBalance() - cost);
                        viewModel.changeUserBalance(changing);

                        getActivity().setResult(CostsFragment.ADD_NEW_COST);
                        getActivity().finish();
                    }

                    @Override
                    public void onFailure() {

                    }
                }, operationHistoryToAdd);
            }
        });
    }
    
    private boolean validation(){
        if(binding.editTextCost.getText().toString().equals("") || binding.editTextCost.getText().toString() == null){
            Toast.makeText(getContext(), "Заполните расход", Toast.LENGTH_SHORT).show();
            return false;
        } else if (selectedCategory == null){
            Toast.makeText(getContext(), "Выберите категорию", Toast.LENGTH_SHORT).show();
            return false;
        } else if (selectedDate.after(Calendar.getInstance().getTime())){
            Toast.makeText(getContext(), "Выбранная дата не должна быть в будущем", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void setUpRecyclerView() {
        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(),4);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recycleView.setLayoutManager(layoutManager);

        adapter = new OperationCategoryAdapter(getContext(), this);
        binding.recycleView.setAdapter(adapter);
    }

    @Override
    public void onItemClicked(OperationCategory category) {
        selectedCategory = category;
    }
}