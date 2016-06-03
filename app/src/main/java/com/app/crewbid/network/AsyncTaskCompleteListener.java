package com.app.crewbid.network;

public interface AsyncTaskCompleteListener {
	public ClsNetworkResponse doBackGround(ClsNetworkResponse clsResponse);

	public void onTaskComplete(ClsNetworkResponse clsResponse);
}
