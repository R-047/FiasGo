package com.example.fiasgo;

import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentManagerNonConfig;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FeedsSlider extends FragmentStateAdapter {
    public FeedsSlider(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }




    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new VicimsFragment();
            case 1:
                return new ServicesFragment();
        }
        return new VicimsFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }



}
