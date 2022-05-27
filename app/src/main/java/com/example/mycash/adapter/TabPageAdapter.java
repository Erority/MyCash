package com.example.mycash.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mycash.interfaces.UpdateViewFromFragment;
import com.example.mycash.view.CostsFragment;
import com.example.mycash.view.IncomesFragment;

public class TabPageAdapter extends FragmentStateAdapter {
    UpdateViewFromFragment updateViewFromFragment;

    public TabPageAdapter(@NonNull FragmentActivity fragmentActivity, UpdateViewFromFragment updateViewFromFragment) {
        super(fragmentActivity);

        this.updateViewFromFragment = updateViewFromFragment;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new CostsFragment(updateViewFromFragment);
            case 1:
                return new IncomesFragment(updateViewFromFragment);
            default:
                return new CostsFragment(updateViewFromFragment);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
