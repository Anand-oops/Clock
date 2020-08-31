package com.anand.clock;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter {

    int tabCount;
    private Context context;

    public FragmentAdapter(Context context, FragmentManager fragmentManager, int count) {
        super(fragmentManager);
        this.context = context;
        this.tabCount = count;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AlarmFragment();
            case 1:
                return new StopwatchFragment();
            case 2:
                return new TimerFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
