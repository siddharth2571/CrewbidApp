package com.app.crewbid.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.crewbid.AppDelegate;
import com.app.crewbid.LoginActivity;
import com.app.crewbid.MainFragmentActivity;
import com.app.crewbid.R;
import com.app.crewbid.interfaces.KeyInterface;
import com.app.crewbid.jsonparser.ParserLogin;
import com.app.crewbid.model.ClsLogin;
import com.app.crewbid.network.AsyncTaskCompleteListener;
import com.app.crewbid.network.ClsNetworkResponse;
import com.app.crewbid.network.NetworkParam;
import com.app.crewbid.network.NetworkTask;
import com.app.crewbid.prefs.PreferenceData;
import com.app.crewbid.utility.ProgressDialogUtility;
import com.app.crewbid.utility.Utility;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginFragment extends Fragment implements OnClickListener,
        KeyInterface {

    private static final int REQ_LOGIN = 1;

    private Button btnSignup, btnLogin;
    private EditText edtUsername, edtPassword;
    private LoginButton fbLoginButton;
    private CallbackManager fbCallbackManager;

    public static final String USER_FRIENDS = "user_friends";
    public static final String ME_FRIENDS = "/me/friends";
    public static final String FB_DATA = "data";
    public static final String FB_NAME = "name";
    public static final String FRIEND_ID = "id";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        View root = inflater.inflate(R.layout.frag_login, container, false);
        fbCallbackManager = CallbackManager.Factory.create();
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        init();
        // startMainActivity();
    }

    private void init() {
        btnSignup = (Button) getView().findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(this);
        btnLogin = (Button) getView().findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        edtUsername = (EditText) getView().findViewById(R.id.edtUsername);
        edtPassword = (EditText) getView().findViewById(R.id.edtPassword);
        fbLoginButton = (LoginButton) getView().findViewById(R.id.facebooklogin);

        fbLoginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends"));
        fbLoginButton.setFragment(this);

//        fbLoginButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.facebook_icon, 0, 0, 0);
//        fbLoginButton.registerCallback(fbCallbackManager,);
        fbLoginButton.registerCallback(fbCallbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

//                Toast.makeText(getActivity(), loginResult.toString(), Toast.LENGTH_LONG).show();

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object,
                                                    GraphResponse response) {

                                ProgressDialogUtility.show(getActivity(), null, false);
                                // Application code
//                                userDetailView.setVisibility(View.VISIBLE);
//                                Toast.makeText(getActivity(), object.toString(), Toast.LENGTH_LONG).show();
                                parseFbUserDetail(object);
                                getUserFriends();

                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,first_name,last_name,age_range");

                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_LONG).show();
            }
        });

        if (IS_DEBUG) {
            edtUsername.setText("member");
            edtPassword.setText("member");
        }

    }


    public void getUserFriends() {

        Log.e("getUserFriends", "Called");
        new GraphRequest(AccessToken.getCurrentAccessToken(), ME_FRIENDS,
                null, HttpMethod.GET, new GraphRequest.Callback() {

            public void onCompleted(GraphResponse response) {

                Log.e("Friendlist", response.toString());
                //      String friendIds = null;
                StringBuilder friendIds = new StringBuilder();
                //friendIds.append("(");
                try {
                    JSONArray Arr = response.getJSONObject().getJSONArray(FB_DATA);
                    for (int i = 0; i < Arr.length(); i++) {
                        friendIds.append(Arr.getJSONObject(i).getString(FRIEND_ID));
                        friendIds.append(",");
                    }
                    //friendIds.append(")");

                    if (Arr.length() > 0) {
                        friendIds.deleteCharAt(friendIds.length() - 1);
                    }


                    Log.e("FB RESPONSE : ", "- -d-d--d -d-d -d- -d -d -- d-d d  d---   " + Arr.toString());
                    Log.e("FB Friend ID: ", "" + friendIds.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).executeAsync();
    }


    private void parseFbUserDetail(JSONObject object) {
        Log.e("object---", object.toString());

        String facebookId = object.optString("id");
        String firstName = object.optString("first_name");
        String fbid = object.optString("id");
        String photo = "https://graph.facebook.com/"
                + facebookId
                + "/picture?type=large";
        String email = object.optString("email");

        Utility.USER_EMAIL = email;
        SignupFragment signupFragment = new SignupFragment();
        ((LoginActivity) getActivity()).switchContent(signupFragment, true,
                FRAG_SIGNUP);
        ProgressDialogUtility.dismiss();
        Log.d("result", firstName + " = " + fbid + " = " + photo);

        /*user.setFirst_name(object.optString("first_name"));
       /* user.setFb_id(object.optString("id"));
        user.setPhoto("https://graph.facebook.com/"
                + facebookId
                + "/picture?type=large");
        if (object.has("email")) {
            user.setEmail(object.optString("email"));
        } else {
            user.setEmail(facebookId + "@Facebook.com");
        }

        if (object.has("age_range")) {
            try {
                JSONObject ageObjects = new JSONObject(object.optString("age_range"));
                user.setAge(ageObjects.optString("min"));
                Log.e("ageObjects", "--- " + user.getAge());
                int ageInt = Integer.parseInt(user.getAge()) - 14;
                if (ageInt > 0) {
                    ageSpinner.setSelection(ageInt);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        user.setFamily_name(object.optString("last_name"));
        user.setGender(object.optString("gender"));
        //  user.setAge(object.optString("age"));
        user.setZipcode(object.optString("zipcode"));
        user.setAddress(object.optString("address"));

        flockkSharedPreferences.edit().putString(Constants.Logged_User_FbData, object.toString()).commit();*//**//*
*/

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnSignup:
                SignupFragment signupFragment = new SignupFragment();
                ((LoginActivity) getActivity()).switchContent(signupFragment, true,
                        FRAG_SIGNUP);
                break;
            case R.id.btnLogin:
                if (!checkValidation()) {
                    return;
                }
                callLogin();
                break;
        }
    }

    private boolean checkValidation() {
        if (edtUsername.getText().toString().trim().length() == 0) {
            MainFragmentActivity.toast(getActivity(), "Please enter username");
            return false;
        } else if (edtPassword.getText().toString().length() == 0) {
            MainFragmentActivity.toast(getActivity(), "Please enter password");
            return false;
        }
        return true;
    }

    private void callLogin() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString();
        NetworkTask networkTask = new NetworkTask(getActivity());
        networkTask.setmCallback(asyncTaskCompleteListener);
        networkTask.setHashMapParams(new NetworkParam().getLoginParam(username,
                password));
        networkTask.execute(NetworkParam.METHOD_LOGIN,
                String.valueOf(REQ_LOGIN));
        ProgressDialogUtility.show(getActivity(), null, false);
    }

    AsyncTaskCompleteListener asyncTaskCompleteListener = new AsyncTaskCompleteListener() {
        @Override
        public void onTaskComplete(ClsNetworkResponse clsResponse) {
            // TODO Auto-generated method stub
            if (clsResponse.getRequestCode() == REQ_LOGIN) {
                ProgressDialogUtility.dismiss();
                if (clsResponse.isSuccess() && clsResponse.getObject() != null) {
                    MainFragmentActivity.toast(getActivity(),
                            clsResponse.getDispMessage());

                    ClsLogin clsLogin = (ClsLogin) clsResponse.getObject();
                    storeLoginData(clsLogin);
                    startMainActivity();
                } else {
                    MainFragmentActivity.toast(getActivity(),
                            clsResponse.getDispMessage());
                }
            }
        }

        @Override
        public ClsNetworkResponse doBackGround(ClsNetworkResponse clsResponse) {
            // TODO Auto-generated method stub
            if (!clsResponse.isSuccess()
                    || clsResponse.getResult_String() == null)
                return clsResponse;
            if (clsResponse.getRequestCode() == REQ_LOGIN) {
                clsResponse = new ParserLogin(clsResponse).parse();
            }
            return clsResponse;
        }
    };

    private void startMainActivity() {
        AppDelegate.storeValue(PreferenceData.IS_LOGGED_IN, true);
        Intent intent = new Intent(getActivity(), MainFragmentActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void storeLoginData(ClsLogin clsLogin) {
        AppDelegate.storeValue(PreferenceData.ADDRESS, clsLogin.getAddress());
        AppDelegate.storeValue(PreferenceData.ADDRESS2, clsLogin.getAddress2());
        AppDelegate.storeValue(PreferenceData.AREACODE, clsLogin.getAreaCode());
        AppDelegate.storeValue(PreferenceData.CITY, clsLogin.getCity());
        AppDelegate.storeValue(PreferenceData.COUNTRYCODE,
                clsLogin.getCountryCode());
        AppDelegate.storeValue(PreferenceData.EMAIL, clsLogin.getEmail());
        AppDelegate.storeValue(PreferenceData.EXTENTION,
                clsLogin.getExtention());
        AppDelegate.storeValue(PreferenceData.FIRSTNAME,
                clsLogin.getFirstName());
        AppDelegate.storeValue(PreferenceData.ISADMIN, clsLogin.getIsadmin());
        AppDelegate.storeValue(PreferenceData.LASTNAME, clsLogin.getLastName());
        AppDelegate.storeValue(PreferenceData.PHONE, clsLogin.getPhone());
        AppDelegate.storeValue(PreferenceData.SALT, clsLogin.getSalt());
        AppDelegate.storeValue(PreferenceData.STATE, clsLogin.getState());
        AppDelegate.storeValue(PreferenceData.USECOMPANYNAME,
                clsLogin.getUsecompanyname());
        AppDelegate.storeValue(PreferenceData.USERID, clsLogin.getUserId());
        AppDelegate.storeValue(PreferenceData.USERNAME, clsLogin.getUsername());
        AppDelegate.storeValue(PreferenceData.ZIPCODE, clsLogin.getZipCode());
        AppDelegate.storeValue(PreferenceData.ZIPCODE, clsLogin.getZipCode());
        AppDelegate.storeValue(PreferenceData.ISVENDOR, clsLogin.getIsvender());
        Log.i("ISVENDOR", "=>" + AppDelegate.getString(PreferenceData.ISVENDOR));
        AppDelegate.storeValue(PreferenceData.HASACCOUNT, clsLogin.getHasAccount());

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        fbCallbackManager.onActivityResult(requestCode, resultCode, data);
//        Toast.makeText(getActivity(), "FragmentOnResult", Toast.LENGTH_SHORT).show();

    }

}
