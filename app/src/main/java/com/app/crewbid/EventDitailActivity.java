package com.app.crewbid;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.app.crewbid.fragment.EventFragment;

/**
 * Created by uday nayak on 2/8/2016.
 */
public class EventDitailActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detailactivity);

        Bundle bundle=new Bundle();
//        android.support.v4.app.Fragment fragment = new EventFragment();
//        fragment.setArguments(bundle);
//        FragmentManager fragmentManager = getFragmentManager();
//
//        fragmentManager.beginTransaction().replace(R.id.container2, fragment).addToBackStack( "tag" ).commit();




    }
}
