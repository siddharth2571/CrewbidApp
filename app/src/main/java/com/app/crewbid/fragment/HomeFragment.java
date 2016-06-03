package com.app.crewbid.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.crewbid.MainFragmentActivity;
import com.app.crewbid.R;
import com.app.crewbid.adapter.HomePagerStripPagerAdapter;
import com.app.crewbid.slidingtab.PagerSlidingTabStrip;
import com.facebook.FacebookSdk;

/**
 * Created by pcs145 on 12/15/2015.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    PagerSlidingTabStrip tabsStrip;
    ViewPager viewPager;
    HomePagerStripPagerAdapter adapter;

    public HomeFragment() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View rootView = inflater.inflate(R.layout.fragment_home, container,
                false);
        FacebookSdk.sdkInitialize(getContext().getApplicationContext());

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        initControls(getView());
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        ((MainFragmentActivity) getContext()).setHeader(getActivity()
                .getString(R.string.home));
    }

    private void initControls(View view) {
        mContext = getActivity();

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        adapter = new HomePagerStripPagerAdapter(getChildFragmentManager(),
                getActivity());
        viewPager.setAdapter(adapter);

        tabsStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        tabsStrip.setViewPager(viewPager);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        }
    }

    @Override
    public void onDestroyView() {
        // if(viewPager != null){
        // viewPager.clearOnPageChangeListeners();
        // }
        super.onDestroyView();

    }

}