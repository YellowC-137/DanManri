package com.example.dku_lf.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.dku_lf.ui.home.FoundFragment;
import com.example.dku_lf.ui.home.LostFragment;

public class HomePagerAdapter extends FragmentStateAdapter {

    public HomePagerAdapter(@NonNull Fragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return new FoundFragment();
            case 1:
                return new LostFragment();
            default:
                return new LostFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
