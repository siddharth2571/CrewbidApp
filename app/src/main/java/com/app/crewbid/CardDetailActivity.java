package com.app.crewbid;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.app.crewbid.network.AsyncTaskCompleteListener;
import com.app.crewbid.network.ClsNetworkResponse;
import com.app.crewbid.network.NetworkParam;
import com.app.crewbid.network.NetworkTask;
import com.app.crewbid.prefs.PreferenceData;
import com.app.crewbid.utility.ProgressDialogUtility;
import com.app.crewbid.utility.Utility;

public class CardDetailActivity extends AppCompatActivity {

    public EditText holdernameEdt, cardnumberEdt, acexmonthedt, acexyearedt, accvcedt;
    private static final int REQ_LOGIN = 1;

    String bidvalue, cid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);
        bidvalue = getIntent().getStringExtra("bidvalue");
        cid = getIntent().getStringExtra("cid");
        holdernameEdt = (EditText) findViewById(R.id.edtCardHolderName);
        cardnumberEdt = (EditText) findViewById(R.id.edtDebitCardNumber);
        acexmonthedt = (EditText) findViewById(R.id.edtExpiryMonth);
        acexyearedt = (EditText) findViewById(R.id.edtExpiryYear);
        accvcedt = (EditText) findViewById(R.id.edtDebitCvv);

        findViewById(R.id.btnContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCardDetail();
            }
        });
    }

    public void callCardDetail() {
        String userId = AppDelegate.getString(PreferenceData.USERID);
        String cardname = holdernameEdt.getText().toString();
        String cardnumber = cardnumberEdt.getText().toString();
        String acExMonth = acexmonthedt.getText().toString();
        String acExYear = acexyearedt.getText().toString();
        String accvc = accvcedt.getText().toString();

        if (TextUtils.isEmpty(holdernameEdt.getText().toString())) {
            holdernameEdt.setError("Enter HolderName");
        } else {
            if (TextUtils.isEmpty(cardnumberEdt.getText().toString())) {
                cardnumberEdt.setError("Enter CardNumber");
            } else {
                if (TextUtils.isEmpty(acexmonthedt.getText().toString())) {
                    acexmonthedt.setError("Enter Month");
                } else {
                    if (TextUtils.isEmpty(acexyearedt.getText().toString())) {
                        acexyearedt.setError("Enter year last 2 digit");
                    } else {
                        if (TextUtils.isEmpty(accvcedt.getText().toString())) {
                            accvcedt.setError("Enter CVV");
                        } else {

                            NetworkTask networkTask = new NetworkTask(getApplicationContext());
                            networkTask.setmCallback(asyncTaskCompleteListener);
                            networkTask.setHashMapParams(new NetworkParam().setUpdateCardDetail(userId, cardname, cardnumber, acExMonth, acExYear, accvc));
                            networkTask.execute(NetworkParam.METHOD_SET_CARD_DETAIL_UPDATE,
                                    String.valueOf(REQ_LOGIN));
                            ProgressDialogUtility.show(this, null, false);

                        }
                    }
                }
            }
        }


    }

    AsyncTaskCompleteListener asyncTaskCompleteListener = new AsyncTaskCompleteListener() {
        @Override
        public void onTaskComplete(ClsNetworkResponse clsResponse) {
            // TODO Auto-generated method stub
            if (clsResponse.getRequestCode() == REQ_LOGIN) {
                ProgressDialogUtility.dismiss();
                if (clsResponse.isSuccess() && clsResponse.getObject() != null) {

                    Log.e("response", "=>" + clsResponse.getObject());

                    AppDelegate.storeValue(PreferenceData.HASACCOUNT, "1");
//                    placeBidES(bidvalue, cid);
                    Log.e("placecall", "=>" + clsResponse.getObject());
                    finish();

                } else {
//                    MainFragmentActivity.toast(CardDetailActivity.this, "Once Again Press Bid");
                    AppDelegate.storeValue(PreferenceData.HASACCOUNT, "1");
                    finish();
                }
            }
        }

        @Override
        public ClsNetworkResponse doBackGround(ClsNetworkResponse clsResponse) {
            // TODO Auto-generated method stub
            if (!clsResponse.isSuccess()
                    || clsResponse.getResult_String() == null)
                return clsResponse;

            return clsResponse;
        }
    };

    private static final int REQ_PLACE_BID = 3000;


    public void placeBidES(String value, String cid) {


//		isRefreshing = true;
//        String userId =Utility.getUserIdSummery();
        String userId = AppDelegate.getString(PreferenceData.USERID);
        // from logine
//		userId = "247";
        NetworkTask networkTask = new NetworkTask(this);
        networkTask.setmCallback(asyncTaskCompleteListener1);
        String pid = Utility.getProductIdSummery();
        Log.d("pid", "pid:- " + pid);
        networkTask.setHashMapParams(new NetworkParam().getAddBidtoEventParam(Integer.parseInt(pid), userId, cid, value, "1", "5", "proposal123"));
        networkTask.execute(NetworkParam.METHOD_ADD_BID_TO_EVENT,
                String.valueOf(REQ_PLACE_BID));

//        ProgressDialogUtility.show(((Activity) getApplicationContext()), null, false);

    }

    AsyncTaskCompleteListener asyncTaskCompleteListener1 = new AsyncTaskCompleteListener() {
        @Override
        public void onTaskComplete(ClsNetworkResponse clsResponse) {
//			setFootviewVisibility(false);
            // TODO Auto-generated method stub
            if (clsResponse.getRequestCode() == REQ_PLACE_BID) {
                ProgressDialogUtility.dismiss();
                Log.d("bidplaced", clsResponse.getObject() + "");
                if (clsResponse.isSuccess()) {
                    Log.d("bidplaced", "bid placed");
                    MainFragmentActivity.toast((Activity) getApplicationContext(),
                            "bid placed");


                } else {
//					offsetNo = -1;
                    MainFragmentActivity.toast((Activity) getApplicationContext(),
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

            return clsResponse;
        }
    };


}
