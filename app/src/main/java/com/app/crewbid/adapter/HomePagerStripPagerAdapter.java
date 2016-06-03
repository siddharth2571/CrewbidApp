package com.app.crewbid.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.app.crewbid.AppDelegate;
import com.app.crewbid.fragment.MyCrewbidFragment;
import com.app.crewbid.fragment.ActivityFragment;
import com.app.crewbid.prefs.PreferenceData;

public class HomePagerStripPagerAdapter extends FragmentPagerAdapter {

    Context mContext;
    public SparseArray<Fragment> sparseArrayFrag = new SparseArray<Fragment>();

    public HomePagerStripPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        Log.e("ISVENDOR", "ISVENDOR:-" + AppDelegate.getString(PreferenceData.ISVENDOR));
        if (AppDelegate.getString(PreferenceData.ISVENDOR).equalsIgnoreCase("1")) {
            switch (position) {
                case 0:
                    fragment = new ActivityFragment();
                    break;

            }
        } else {
            switch (position) {
                case 0:
                    fragment = new ActivityFragment();
                    break;
                case 1:
                    fragment = new MyCrewbidFragment();
                    break;
            }
        }
        return fragment;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        Fragment fragment = (Fragment) super.instantiateItem(container,
                position);
        sparseArrayFrag.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        sparseArrayFrag.remove(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        if (AppDelegate.getString(PreferenceData.ISVENDOR).equalsIgnoreCase("1")) {
            return 1;
        }else {
            return 2;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if (AppDelegate.getString(PreferenceData.ISVENDOR).equalsIgnoreCase("1")) {
            switch (position) {
                case 0:
                    return "Activity";
                default:
                    return "";
            }
        } else {
            switch (position) {
                case 0:
                    return "Activity";
                case 1:
                    return "My Crewbid";
                default:
                    return "";
            }
        }
    }

    public SparseArray<Fragment> getSparseArrayFrag() {
        return sparseArrayFrag;
    }
}
