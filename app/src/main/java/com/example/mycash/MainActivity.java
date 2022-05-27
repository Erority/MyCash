package com.example.mycash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.example.mycash.adapter.TabPageAdapter;
import com.example.mycash.databinding.ActivityMainBinding;
import com.example.mycash.interfaces.IChangeFragment;
import com.example.mycash.interfaces.UpdateViewFromFragment;
import com.example.mycash.model.User;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements UpdateViewFromFragment {

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        setUI();
        setTabLayout();
    }


    private void setUI(){
        Gson gson = new Gson();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String json = preferences.getString("authUser", "");
        User user = gson.fromJson(json, User.class);

        StringBuilder sb = new StringBuilder();

        //set balance
        String balance = sb.append(user.getBalance()).toString();
        binding.textMoney.setText(balance + " Р");

        //set userImage
        AssetManager am = getResources().getAssets();
        try {
            InputStream is = am.open(user.getImage());
            Drawable d = Drawable.createFromStream(is, null);
            binding.userImage.setImageDrawable(d);
            is.close();
            // use the input stream as you want
        } catch (IOException e) {
            e.printStackTrace();
        }

        AssetManager am2 = getResources().getAssets();
        try {
            InputStream is2 = am2.open("free-icon-ruble-6507120.png");
            Drawable d2 = Drawable.createFromStream(is2, null);
            binding.imageViewBill.setImageDrawable(d2);
            is2.close();
            // use the input stream as you want
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setTabLayout(){
        TabPageAdapter adapter = new TabPageAdapter(this, this::updateSumm);

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

        binding.buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void updateSumm(String summ) {
        binding.textMoney.setText(summ + " Р");
    }
}