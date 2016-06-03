package com.app.crewbid.network;

import java.io.File;
import java.util.HashMap;

public class ClsNetworkResponse {

	private String url;
	private int requestCode;
	private boolean success;
	private boolean networkConneted;

	private String resultString;
	private Object object;
	private String dispMessage;
	private long pkId = -1;

	private HashMap<String, Object> hashMapParams = null;
	private HashMap<String, File> hashMapFiles = null;

	public String getResult_String() {
		return resultString;
	}

	public void setResult_String(String resultString) {
		this.resultString = resultString;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getRequestCode() {
		return requestCode;
	}

	public void setRequestCode(int requestCode) {
		this.requestCode = requestCode;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getDispMessage() {
		return dispMessage;
	}

	public void setDispMessage(String dispMessage) {
		this.dispMessage = dispMessage;
	}

	public boolean isNetworkConneted() {
		return networkConneted;
	}

	public void setNetworkConneted(boolean networkConneted) {
		this.networkConneted = networkConneted;
	}

	public long getPkId() {
		return pkId;
	}

	public void setPkId(long pkId) {
		this.pkId = pkId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public HashMap<String, Object> getHashMapParams() {
		return hashMapParams;
	}

	public void setHashMapParams(HashMap<String, Object> hashMapParams) {
		this.hashMapParams = hashMapParams;
	}

	public HashMap<String, File> getHashMapFiles() {
		return hashMapFiles;
	}

	public void setHashMapFiles(HashMap<String, File> hashMapFiles) {
		this.hashMapFiles = hashMapFiles;
	}

}
