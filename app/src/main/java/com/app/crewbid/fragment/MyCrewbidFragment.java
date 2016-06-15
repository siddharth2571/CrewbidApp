package com.app.crewbid.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.crewbid.AppDelegate;
import com.app.crewbid.MainFragmentActivity;
import com.app.crewbid.R;
import com.app.crewbid.adapter.ProductListAdapter;
import com.app.crewbid.interfaces.KeyInterface;
import com.app.crewbid.jsonparser.ParserMyCrewEvents;
import com.app.crewbid.model.ClsProductList;
import com.app.crewbid.network.AsyncTaskCompleteListener;
import com.app.crewbid.network.ClsNetworkResponse;
import com.app.crewbid.network.NetworkParam;
import com.app.crewbid.network.NetworkTask;
import com.app.crewbid.prefs.PreferenceData;
import com.app.crewbid.utility.ProgressDialogUtility;
import com.app.crewbid.utility.Utility;

import java.util.ArrayList;

public class MyCrewbidFragment extends Fragment implements AdapterView.OnItemClickListener,
        View.OnClickListener, KeyInterface, AbsListView.OnScrollListener {

    private static final int REQ_MY_CREW_EVENT = 1000;
    private static final int REQ_ALL_CREW_EVENTS = 2000;
    private ArrayList<ClsProductList> productLists = new ArrayList<ClsProductList>();
    private ProductListAdapter adapter;
    // onScroll
    private int offsetNo = 1;
    int visibleChildCount = 0;
    boolean isRefreshing = false;
    TextView nobidfoundtxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callMyCrewBidEventsWs();
//		callAllCrewEventsWs();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_activity, container,
                false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init(getView());
    }

    private ListView lstProductList = null;
    private View footerView = null;
    private LinearLayout linearFooter = null;

    private void init(View v) {
        ArrayList<ClsProductList> listAdapter = productLists;

        lstProductList = (ListView) v.findViewById(R.id.lstActivityList);
        nobidfoundtxt = (TextView) v.findViewById(R.id.emptyElement);

        footerView = ((Activity) getActivity()).getLayoutInflater().inflate(
                R.layout.list_load_more_footer, null);
        linearFooter = (LinearLayout) footerView
                .findViewById(R.id.linearFooter);
//        lstProductList.setAdapter(adapter);
        lstProductList.setOnItemClickListener(this);
        lstProductList.setOnScrollListener(this);

        visibleChildCount = (lstProductList.getLastVisiblePosition() - lstProductList
                .getFirstVisiblePosition()) + 1;
    }

    private void callMyCrewBidEventsWs() {
        isRefreshing = true;
        String userId = AppDelegate.getString(PreferenceData.USERID);
//        userId = "247";
        NetworkTask networkTask = new NetworkTask(getActivity());
        networkTask.setmCallback(asyncTaskCompleteListener);
        networkTask.setHashMapParams(new NetworkParam().getMyCrewEventsParam(
                userId, "all", offsetNo));
        networkTask.execute(NetworkParam.METHOD_MY_CREW_EVENTS,
                String.valueOf(REQ_MY_CREW_EVENT));
        if (offsetNo == 1)
            ProgressDialogUtility.show(getActivity(), null, false);
        else
            setFootviewVisibility(true);
    }

    // private void setFootviewVisibility(boolean isVisible) {
    // if (isVisible)
    // lstProductList.addFooterView(footerView);
    // else
    // lstProductList.removeFooterView(footerView);
    // }

    private void setFootviewVisibility(boolean isVisible) {

        if (linearFooter != null && isVisible) {
            if (lstProductList.getFooterViewsCount() <= 0)
                lstProductList.addFooterView(footerView);
            linearFooter.setVisibility(View.VISIBLE);
        } else if (linearFooter != null && !isVisible) {
            linearFooter.setVisibility(View.GONE);
            if (offsetNo < 0) {
                try {
                    lstProductList.removeFooterView(linearFooter);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    AsyncTaskCompleteListener asyncTaskCompleteListener = new AsyncTaskCompleteListener() {
        @Override
        public void onTaskComplete(ClsNetworkResponse clsResponse) {
            setFootviewVisibility(false);
            // TODO Auto-generated method stub
            if (clsResponse.getRequestCode() == REQ_MY_CREW_EVENT) {
                ProgressDialogUtility.dismiss();
                if (clsResponse.isSuccess() && clsResponse.getObject() != null) {
                    ArrayList<ClsProductList> productList = (ArrayList<ClsProductList>) clsResponse
                            .getObject();
                    productLists.addAll(productList);

                    setAdapter(productLists);

//                    adapter.setArrayList(productLists);
//                    adapter.notifyDataSetChanged();
                    isRefreshing = false;
                    offsetNo++;

                } else {
                    offsetNo = -1;
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
            if (clsResponse.getRequestCode() == REQ_MY_CREW_EVENT) {
                clsResponse = new ParserMyCrewEvents(clsResponse).parse();
            }
            return clsResponse;
        }
    };

    public void setAdapter(ArrayList<ClsProductList> productListsdetail) {
        if (!productListsdetail.isEmpty()) {
            // sometime it layout inflater null object refernce
            try {
                adapter = new ProductListAdapter(getActivity(), this, productListsdetail);
                lstProductList.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            lstProductList.setEmptyView(nobidfoundtxt);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // what is the bottom iten that is visible
        int lastInScreen = firstVisibleItem + visibleItemCount;
        if ((lastInScreen == totalItemCount)) {
            if ((productLists.size() - 1) > visibleChildCount) {
                if (!isRefreshing && offsetNo > 0) {
                    callMyCrewBidEventsWs();
                }
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView arg0, int arg1) {

    }

   /* public ArrayList<ClsProductList> getProductLists() {
        return productLists;
    }

    public void setProductLists(ArrayList<ClsProductList> productLists) {
        this.productLists = productLists;
    }*/

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub

        Intent i;
        String name = adapter.getItem(position).toString();
        Log.d("id:- ", name);
        Log.d("position:- ", position + "");
//		Log.d("position:- ", productLists.get(position).getProductId());
        Log.d("getProductTitle:- ", productLists.get(position).getProductTitle());
        Utility.CrewFunded = productLists.get(position).getCrewsFunded();
        Log.d("result", productLists.get(position).getCrewsFunded());

        switchContent(productLists.get(position).getProductId(), productLists.get(position).getProductUserId(), productLists.get(position).getStatusIsAward());
        Log.d("getProductId:- ", productLists.get(position).getProductId());
    }

    public void switchContent(String pId, String userId, String status) {

        Bundle bundle = new Bundle();
        Utility.setProductIdSummery(pId);
        Utility.setUserIdSummery(userId);
        Utility.setBidIsAwarded(status);
//        bundle.putString("ProductId", pId);
        //set Fragmentclass Arguments
//        Fragmentclass fragobj=new Fragmentclass();

        Fragment fragment = new EventFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }
}

