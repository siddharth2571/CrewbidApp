package com.app.crewbid;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.app.crewbid.fragment.SplashFragment;
import com.app.crewbid.interfaces.KeyInterface;
import com.app.crewbid.prefs.PreferenceData;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

public class SplashActivity extends FragmentActivity implements KeyInterface {

    public static int CURRENT_FRAGMENT = 0;
    private GoogleCloudMessaging googleCloudMessaging;
    private String regId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        switchContent(new SplashFragment(), false, FRAG_SPLASH);
        registerInBackground();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startNext();
            }
        }, 3000);
    }

    private void startNext() {
        if (AppDelegate.getBoolean(PreferenceData.IS_LOGGED_IN)) {
            startActivity(new Intent(SplashActivity.this,
                    MainFragmentActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }
        SplashActivity.this.finish();
    }

    public void switchContent(Fragment fragment, boolean isAddBackStack, int tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment, String.valueOf(tag));
        if (isAddBackStack)
            ft.addToBackStack(null);
        ft.commit();
        CURRENT_FRAGMENT = Integer.valueOf(tag);
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (googleCloudMessaging == null) {
                        googleCloudMessaging = GoogleCloudMessaging.getInstance(SplashActivity.this);
                    }
                    regId = googleCloudMessaging.register("282973677367");
                    Log.e(SplashActivity.class.getSimpleName(), regId);


                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                } catch (IOException ex) {
                    Log.i("error", ex.getMessage());
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
            }
        }.execute(null, null, null);
    }
}
