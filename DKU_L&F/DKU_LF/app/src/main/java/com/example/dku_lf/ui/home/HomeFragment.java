package com.example.dku_lf.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.dku_lf.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ViewPager2 viewPager2 = root.findViewById(R.id.viewPager);
        viewPager2.setAdapter(new HomePagerAdapter(this));

        TabLayout tabLayout = root.findViewById(R.id.tabLayout);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                switch(position){
                    case 0:{
                        tab.setText("FOUND");
                        break;
                    }
                    case 1:{
                        tab.setText("LOST");
                        break;
                    }
                }

            }
        }
        );

        tabLayoutMediator.attach();


        return root;

    }
}