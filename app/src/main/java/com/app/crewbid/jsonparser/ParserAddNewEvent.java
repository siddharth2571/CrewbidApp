package com.app.crewbid.jsonparser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.crewbid.model.ClsAddNewEvent;
import com.app.crewbid.model.ClsAddNewEvent.ClsCategory;
import com.app.crewbid.model.ClsAddNewEvent.ClsCity;
import com.app.crewbid.network.ClsNetworkResponse;

public class ParserAddNewEvent extends AbstractParser {

	public ParserAddNewEvent(ClsNetworkResponse clsResponse) {
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

			ClsAddNewEvent addNewEvent = new ClsAddNewEvent();
			JSONObject objData = jsonObject.optJSONObject("data");

			ArrayList<ClsCity> listCity = new ArrayList<ClsAddNewEvent.ClsCity>();
			JSONArray jsonArrayCityList = objData
					.optJSONArray("project_city_list");
			for (int i = 0; i < jsonArrayCityList.length(); i++) {
				JSONObject object = jsonArrayCityList.getJSONObject(i);
				listCity.add(new ClsCity(object.optString("id"), object
						.optString("name")));
			}
			addNewEvent.setProjectCityList(listCity);

			ArrayList<ClsCategory> listCategory = new ArrayList<ClsAddNewEvent.ClsCategory>();
			JSONArray jsonArrayCategoryList = objData
					.optJSONArray("category_list");
			for (int i = 0; i < jsonArrayCategoryList.length(); i++) {
				JSONObject object = jsonArrayCategoryList.getJSONObject(i);
				listCategory.add(new ClsCategory(object.optString("id"), object
						.optString("name")));
			}
			addNewEvent.setProjectCategoryList(listCategory);

			String paypalMinimum = objData.optString("paypal_minimum");
			addNewEvent.setPaypalMinimum(paypalMinimum);

			clsResponse.setObject(addNewEvent);
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
