package com.app.crewbid.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.crewbid.fragment.BrowseBlanck;

/**
 * Created by siddharth on 5/4/2016.
 */
public class BrowseVendorAdapter extends FragmentPagerAdapter {
    public BrowseVendorAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new BrowseBlanck();
            case 1:
                // Games fragment activity
                return new BrowseBlanck();
            case 2:
                // Movies fragment activity
                return new BrowseBlanck();
        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        String[] title = new String[]{"Popular", "New", "Most"};
        return title[position];
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }


}
