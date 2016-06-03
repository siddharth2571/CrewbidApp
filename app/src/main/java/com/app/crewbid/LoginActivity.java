package com.app.crewbid;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;

import com.app.crewbid.fragment.LoginFragment;
import com.app.crewbid.fragment.SignupFragment;
import com.app.crewbid.interfaces.KeyInterface;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends FragmentActivity implements KeyInterface {

    public static int CURRENT_TOP_FRAGMENT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//        FacebookSdk.sdkInitialize(getApplicationContext().getApplicationContext());

        showHashKey(getApplicationContext());

        setContentView(R.layout.activity_login);
        switchContent(new LoginFragment(), false, FRAG_LOGIN);

    }

    public static void showHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "com.app.crewbid", PackageManager.GET_SIGNATURES); //Your            package name here
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    public void switchContent(Fragment fragment, boolean isAddBackStack, int tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment, String.valueOf(tag));
        if (isAddBackStack)
            ft.addToBackStack(null);
        ft.commit();
        CURRENT_TOP_FRAGMENT = Integer.valueOf(tag);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        boolean back = true;
        if (CURRENT_TOP_FRAGMENT == FRAG_SIGNUP) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(
                    String.valueOf(FRAG_SIGNUP));
            if (fragment != null) {
                back = ((SignupFragment) fragment).checkBack();
            }
        }

        if (back) {
            super.onBackPressed();
        }
    }


}
