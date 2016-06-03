package com.app.crewbid.jsonparser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.crewbid.model.ClsLogin;
import com.app.crewbid.model.ClsProductList;
import com.app.crewbid.model.ClsTopTen;
import com.app.crewbid.network.ClsNetworkResponse;

public class ParserTopTenLists extends AbstractParser {

	public ParserTopTenLists(ClsNetworkResponse clsResponse) {
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
			ArrayList<ClsTopTen> productLists = new ArrayList<ClsTopTen>();
			JSONArray jAryData = jsonObject.optJSONArray("data");
			if (jAryData != null && jAryData.length() > 0) {
				for (int index = 0; index < jAryData.length(); index++) {
					JSONObject objData = jAryData.getJSONObject(index);
					ClsTopTen clsTopTen = new ClsTopTen();
					clsTopTen.setProductTitle(objData
							.optString("project_title"));
					clsTopTen.setProductDateTime(objData
							.optString("date_time"));
					clsTopTen.setProductPrice(objData
							.optString("crewtotal"));
					clsTopTen.setProductBidCount(objData
							.optString("uniquebidcount"));
					clsTopTen.setProductBidCount(objData
							.optString("uniquebidcount"));
					clsTopTen.setProductDescription(objData
							.optString("description"));
					clsTopTen.setProductTrackingCount(objData
							.optString("ship_trackingnumber"));
					productLists.add(clsTopTen);
				}
			}
			clsResponse.setObject(productLists);
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
