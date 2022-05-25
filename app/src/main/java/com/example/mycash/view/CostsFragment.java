package com.example.mycash.view;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mycash.R;
import com.example.mycash.databinding.FragmentCostsBinding;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;


public class CostsFragment extends Fragment {

    private FragmentCostsBinding binding;

    public CostsFragment() {
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
        binding = FragmentCostsBinding.inflate(inflater ,container, false);


        setDataPieChart();

        return binding.getRoot();
    }

    private void setDataPieChart(){
        List<PieEntry> entries= new ArrayList<>();

        entries.add(new PieEntry((float) ((Math.random() * 30) + 30 / 5)));
        entries.add(new PieEntry((float) ((Math.random() * 30) + 30 / 5)));
        entries.add(new PieEntry((float) ((Math.random() * 30) + 30 / 5)));
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


        //remove useless view
        binding.chart.getDescription().setEnabled(false);
        binding.chart.getLegend().setEnabled(false);
    }
}