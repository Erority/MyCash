package com.example.mycash.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mycash.view.CostsFragment;
import com.example.mycash.view.IncomesFragment;

public class TabPageAdapter extends FragmentStateAdapter {


    public TabPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new CostsFragment();
            case 1:
                return new IncomesFragment();
            default:
                return new CostsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
