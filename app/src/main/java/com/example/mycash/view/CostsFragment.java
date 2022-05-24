package com.example.mycash.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mycash.R;
import com.example.mycash.databinding.FragmentCostsBinding;


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
        return binding.getRoot();
    }
}