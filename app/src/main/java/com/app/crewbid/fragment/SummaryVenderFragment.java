package com.app.crewbid.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.app.crewbid.MainFragmentActivity;
import com.app.crewbid.R;
import com.app.crewbid.adapter.EventSummeryVenderListAdapter;
import com.app.crewbid.interfaces.KeyInterface;
import com.app.crewbid.jsonparser.ParserMyCrewEventsSummery;
import com.app.crewbid.model.ClsEventSummeryList;
import com.app.crewbid.network.AsyncTaskCompleteListener;
import com.app.crewbid.network.ClsNetworkResponse;
import com.app.crewbid.network.NetworkParam;
import com.app.crewbid.network.NetworkTask;
import com.app.crewbid.utility.ProgressDialogUtility;
import com.app.crewbid.utility.Utility;

import java.util.ArrayList;

public class SummaryVenderFragment extends Fragment implements AdapterView.OnItemClickListener,
        View.OnClickListener, KeyInterface, AbsListView.OnScrollListener {

    private static final int REQ_MY_CREW_EVENT_SUMMERY = 1000;
    private ArrayList<ClsEventSummeryList> productLists = new ArrayList<ClsEventSummeryList>();

    private EventSummeryVenderListAdapter adapter;
    // onScroll
    private int offsetNo = 1;
    int visibleChildCount = 0;
    boolean isRefreshing = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callMyCrewBidEventsSummeryWs();
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
        lstProductList = (ListView) v.findViewById(R.id.lstActivityList);
        adapter = new EventSummeryVenderListAdapter(getActivity(), this, getProductLists());
        footerView = ((Activity) getActivity()).getLayoutInflater().inflate(
                R.layout.list_load_more_footer, null);
        linearFooter = (LinearLayout) footerView
                .findViewById(R.id.linearFooter);
        lstProductList.setAdapter(adapter);
        lstProductList.setOnItemClickListener(this);
        lstProductList.setOnScrollListener(this);

        visibleChildCount = (lstProductList.getLastVisiblePosition() - lstProductList
                .getFirstVisiblePosition()) + 1;
    }

    public ArrayList<ClsEventSummeryList> getProductLists() {
        return productLists;
    }

    public void setProductLists(ArrayList<ClsEventSummeryList> productLists) {
        this.productLists = productLists;
    }

    private void callMyCrewBidEventsSummeryWs() {
//		isRefreshing = true;
        String userId = Utility.getUserIdSummery();
//		userId = "247";
        NetworkTask networkTask = new NetworkTask(getActivity());
        networkTask.setmCallback(asyncTaskCompleteListener);
        String pid = Utility.getProductIdSummery();
        Log.d("pid", "pid:- " + pid);
        networkTask.setHashMapParams(new NetworkParam().getMyCrewSummeryParam(Integer.parseInt(pid), userId));
        networkTask.execute(NetworkParam.METHOD_MY_CREW_EVENTS_SUMMERY,
                String.valueOf(REQ_MY_CREW_EVENT_SUMMERY));
        if (offsetNo == 1)
            ProgressDialogUtility.show(getActivity(), null, false);
//		else
//			setFootviewVisibility(true);
    }

    AsyncTaskCompleteListener asyncTaskCompleteListener = new AsyncTaskCompleteListener() {
        @Override
        public void onTaskComplete(ClsNetworkResponse clsResponse) {
//			setFootviewVisibility(false);
            // TODO Auto-generated method stub
            if (clsResponse.getRequestCode() == REQ_MY_CREW_EVENT_SUMMERY) {
                ProgressDialogUtility.dismiss();
                if (clsResponse.isSuccess() && clsResponse.getObject() != null) {
                    ArrayList<ClsEventSummeryList> productList = (ArrayList<ClsEventSummeryList>) clsResponse
                            .getObject();
                    Log.d("ss", "onTaskComplete" + productList.get(0).getProjectTitle());
                    getProductLists().addAll(productList);
                    adapter.setArrayList(getProductLists());
                    adapter.notifyDataSetChanged();

//					isRefreshing = false;
//					offsetNo++;

                } else {
//					offsetNo = -1;
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
            if (clsResponse.getRequestCode() == REQ_MY_CREW_EVENT_SUMMERY) {

                Log.d("ss", "doBackGround ParserMyCrewEventsSummery");
                clsResponse = new ParserMyCrewEventsSummery(clsResponse).parse();
            }
            return clsResponse;
        }
    };

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
            if ((getProductLists().size() - 1) > visibleChildCount) {
                if (!isRefreshing && offsetNo > 0) {
                    callMyCrewBidEventsSummeryWs();
                }
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView arg0, int arg1) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub

//		Intent i;
//		String name = adapter.getItem(position).toString();
//		Log.d("id:- ", name);
//		Log.d("position:- ", position + "");
////		Log.d("position:- ", productLists.get(position).getProductId());
//		Log.d("getProductTitle:- ", productLists.get(position).getProductTitle());
//		switchContent();
    }

}
