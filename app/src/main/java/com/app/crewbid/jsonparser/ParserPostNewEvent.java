package com.app.crewbid.jsonparser;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.crewbid.model.ClsPostNewEventRes;
import com.app.crewbid.network.ClsNetworkResponse;

public class ParserPostNewEvent extends AbstractParser {

	public ParserPostNewEvent(ClsNetworkResponse clsResponse) {
		this.clsResponse = clsResponse;
	}

	@Override
	public ClsNetworkResponse parse() {
		// TODO Auto-generated method stub
		try {
			JSONObject jsonObject = new JSONObject(
					clsResponse.getResult_String());
			boolean success = jsonObject.optInt(STATUS) == 1 ? true : false;
			clsResponse.setDispMessage(jsonObject.optString(MESSAGE));
			if (!success) {
				clsResponse.setSuccess(false);
				return clsResponse;
			}

			ClsPostNewEventRes clsPostNewEventRes = new ClsPostNewEventRes();
			clsPostNewEventRes
					.setProject_id(jsonObject.optString("project_id"));
			clsPostNewEventRes.setFeatured_fee(jsonObject
					.optString("featured_fee"));

			clsResponse.setObject(clsPostNewEventRes);
			clsResponse.setSuccess(true);
			clsResponse.setResult_String(null);
		} catch (JSONException e) {
			e.printStackTrace();
			clsResponse.setSuccess(false);
			clsResponse.setDispMessage("Json parsing error");
		}
		return clsResponse;
	}

}
