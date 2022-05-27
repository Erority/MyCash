package com.example.mycash.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.mycash.adapter.AddTabPageAdapter;
import com.example.mycash.adapter.TabPageAdapter;
import com.example.mycash.databinding.ActivityAddOperationBinding;
import com.google.android.material.tabs.TabLayout;

public class AddOperationActivity extends AppCompatActivity {

    private ActivityAddOperationBinding binding;
    private boolean isIncomes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddOperationBinding.inflate(getLayoutInflater());

        Intent intent = getIntent();
        isIncomes = intent.getBooleanExtra("isIncomes", false);

        setTabLayout();

        setContentView(binding.getRoot());
    }

    private void setTabLayout(){
        AddTabPageAdapter adapter = new AddTabPageAdapter(this);

        binding.viewPager.setAdapter(adapter);

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position));
            }
        });

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if(isIncomes){
            TabLayout.Tab tab = binding.tabLayout.getTabAt(1);
            tab.select();
        }

    }
}