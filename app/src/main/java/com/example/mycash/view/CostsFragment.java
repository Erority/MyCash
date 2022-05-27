package com.example.mycash.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.mycash.MainActivity;
import com.example.mycash.R;
import com.example.mycash.adapter.OperationHistoryAdapter;
import com.example.mycash.databinding.FragmentCostsBinding;
import com.example.mycash.interfaces.UpdateViewFromFragment;
import com.example.mycash.model.OperationHistory;
import com.example.mycash.model.User;
import com.example.mycash.services.UserService;
import com.example.mycash.utils.HistoryAdapterItemClickListener;
import com.example.mycash.viewmodel.CostsViewModel;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class CostsFragment extends Fragment implements HistoryAdapterItemClickListener {

    private FragmentCostsBinding binding;
    private CostsViewModel viewModel;

    private OperationHistoryAdapter adapter;

    UpdateViewFromFragment updateViewFromFragment;

    public static final int ADD_NEW_COST = 2;
    public static final int DELETE_HISTORY = 3;

    private User user;

    final Calendar myCalendar= Calendar.getInstance();
    private Date selectedDate = Calendar.getInstance().getTime();

    public CostsFragment(UpdateViewFromFragment updateViewFromFragment) {
        // Required empty public constructor
        this.updateViewFromFragment = updateViewFromFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCostsBinding.inflate(inflater ,container, false);
        viewModel = new ViewModelProvider(getActivity()).get(CostsViewModel.class);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String json = preferences.getString("authUser", "");

        Gson gson = new Gson();
        user = gson.fromJson(json, User.class);

        viewModel.getOperationHistory(user.getId(), selectedDate);


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
        setUpPiechart();

        setListeners();

        binding.chart.setNoDataText("У вас нет расходов за эту дату");
        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_NEW_COST|| requestCode == DELETE_HISTORY){
            viewModel.updateOperation(user.getId(), selectedDate);

            //update user
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    UserService userService = new UserService();
                    try {
                        user = userService.updateUser(user.getId());
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor editor = preferences.edit();

                        Gson gson = new Gson();

                        editor.putString("authUser", gson.toJson(user)); // changed from liked
                        editor.apply();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
            try {
                thread.join();

                //update UI
                StringBuilder sb = new StringBuilder();
                sb.append(user.getBalance());
                updateViewFromFragment.updateSumm(sb.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    private void setObservers(){
        viewModel.getUsersOperation().observe(getActivity(), new Observer<List<OperationHistory>>() {
            @Override
            public void onChanged(List<OperationHistory> operationHistories) {
                if(operationHistories.size() != 0) {
                    setDataPieChart(operationHistories);
                    adapter.clearItems();
                    adapter.setItems(operationHistories);
                    binding.chart.invalidate();
                } else {
                    binding.chart.setData(null);
                    binding.chart.invalidate();
                }
            }
        });
    }

    private void setListeners(){
        binding.buttonAddCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddOperationActivity.class);
                startActivityForResult(intent, ADD_NEW_COST);
            }
        });

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);

                selectedDate = myCalendar.getTime();
                viewModel.updateOperation(user.getId(), selectedDate);
            }
        };

        binding.buttonCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void setUpRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recycleView.setLayoutManager(layoutManager);

        adapter = new OperationHistoryAdapter(getContext(), this::onItemClicked);
        binding.recycleView.setAdapter(adapter);
    }

    private void setDataPieChart(List<OperationHistory> histories){
        List<PieEntry> entries= new ArrayList<>();
        double summ = 0;

        for (OperationHistory op: histories) {
            entries.add(new PieEntry((float) (op.getAmount())));
            summ += op.getAmount();
        }

        PieDataSet dataSet = new PieDataSet(entries, null);

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);


        PieData data = new PieData(dataSet);

        data.setDrawValues(false);

        binding.chart.setData(data);
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);

        StringBuilder sb = new StringBuilder();
        sb.append(summ);
        binding.chart.setCenterText(sb.toString() + " Р");
    }

    private void setUpPiechart() {
        //remove useless view
        binding.chart.getDescription().setEnabled(false);
        binding.chart.getLegend().setEnabled(false);

        binding.chart.animateY(1000, Easing.EaseInOutQuad);
    }

    @Override
    public void onItemClicked(OperationHistory history) {
        Intent intent = new Intent(getContext(), BillInfoActivity.class);

        Gson gson = new Gson();
        String json = gson.toJson(history);

        intent.putExtra("history", json);

        startActivityForResult(intent, DELETE_HISTORY);
    }
}