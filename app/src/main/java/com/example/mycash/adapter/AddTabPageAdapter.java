package com.example.mycash.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mycash.view.AddCostsFragment;
import com.example.mycash.view.AddIncomesFragment;
import com.example.mycash.view.CostsFragment;
import com.example.mycash.view.IncomesFragment;

public class AddTabPageAdapter extends FragmentStateAdapter {


    public AddTabPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new AddCostsFragment();
            case 1:
                return new AddIncomesFragment();
            default:
                return new AddCostsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
