package com.app.crewbid.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.app.crewbid.AppDelegate;
import com.app.crewbid.MainFragmentActivity;
import com.app.crewbid.R;
import com.app.crewbid.adapter.TopTenListAdapter;
import com.app.crewbid.interfaces.KeyInterface;
import com.app.crewbid.jsonparser.ParserTopTenLists;
import com.app.crewbid.model.ClsTopTen;
import com.app.crewbid.network.AsyncTaskCompleteListener;
import com.app.crewbid.network.ClsNetworkResponse;
import com.app.crewbid.network.NetworkParam;
import com.app.crewbid.network.NetworkTask;
import com.app.crewbid.prefs.PreferenceData;
import com.app.crewbid.utility.ProgressDialogUtility;

/**
 * Created by pcs145 on 12/15/2015.
 */
public class TopTenFragment extends Fragment implements OnItemClickListener,
		OnClickListener, KeyInterface, OnScrollListener {

	private static final int REQ_TOP_TEN = 1000;
	private ArrayList<ClsTopTen> topTenLists = new ArrayList<ClsTopTen>();
	private TopTenListAdapter adapter;
	// onScroll
	private int offsetNo = 1;
	int visibleChildCount = 0;
	boolean isRefreshing = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// callTopTenWs();
		ClsTopTen clsTopTen = new ClsTopTen();
		clsTopTen.setProductTitle("Crew");
		getTopTenLists().add(clsTopTen);
		getTopTenLists().add(clsTopTen);
		getTopTenLists().add(clsTopTen);
		getTopTenLists().add(clsTopTen);
		getTopTenLists().add(clsTopTen);
		getTopTenLists().add(clsTopTen);
		getTopTenLists().add(clsTopTen);
		getTopTenLists().add(clsTopTen);
		getTopTenLists().add(clsTopTen);
		getTopTenLists().add(clsTopTen);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.frag_top_ten, container,
				false);

		return rootView;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		((MainFragmentActivity) getContext()).setHeader("Top 10");
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
		lstProductList = (ListView) v.findViewById(R.id.lstTopTenList);
		adapter = new TopTenListAdapter(getActivity(), this, getTopTenLists());
		footerView = ((Activity) getActivity()).getLayoutInflater().inflate(
				R.layout.list_load_more_footer, null);
		linearFooter = (LinearLayout) footerView
				.findViewById(R.id.linearFooter);
		lstProductList.setAdapter(adapter);
		lstProductList.setOnItemClickListener(this);
		// lstProductList.setOnScrollListener(this);

		visibleChildCount = (lstProductList.getLastVisiblePosition() - lstProductList
				.getFirstVisiblePosition()) + 1;
	}

	private void callTopTenWs() {
		isRefreshing = true;
		String userId = AppDelegate.getString(PreferenceData.USERID);
		userId = "247";
		NetworkTask networkTask = new NetworkTask(getActivity());
		networkTask.setmCallback(asyncTaskCompleteListener);
		networkTask.setHashMapParams(new NetworkParam().getMyCrewEventsParam(
				userId, "all", offsetNo));
		networkTask.execute(NetworkParam.METHOD_MY_CREW_EVENTS,
				String.valueOf(REQ_TOP_TEN));
		if (offsetNo == 1)
			ProgressDialogUtility.show(getActivity(), null, false);
		else
			setFootviewVisibility(true);
	}

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
			if (clsResponse.getRequestCode() == REQ_TOP_TEN) {
				ProgressDialogUtility.dismiss();
				if (clsResponse.isSuccess() && clsResponse.getObject() != null) {
					ArrayList<ClsTopTen> productList = (ArrayList<ClsTopTen>) clsResponse
							.getObject();
					getTopTenLists().addAll(productList);
					adapter.setArrayList(getTopTenLists());
					adapter.notifyDataSetChanged();
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
			if (clsResponse.getRequestCode() == REQ_TOP_TEN) {
				clsResponse = new ParserTopTenLists(clsResponse).parse();
			}
			return clsResponse;
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// what is the bottom iten that is visible
		int lastInScreen = firstVisibleItem + visibleItemCount;
		if ((lastInScreen == totalItemCount)) {
			if ((getTopTenLists().size() - 1) > visibleChildCount) {
				if (!isRefreshing && offsetNo > 0) {
					callTopTenWs();
				}
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {

	}

	public ArrayList<ClsTopTen> getTopTenLists() {
		return topTenLists;
	}

	public void setTopTenLists(ArrayList<ClsTopTen> topTenLists) {
		this.topTenLists = topTenLists;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}
}