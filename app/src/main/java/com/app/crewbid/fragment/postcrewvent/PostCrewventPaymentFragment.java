package com.app.crewbid.fragment.postcrewvent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.crewbid.MainFragmentActivity;
import com.app.crewbid.R;
import com.app.crewbid.fragment.HomeFragment;
import com.app.crewbid.interfaces.KeyInterface;
import com.app.crewbid.jsonparser.ParserPostNewEvent;
import com.app.crewbid.model.ClsPostNewEventRes;
import com.app.crewbid.network.AsyncTaskCompleteListener;
import com.app.crewbid.network.ClsNetworkResponse;
import com.app.crewbid.network.NetworkParam;
import com.app.crewbid.network.NetworkTask;
import com.app.crewbid.utility.ProgressDialogUtility;
import com.app.crewbid.utility.Utility;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

public class PostCrewventPaymentFragment extends Fragment implements
        OnClickListener, KeyInterface {

    private static final int REQ_POST_NEW_EVENT = 1;

    EditText edtCardnumber, edtExpMonth, edtExpYear, edtPaymentCVC, edtPaymentAmount;
    TextView paywithcard;

    private Button btnPost;
    private TextView txtMakeDepositLive;
    private AsyncTask<Void, Void, ClsNetworkResponse> uploadTask;

//    public static final String PUBLISHABLE_KEY = "pk_test_6pRNASCoBOKtIshFeQd4XMUh";

    public static final String PUBLISHABLE_KEY = "pk_test_rLeaHVxl0DRzePeKhwQNL8UQ";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View root = inflater.inflate(R.layout.frag_post_crewvent_payment,
                container, false);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        ((MainFragmentActivity) getContext()).setHeader(getActivity()
                .getString(R.string.post_a_crewvent));
    }

    private void init(View v) {
        btnPost = (Button) v.findViewById(R.id.btnPost);
        btnPost.setOnClickListener(this);
        txtMakeDepositLive = (TextView) v.findViewById(R.id.txtMakeDepositLive);
        edtCardnumber = (EditText) v.findViewById(R.id.edtCardnumber);
        edtExpMonth = (EditText) v.findViewById(R.id.edtExpMonth);
        edtExpYear = (EditText) v.findViewById(R.id.edtExpYear);
        edtPaymentCVC = (EditText) v.findViewById(R.id.edtPaymentCVC);
        edtPaymentAmount = (EditText) v.findViewById(R.id.edtPaymentAmount);
        paywithcard = (TextView) getView().findViewById(R.id.paywithcard);

        paywithcard.setOnClickListener(this);


        if (IS_DEBUG) {
            edtCardnumber.setText("4242424242424242");
            edtExpMonth.setText("08");
            edtExpYear.setText("20");
            edtPaymentCVC.setText("456");
            edtPaymentAmount.setText("26");
        }

        String data = "To make <b>"
                + Utility.filter(PostCrewventFormFragment.clsAddNewEvent
                .getEventName()) + "</b> Live, please deposit $"
                + PostCrewventFormFragment.clsAddNewEvent.getPaypalMinimum()
                + " or more";
        txtMakeDepositLive.setText(Html.fromHtml(data));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnPost:
//                callPost();
                saveCreditCard();
                break;

            case R.id.paywithcard:
//                callPost(Token, Amount);
                break;
        }
    }

    String Amount;
    String Token;


    public void saveCreditCard() {

//        Card card = new Card(
//                form.getCardNumber(),
//                form.getExpMonth(),
//                form.getExpYear(),
//                form.getCvc());
        Card card = null;
        boolean validation = false;
        if (!TextUtils.isEmpty(edtCardnumber.getText().toString()) && !TextUtils.isEmpty(edtExpMonth.getText().toString()) && !TextUtils.isEmpty(edtExpYear.getText().toString()) && !TextUtils.isEmpty(edtExpYear.getText().toString())) {
            card = new Card(edtCardnumber.getText().toString(), Integer.parseInt(edtExpMonth.getText().toString()), Integer.parseInt(edtExpYear.getText().toString()), edtPaymentCVC.getText().toString());
            validation = card.validateCard();
            setPaymentGetToken(card, validation);

        } else {
            Toast.makeText(getActivity(), "Please Check Info", Toast.LENGTH_SHORT).show();
        }
        Amount = edtPaymentAmount.getText().toString();
//        card.setCurrency(form.getCurrency());
    }

    public void setPaymentGetToken(Card card, boolean validation) {
        Log.i("Validation", "" + validation);
        if (validation) {
            ProgressDialogUtility.show(getActivity(), null, false);
            new Stripe().createToken(
                    card,
                    PUBLISHABLE_KEY,
                    new TokenCallback() {
                        public void onSuccess(Token token) {
                            Log.i("Token", "" + token.getId());
                            Toast.makeText(getActivity(), token.getId(), Toast.LENGTH_SHORT);
                            ProgressDialogUtility.dismiss();
                            Token = token.getId();

                            callPost();

//                            paywithcard.performClick();

//                            if (!TextUtils.isEmpty(Token)) {
//                                edtPaymentAmount.setVisibility(View.VISIBLE);
//                            }

                            // new AsyncRequestCall().execute(token.getId(), edtPaymentAmount.getText().toString());
//                            if (TextUtils.isEmpty(edtPaymentAmount.getText().toString())) {
//                                callPost(token.getId(), edtPaymentAmount.getText().toString());

//                            }

//                            getTokenList().addToList(token);
//                            finishProgress();
                        }

                        public void onError(Exception error) {
                            Log.i("Token", "" + error);
//                            handleError(error.getLocalizedMessage());
//                            finishProgress();
                        }
                    });
        } else if (!card.validateNumber()) {
            Toast.makeText(getActivity(), "The card number that you entered is invalid", Toast.LENGTH_SHORT);
        } else if (!card.validateExpiryDate()) {
            Toast.makeText(getActivity(), "The expiration date that you entered is invalid", Toast.LENGTH_SHORT);
        } else if (!card.validateCVC()) {
            Toast.makeText(getActivity(), "The CVC code that you entered is invalid", Toast.LENGTH_SHORT);
        } else {
            Toast.makeText(getActivity(), "The card details that you entered are invalid", Toast.LENGTH_SHORT);
        }
    }

    /*public void setAsyncIntegration() {
        new DownloadWebPageTask().execute(Token, Amount);
    }

    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {

        String url = "http://www.crewbid.com/api/services.php?function=payment_request";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialogUtility.show(getActivity(), null, false);
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";

            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            try {
                HttpResponse execute = client.execute(httpPost);
                InputStream content = execute.getEntity().getContent();
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
                nameValuePair.add(new BasicNameValuePair("token", urls[0]));
                nameValuePair.add(new BasicNameValuePair("amount", urls[1]));

                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getActivity(), "" + result, Toast.LENGTH_SHORT).show();
            ProgressDialogUtility.dismiss();

        }
    }

*/

    private void callPost() {
        NetworkTask networkTask = new NetworkTask(getActivity());
        networkTask.setMultipart(true);
        networkTask.setmCallback(asyncTaskCompleteListener1);
        networkTask.setHashMapParams(new NetworkParam()
                .getPostNewEventParam(PostCrewventFormFragment.clsAddNewEvent, Token, Amount));
        networkTask.execute(NetworkParam.METHOD_ADD_NEW_EVENT,
                String.valueOf(REQ_POST_NEW_EVENT));
        ProgressDialogUtility.show(getActivity(), null, false);
    }

    private AsyncTaskCompleteListener asyncTaskCompleteListener1 = new AsyncTaskCompleteListener() {
        @Override
        public void onTaskComplete(ClsNetworkResponse clsResponse) {
            // TODO Auto-generated method stub
            if (clsResponse.getRequestCode() == REQ_POST_NEW_EVENT) {
                ProgressDialogUtility.dismiss();
                if (clsResponse.isSuccess() && clsResponse.getObject() != null) {
                    MainFragmentActivity.toast(getActivity(),
                            clsResponse.getDispMessage());

                    Log.i("Result", "" + clsResponse.getResult_String() + "---" + clsResponse.getDispMessage());

                    gotoHomeFragment();

//                    saveCreditCard();

                    // clsAddNewEvent = addNewEvent;
                    // setData();
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
            if (clsResponse.getRequestCode() == REQ_POST_NEW_EVENT) {
                clsResponse = new ParserPostNewEvent(clsResponse).parse();
            }
            return clsResponse;
        }
    };


    private void callPost(String accesstoken, String amount) {
        NetworkTask networkTask = new NetworkTask(getActivity());
        networkTask.setMultipart(true);
        networkTask.setmCallback(asyncTaskCompleteListener);
        networkTask.setHashMapParams(new NetworkParam().getPayment(accesstoken, amount));
        networkTask.execute(NetworkParam.METHOD_PAYMENT_REQ,
                String.valueOf(REQ_POST_NEW_EVENT));
        ProgressDialogUtility.show(getActivity(), null, false);
    }

    private AsyncTaskCompleteListener asyncTaskCompleteListener = new AsyncTaskCompleteListener() {
        @Override
        public void onTaskComplete(ClsNetworkResponse clsResponse) {
            // TODO Auto-generated method stub
            if (clsResponse.getRequestCode() == REQ_POST_NEW_EVENT) {
                ProgressDialogUtility.dismiss();
                if (clsResponse.isSuccess() && clsResponse.getObject() != null) {
                    MainFragmentActivity.toast(getActivity(),
                            clsResponse.getDispMessage());

                    ClsPostNewEventRes postEventResponse = (ClsPostNewEventRes) clsResponse
                            .getObject();

                    gotoHomeFragment();
                    // clsAddNewEvent = addNewEvent;
                    // setData();


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
            if (clsResponse.getRequestCode() == REQ_POST_NEW_EVENT) {
                clsResponse = new ParserPostNewEvent(clsResponse).parse();
            }
            return clsResponse;
        }
    };

    private void gotoHomeFragment() {
        HomeFragment homeFragment = new HomeFragment();
        ((MainFragmentActivity) getContext()).switchContent(
                homeFragment, false, FRAG_HOME);
    }
}
