package com.app.crewbid.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.app.crewbid.AppDelegate;
import com.app.crewbid.fragment.BidsFragment;
import com.app.crewbid.fragment.SummaryFragment;
import com.app.crewbid.fragment.SummaryVenderFragment;
import com.app.crewbid.prefs.PreferenceData;
import com.app.crewbid.utility.Utility;

public class EventPagerStripPagerAdapter extends FragmentPagerAdapter {

    Context mContext;
    public SparseArray<Fragment> sparseArrayFrag = new SparseArray<Fragment>();

    public EventPagerStripPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Log.e("userid", "=>" + Utility.getUserIdSummery());
        Log.e("userid", "=>user" + AppDelegate.getString(PreferenceData.USERID));

        if (AppDelegate.getString(PreferenceData.ISVENDOR).equalsIgnoreCase("1")) {

            switch (position) {
                case 0:
                    fragment = new SummaryVenderFragment();
                    Log.e("userid", "=>" + Utility.getUserIdSummery());
                    break;
            }
        } else {
            switch (position) {
                case 0:
                    fragment = new SummaryFragment();
                    break;
                case 1:
                    fragment = new BidsFragment();
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
        } else {
            if (Utility.getUserIdSummery().equalsIgnoreCase(AppDelegate.getString(PreferenceData.USERID)))
                return 2;
            else
                return 1;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if (AppDelegate.getString(PreferenceData.ISVENDOR).equalsIgnoreCase("1")) {
            switch (position) {
                case 0:
                    return "Summary";
                default:
                    return "";
            }
        } else {
            switch (position) {
                case 0:
                    return "Summary";
                case 1:
                    return "Bids";
//            case 2:
//                return "Alert";
//            case 3:
//                return "Share";
                default:
                    return "";
            }
        }
    }

    public SparseArray<Fragment> getSparseArrayFrag() {
        return sparseArrayFrag;
    }
}
