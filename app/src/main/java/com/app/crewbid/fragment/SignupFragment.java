package com.app.crewbid.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.crewbid.AppDelegate;
import com.app.crewbid.LoginActivity;
import com.app.crewbid.MainFragmentActivity;
import com.app.crewbid.R;
import com.app.crewbid.jsonparser.ParserSignup;
import com.app.crewbid.model.ClsSignup;
import com.app.crewbid.network.AsyncTaskCompleteListener;
import com.app.crewbid.network.ClsNetworkResponse;
import com.app.crewbid.network.NetworkParam;
import com.app.crewbid.network.NetworkTask;
import com.app.crewbid.prefs.PreferenceData;
import com.app.crewbid.utility.ProgressDialogUtility;
import com.app.crewbid.utility.Utility;

public class SignupFragment extends Fragment implements OnClickListener {

    private static final int REQ_SIGNUP = 1;
    private LinearLayout linearMemberVendor, linearField, debitcard_ll;
    private Button btnContinue, btnSkip;
    private EditText edtEmail, edtPhone, edtUsername, edtPassword, edtConfirmPassword, edtCardHolderName, edtDebitCardNumber, edtExpiryMonth, edtExpiryYear, edtDebitCvv;

    private CheckBox chkMembers, chkVendors;
    private RelativeLayout relativeVendors, relativeMembers;
    private boolean isVendor = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View root = inflater.inflate(R.layout.frag_signup, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        linearMemberVendor = (LinearLayout) getView().findViewById(
                R.id.linearMemberVendor);
        linearField = (LinearLayout) getView().findViewById(R.id.linearField);
        debitcard_ll = (LinearLayout) getView().findViewById(R.id.debitcard_ll);
        btnContinue = (Button) getView().findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(this);
        btnSkip = (Button) getView().findViewById(R.id.btnSkip);

        edtEmail = (EditText) getView().findViewById(R.id.edtEmail);
        edtUsername = (EditText) getView().findViewById(R.id.edtUsername);
        edtPassword = (EditText) getView().findViewById(R.id.edtPassword);
        edtPhone = (EditText) getView().findViewById(R.id.edtPhone);

        edtConfirmPassword = (EditText) getView().findViewById(
                R.id.edtConfirmPassword);

        edtCardHolderName = (EditText) getView().findViewById(R.id.edtCardHolderName);
        edtDebitCardNumber = (EditText) getView().findViewById(R.id.edtDebitCardNumber);
        edtExpiryMonth = (EditText) getView().findViewById(R.id.edtExpiryMonth);
        edtExpiryYear = (EditText) getView().findViewById(R.id.edtExpiryYear);
        edtDebitCvv = (EditText) getView().findViewById(R.id.edtDebitCvv);

        chkMembers = (CheckBox) getView().findViewById(R.id.chkMembers);
        chkVendors = (CheckBox) getView().findViewById(R.id.chkVendors);
        relativeVendors = (RelativeLayout) getView().findViewById(
                R.id.relativeVendors);
        relativeMembers = (RelativeLayout) getView().findViewById(
                R.id.relativeMembers);
        relativeVendors.setOnClickListener(this);
        relativeMembers.setOnClickListener(this);
        btnSkip.setOnClickListener(this);

        if (!TextUtils.isEmpty(Utility.USER_EMAIL)) {
            edtEmail.setText(Utility.USER_EMAIL + "");
            edtEmail.setEnabled(false);
            edtPassword.setVisibility(View.GONE);
            edtConfirmPassword.setVisibility(View.GONE);
        }

        edtEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !Utility.isValidEmail(edtEmail.getText().toString())) {
                    Toast.makeText(getContext(), "Please Enter Valid Email Id", Toast.LENGTH_SHORT).show();
                    edtEmail.setError("Enter Valid EmailId");
                    edtEmail.post(new Runnable() {
                        @Override
                        public void run() {
                            edtEmail.requestFocus();
                        }
                    });
                }
            }
        });

    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnContinue:
                if (linearField.getVisibility() == View.VISIBLE) {
                    if (!checkValidation())
                        return;
                    showHideLayout(true);
                } else {
                    if (!checkValidation())
                        return;
                    // call WS
                    if (chkVendors.isChecked()) {
                        debitcard_ll.setVisibility(View.VISIBLE);
                        linearMemberVendor.setVisibility(View.GONE);

                        if (debitcard_ll.getVisibility() == View.VISIBLE) {
                            if (checkVendroValidation()) {
                                AppDelegate.storeValue(PreferenceData.ISVENDOR, "1");
                                callVendorSignup();
                            }
                        }
                    } else {
                        callSignup();
                    }
                }
                break;

            case R.id.btnSkip:
//                Toast.makeText(getContext(), "Click", Toast.LENGTH_SHORT).show();
                AppDelegate.storeValue(PreferenceData.ISVENDOR, "1");
                callVendorSignup();
//                if (!checkValidation()) callSignup();

                break;

            case R.id.relativeMembers:
                chkMembers.setChecked(true);
                chkVendors.setChecked(false);
                break;
            case R.id.relativeVendors:
                chkMembers.setChecked(false);
                chkVendors.setChecked(true);
                break;
        }
    }

    private void callVendorSignup() {

        String email = edtEmail.getText().toString().trim();
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString();
        String phone = edtPhone.getText().toString();
        String cardHoldername = edtCardHolderName.getText().toString().trim();
        String cardNumber = edtDebitCardNumber.getText().toString().trim();
        String expMonth = edtExpiryMonth.getText().toString();
        String expYear = edtExpiryYear.getText().toString();
        String cvvNumber = edtDebitCvv.getText().toString();

        NetworkTask networkTask = new NetworkTask(getActivity());
        networkTask.setmCallback(asyncTaskCompleteListener);
        networkTask.setHashMapParams(new NetworkParam().getVendorSignupParam(email, username, password, phone, cardNumber, expMonth, expYear, cvvNumber));
        networkTask.execute(NetworkParam.METHOD_SIGNUP,
                String.valueOf(REQ_SIGNUP));
        ProgressDialogUtility.show(getActivity(), null, false);

/*
        if (!TextUtils.isEmpty(cardHoldername)) {
            if (!TextUtils.isEmpty(cardNumber)) {

            }
        }else{
            edtCardHolderName.setEr
        }*/
    }

    private boolean checkValidation() {
        if (edtEmail.getText().toString().trim().length() == 0) {
            MainFragmentActivity.toast(getActivity(), "Please enter email");
            return false;
        } else if (Utility.isValidEmail(edtEmail.getText().toString().trim())) {
            MainFragmentActivity.toast(getActivity(), "Please enter Valid Email");
            return false;
        } else if (edtUsername.getText().toString().trim().length() == 0) {
            MainFragmentActivity.toast(getActivity(), "Please enter username");
            return false;
        } else if (edtPassword.getVisibility() == View.VISIBLE) {
            if (edtPassword.getText().toString().length() == 0 && edtPassword.getVisibility() == View.VISIBLE) {
                MainFragmentActivity.toast(getActivity(), "Please enter password");
                return false;
            } else if (edtConfirmPassword.getText().toString().length() == 0) {
                MainFragmentActivity.toast(getActivity(), "Please enter confirm password");
                return false;
            } else if (!edtPassword.getText().toString()
                    .equals(edtConfirmPassword.getText().toString())) {
                MainFragmentActivity.toast(getActivity(), "Password did not matched!");
                return false;
            }
        } else {
            return true;
        }
        return true;
    }

    private boolean checkVendroValidation() {
        if (edtEmail.getText().toString().trim().length() == 0) {
            MainFragmentActivity.toast(getActivity(), "Please enter email");
            return false;
        } else if (edtUsername.getText().toString().trim().length() == 0) {
            MainFragmentActivity.toast(getActivity(), "Please enter username");
            return false;
        } else if (edtPassword.getVisibility() == View.VISIBLE) {
            if (edtPassword.getText().toString().length() == 0) {
                MainFragmentActivity.toast(getActivity(), "Please enter password");
                return false;
            } else if (edtConfirmPassword.getText().toString().length() == 0) {
                MainFragmentActivity.toast(getActivity(), "Please enter confirm password");
                return false;
            } else if (edtCardHolderName.getText().toString().trim().length() == 0) {
//            MainFragmentActivity.toast(getActivity(), "CardHolderName did not Empty!");
                return false;
            } else {
                return true;
            }
        } else if (edtDebitCardNumber.getText().toString().trim().length() == 0) {
            MainFragmentActivity.toast(getActivity(), "DebitCardNumber did not Empty!");
            return false;
        } else if (edtExpiryYear.getText().toString().trim().length() == 0) {
            MainFragmentActivity.toast(getActivity(), "ExpiryYear did not Empty!");
            return false;
        } else if (edtExpiryMonth.getText().toString().trim().length() == 0) {
            MainFragmentActivity.toast(getActivity(), "ExpiryMonth did not Empty!");
            return false;
        } else if (edtDebitCvv.getText().toString().trim().length() == 0) {
            MainFragmentActivity.toast(getActivity(), "Cvv did not Empty!");
            return false;
        }

        return true;
    }

    private void showHideLayout(boolean isChoiceVisible) {
        if (isChoiceVisible) {
            linearMemberVendor.setVisibility(View.VISIBLE);
            linearField.setVisibility(View.GONE);
        } else {
            linearMemberVendor.setVisibility(View.GONE);
            linearField.setVisibility(View.VISIBLE);
        }
    }

    public boolean checkBack() {
        if (linearField.getVisibility() == View.VISIBLE) {
            return true;
        } else {
            // make visible
            showHideLayout(false);
        }
        return false;
    }

    private void callSignup() {
        String email = edtEmail.getText().toString().trim();
        String username = edtUsername.getText().toString().trim();
        String password = null;
        if (!TextUtils.isEmpty(edtPassword.getText().toString())) {
            password = edtPassword.getText().toString();
        }

        String phone = edtPhone.getText().toString();

        NetworkTask networkTask = new NetworkTask(getActivity());
        networkTask.setmCallback(asyncTaskCompleteListener);
        networkTask.setHashMapParams(new NetworkParam().getSignupParam(email,
                username, password, phone, isVendor));
        networkTask.execute(NetworkParam.METHOD_SIGNUP,
                String.valueOf(REQ_SIGNUP));
        ProgressDialogUtility.show(getActivity(), null, false);
    }

    private AsyncTaskCompleteListener asyncTaskCompleteListener = new AsyncTaskCompleteListener() {
        @Override
        public void onTaskComplete(ClsNetworkResponse clsResponse) {
            // TODO Auto-generated method stub
            if (clsResponse.getRequestCode() == REQ_SIGNUP) {
                ProgressDialogUtility.dismiss();
                if (clsResponse.isSuccess() && clsResponse.getObject() != null) {
                    MainFragmentActivity.toast(getActivity(),
                            clsResponse.getDispMessage());

                    ClsSignup clsLogin = (ClsSignup) clsResponse.getObject();
                    storeSignupData(clsLogin);
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
            if (clsResponse.getRequestCode() == REQ_SIGNUP) {
                clsResponse = new ParserSignup(clsResponse).parse();
            }
            return clsResponse;
        }
    };

    private void startMainActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void storeSignupData(ClsSignup clsLogin) {
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
    }
}
