package com.app.crewbid.network;

import java.util.HashMap;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Process;

import com.app.crewbid.interfaces.KeyInterface;
import com.app.crewbid.utility.Utility;

public class NetworkTask extends AsyncTask<String, Void, ClsNetworkResponse>
		implements KeyInterface {

	private Context mContext;
	private AsyncTaskCompleteListener mCallback;

	private boolean isGet = false;
	private boolean isDelete = false;
	private boolean isMultipart = false;

	private long pkId = -1;

	private HashMap<String, Object> hashMapParams = null;

	public NetworkTask(Context context) {
		this.mContext = context;
		hashMapParams = new HashMap<String, Object>();
		try {
			this.mCallback = (AsyncTaskCompleteListener) context;
		} catch (ClassCastException e) {
			Utility.log("======Base Async======",
					"Ignore ClassCastException. If don't get call back, then check this.");
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(ClsNetworkResponse result) {
		super.onPostExecute(result);
		if (result != null)
			result.setPkId(getPkId());
		if (mCallback != null)
			mCallback.onTaskComplete(result);
	}

	/**
	 * @param weblink
	 * @param requestCode
	 */
	@Override
	protected ClsNetworkResponse doInBackground(String... pParams) {
		Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

		ClsNetworkResponse objClsResponse = null;
		if (pParams.length == 2) {
			objClsResponse = callWithParameter(pParams[0], pParams[1]);
		}
		if (mCallback != null)
			objClsResponse = mCallback.doBackGround(objClsResponse);
		return objClsResponse;
	}

	public ClsNetworkResponse callWithParameter(String url, String requestCode) {
		ClsNetworkResponse clsResponse = new ClsNetworkResponse();
		if (url == null || requestCode == null) {
			clsResponse.setDispMessage("Please check url");
			return clsResponse;
		}

		if (!url.startsWith("http")) {
			url = "http://" + url;
		}

		clsResponse.setUrl(url);
		clsResponse.setRequestCode(Integer.valueOf(requestCode));
		clsResponse.setPkId(getPkId());
		clsResponse.setSuccess(false);
		clsResponse.setNetworkConneted(false);
		clsResponse.setResult_String(null);
		clsResponse.setObject(null);

		if (!Utility.checkInternetConnection(mContext)) {
			clsResponse.setDispMessage("Please check your internet connection");
			return clsResponse;
		}

		clsResponse.setNetworkConneted(true);
		clsResponse.setHashMapParams(hashMapParams);

		NetworkConnectionClient client = new NetworkConnectionClient(
				clsResponse);
		if (hashMapParams != null)
			Utility.log("Request param", "@" + hashMapParams.toString());
		if (isDelete) {
			client.execute(NetworkConnectionClient.DELETE);
		} else if (isGet) {
			client.execute(NetworkConnectionClient.GET);
		} else if (isMultipart) {
			client.execute(NetworkConnectionClient.MULTIPART);
		} else {
			// default post
			client.execute(NetworkConnectionClient.POST);
		}

		clsResponse = client.getClsResponse();
		clsResponse.setHashMapParams(null);
		clsResponse.setUrl(null);
		client = null;
		return clsResponse;
	}

	public void setmCallback(AsyncTaskCompleteListener mCallback) {
		this.mCallback = mCallback;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	public boolean isGet() {
		return isGet;
	}

	public void setGet(boolean isGet) {
		this.isGet = isGet;
	}

	public long getPkId() {
		return pkId;
	}

	public void setPkId(long pkId) {
		this.pkId = pkId;
	}

	public HashMap<String, Object> getHashMapParams() {
		return hashMapParams;
	}

	public void setHashMapParams(HashMap<String, Object> hashMapParams) {
		this.hashMapParams = hashMapParams;
	}

	public boolean isMultipart() {
		return isMultipart;
	}

	public void setMultipart(boolean isMultipart) {
		this.isMultipart = isMultipart;
	}

}