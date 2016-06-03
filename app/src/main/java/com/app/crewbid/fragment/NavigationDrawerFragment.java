package com.app.crewbid.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.crewbid.AppDelegate;
import com.app.crewbid.CardDetailActivity;
import com.app.crewbid.LoginActivity;
import com.app.crewbid.MainFragmentActivity;
import com.app.crewbid.R;
import com.app.crewbid.fragment.postcrewvent.PostCrewventFormFragment;
import com.app.crewbid.interfaces.KeyInterface;
import com.app.crewbid.prefs.PreferenceData;
import com.facebook.login.LoginManager;

/**
 * Fragment used for managing interactions for and presentation of a navigation
 * drawer. See the <a href=
 * "https://developer.android.com/design/patterns/navigation-drawer.html#Interaction"
 * > design guidelines</a> for a complete explanation of the behaviors
 * implemented here.
 */
public class NavigationDrawerFragment extends Fragment implements KeyInterface,
        OnClickListener {

    private static final int DRAWER_DURATION = 300;

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the
     * user manually expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    private TextView txtPostCrewvent, txtHome;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated
        // awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState
                    .getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of
        // actions in the action bar.
        setHasOptionsMenu(true);
        init(getView());
    }

    private void init(View view) {


        Log.e("ISVENDOR", "ISVENDOR:-" + AppDelegate.getString(PreferenceData.ISVENDOR));

        TextView txtHome = (TextView) view.findViewById(R.id.txtHome);
        txtHome.setOnClickListener(this);

        TextView txtTopTen = (TextView) view.findViewById(R.id.txtTopTen);
        txtTopTen.setOnClickListener(this);

        TextView txtTrackList = (TextView) view.findViewById(R.id.txtTrackList);
//		txtTrackList.setOnClickListener(this);

        TextView txtPostCrewvent = (TextView) view.findViewById(R.id.txtPostCrewvent);
        TextView txtBrowseVendors = (TextView) view.findViewById(R.id.txtBrowseVendors);
        TextView txtSwitchTo = (TextView) view.findViewById(R.id.txtSwitchTo);
        TextView txtUserName = (TextView) view.findViewById(R.id.txtUserName);
        txtUserName.setText(AppDelegate.getString(PreferenceData.USERNAME));

        TextView txtSettings = (TextView) view.findViewById(R.id.txtSettings);

        txtBrowseVendors.setOnClickListener(this);

        if (AppDelegate.getString(PreferenceData.ISVENDOR).toString().equals("1")) {
            txtSettings.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    startActivity(new Intent(getActivity(), CardDetailActivity.class));

                }
            });
        }

        ImageView ivUser = (ImageView) view.findViewById(R.id.ImgUser);

        TextView txtLogout = (TextView) view.findViewById(R.id.textLogout);
        txtLogout.setOnClickListener(this);

        if (AppDelegate.getString(PreferenceData.ISVENDOR).equalsIgnoreCase("1")) {

            txtSwitchTo.setText("Switch to Member");
            txtTrackList.setVisibility(View.VISIBLE);
            txtPostCrewvent.setVisibility(View.GONE);
            txtBrowseVendors.setVisibility(View.GONE);

//            ivUser.setImageResource(getResources().getDrawable());
//            ivUser.setImageDrawable(getActivity().getDrawable(, R.drawable.temp_profile));

            final int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                ivUser.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.avatar_menu));
            } else {
                ivUser.setBackground(getActivity().getResources().getDrawable(R.drawable.avatar_menu));
            }


        } else {
            txtTrackList.setVisibility(View.GONE);
            txtPostCrewvent.setVisibility(View.VISIBLE);
            txtBrowseVendors.setVisibility(View.VISIBLE);


            txtSwitchTo.setText("Switch to Vendor");

            final int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                ivUser.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.temp_profile));
            } else {
                ivUser.setBackground(getActivity().getResources().getDrawable(R.drawable.temp_profile));
            }


        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_navigation_drawer,
                container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        txtPostCrewvent = (TextView) view.findViewById(R.id.txtPostCrewvent);
        txtPostCrewvent.setOnClickListener(this);
        txtHome = (TextView) view.findViewById(R.id.txtHome);
        txtHome.setOnClickListener(this);
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null
                && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    public void toggle() {
        if (isDrawerOpen()) {
            mDrawerLayout.closeDrawer(Gravity.START);
        } else {
            mDrawerLayout.openDrawer(Gravity.START);
        }
    }

    /**
     * Users of this fragment must call this method to set up the navigation
     * drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer
        // opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.hide();
        // actionBar.setDisplayHomeAsUpEnabled(true);
        // actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), /* host Activity */
                mDrawerLayout, /* DrawerLayout object */
                R.drawable.ic_menu, /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open, /*
                                         * "open drawer" description for
										 * accessibility
										 */
                R.string.navigation_drawer_close /*
                                         * "close drawer" description for
										 * accessibility
										 */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu(); // calls
                // onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to
                    // prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true)
                            .commit();
                }

                getActivity().supportInvalidateOptionsMenu(); // calls
                // onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce
        // them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar.
        // See also
        // showGlobalContextActionBar, which controls the top-left area of the
        // action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (item.getItemId() == R.id.action_example) {
            Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to
     * show the global app 'context', rather than just what's in the current
     * screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    /**
     * Callbacks interface that all activities using this fragment must
     * implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }

    private void closeDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(Gravity.START);
        }
    }

    int fragmentId;
    Fragment fragmentName;

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        closeDrawer();
        switch (v.getId()) {
            case R.id.txtHome:
                fragmentId = FRAG_HOME;
                fragmentName = new HomeFragment();
                break;
            case R.id.txtPostCrewvent: {
                fragmentId = FRAG_POST_CREWVENT_FORM;
                fragmentName = new PostCrewventFormFragment();
                break;
            }
            case R.id.txtBrowseVendors: {
//                fragmentId = FRAG_BROWSE_VENDORS;
                fragmentName = new BrowseVendorsFragment();
                break;
            }
            case R.id.txtNotification: {
                fragmentId = FRAG_NOTIFICATION;
                // fragmentName = new NotificationsFragment();
                break;
            }
            case R.id.txtTopTen: {
                fragmentId = FRAG_TOP_TEN;
                fragmentName = new TopTenFragment();
                break;
            }
            case R.id.txtSettings: {
                fragmentId = FRAG_SETTINGS;
                // fragmentName = new SettingsFragment();
                break;
            }
            case R.id.textLogout: {
                fragmentId = FRAG_LOGOUT;
                // fragmentName = new SettingsFragment();

                AppDelegate.storeValue(PreferenceData.IS_LOGGED_IN, false);
                startActivity(new Intent(getContext(), LoginActivity.class));
                LoginManager.getInstance().logOut();

//                startActivity(new Intent(NavigationDrawerFragment.this, LoginActivity.class));

                break;
            }
            default:
                break;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switchFragment(fragmentId, fragmentName);
            }
        }, DRAWER_DURATION);
    }

    private void switchFragment(int fragmentId, Fragment fragmentName) {
        if (fragmentName != null) {
            //((MainFragmentActivity) getActivity()).popBackStackAll();
            ((MainFragmentActivity) getActivity()).switchContent(fragmentName,
                    true, fragmentId);
        }
    }

}
