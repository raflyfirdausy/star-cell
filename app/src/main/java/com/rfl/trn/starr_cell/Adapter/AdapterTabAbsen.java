package com.rfl.trn.starr_cell.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdapterTabAbsen extends FragmentStatePagerAdapter {
    public final List<Fragment> fragmentList = new ArrayList<>();
    public final List<String> titleFragmentList = new ArrayList<>();

    public AdapterTabAbsen(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String tittle) {
        fragmentList.add(fragment);
        titleFragmentList.add(tittle);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleFragmentList.get(position);
    }
}
