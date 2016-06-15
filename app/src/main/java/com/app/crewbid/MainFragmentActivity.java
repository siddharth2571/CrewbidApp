package com.app.crewbid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.crewbid.fragment.HomeFragment;
import com.app.crewbid.fragment.NavigationDrawerFragment;
import com.app.crewbid.fragment.postcrewvent.PostCrewventUploadFragment;
import com.app.crewbid.interfaces.KeyInterface;

import java.util.ArrayList;
import java.util.List;

public class MainFragmentActivity extends ActionBarActivity implements
        KeyInterface, NavigationDrawerFragment.NavigationDrawerCallbacks,
        OnClickListener {

    private ImageView imgDrawer;

    /**
     * Fragment managing the behaviors, interactions and presentation of the
     * navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in
     * {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private static int CURRENT_TOP_FRAGMENT;
    private TextView txtHeaderTitle;

    //sam
    private List<Fragment> mFragmentsStack = new ArrayList<>();
    private FragmentManager mFragmentManager;
    public static boolean visibilityState = false;

    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);

        // hide the action bar
        getSupportActionBar().hide();

        mFragmentManager = getSupportFragmentManager();
        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        init();


    }

    private void init() {
        imgDrawer = (ImageView) findViewById(R.id.imgDrawer);
        imgDrawer.setOnClickListener(this);

        txtHeaderTitle = (TextView) findViewById(R.id.txtHeaderTitle);
        switchContent(new HomeFragment(), true, FRAG_HOME);
//        popBackStackAll();

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.imgDrawer:
                Fragment fragment = getSupportFragmentManager().findFragmentById(
                        R.id.navigation_drawer);
                if (fragment != null) {
                    ((NavigationDrawerFragment) fragment).toggle();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        // FragmentManager fragmentManager = getSupportFragmentManager();
        // fragmentManager
        // .beginTransaction()
        // .replace(R.id.container,
        // PlaceholderFragment.newInstance(position + 1)).commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void toast(Activity activity, String string) {
        // TODO Auto-generated method stub
        Toast.makeText(activity, string, Toast.LENGTH_SHORT).show();
    }

    public static void toastNew(Activity activity, String string) {
        // TODO Auto-generated method stub
        Toast.makeText(activity, string, Toast.LENGTH_SHORT).show();
    }

    public void setHeader(String title) {
        txtHeaderTitle.setText(title);
    }

    public void switchContent(Fragment fragment, boolean isAddBackStack, int tag) {
//        FragmentTransaction ft = mFragmentManager.beginTransaction();

        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.popBackStack();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();

//        ft.addToBackStack(null);
//        ft.replace(R.id.container, fragment).commit();

    }


    public void setCurrentTopFragment(int id) {
        CURRENT_TOP_FRAGMENT = id;
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        mFragmentManager = getSupportFragmentManager();

//        Toast.makeText(this, "back", Toast.LENGTH_SHORT).show();
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        } else {
//            switchContent(new HomeFragment(), true, 0);
//            removeFragment();
            if (mFragmentManager.getBackStackEntryCount() > 1) {
                Log.i("MainActivity", "popping backstack");
//                mFragmentManager.popBackStack();
//                popBackStackAll();
                if (visibilityState) {
                    finish();
                } else {
                    switchContent(new HomeFragment(), true, 0);
                }
            } else {
                finish();
            }
        }
    }

    public void removeFragment() {
        Log.e("TAG", "removeFrag");
        Fragment fragment = mFragmentsStack.get(mFragmentsStack.size() - 2);
        Log.e("TAG", "frag " + fragment.getClass().getSimpleName());

        mFragmentsStack.remove(mFragmentsStack.size() - 1);

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    public void goBack() {
        if (getSupportFragmentManager() != null) {
            getSupportFragmentManager().popBackStack();
        }
    }

    public void goBack(int upTo) {
        if (getSupportFragmentManager() != null) {
            for (int i = 0; i < upTo; i++) {
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    public void popBackStackAll() {
        if (getSupportFragmentManager() != null) {
            getSupportFragmentManager().popBackStack(null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == PostCrewventUploadFragment.PICKLOGO_REQUEST_CODE
                || requestCode == PostCrewventUploadFragment.PICKATTACHMENT_REQUEST_CODE) {
            if (getSupportFragmentManager() == null)
                return;
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(
                    String.valueOf(FRAG_POST_CREWVENT_UPLOAD));
            if (fragment != null
                    && fragment instanceof PostCrewventUploadFragment) {
                // toast("Tap on location icon to get your current location");
                ((PostCrewventUploadFragment) fragment).onResultFromActivity(
                        requestCode, resultCode, intent);
            }
        }

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, intent);
        }
    }


}
