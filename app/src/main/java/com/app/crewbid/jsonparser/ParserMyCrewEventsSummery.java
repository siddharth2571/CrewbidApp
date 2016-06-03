package com.app.crewbid.jsonparser;

import android.util.Log;

import com.app.crewbid.model.ClsEventSummeryList;
import com.app.crewbid.network.ClsNetworkResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParserMyCrewEventsSummery extends AbstractParser {

    public ParserMyCrewEventsSummery(ClsNetworkResponse clsResponse) {
        this.clsResponse = clsResponse;
    }

    @Override
    public ClsNetworkResponse parse() {
        // TODO Auto-generated method stub
        try {
            JSONObject jsonObject = new JSONObject(
                    clsResponse.getResult_String());
            boolean success = jsonObject.optInt(STATUS) == 1 ? true : false;
            Log.d("success", "" + success);
            clsResponse.setDispMessage(jsonObject.optString(MESSAGE));
            if (!success) {
                clsResponse.setSuccess(false);
                return clsResponse;
            }

            JSONObject jData = jsonObject.getJSONObject("data");
            Log.d("project_title", "" + jData.optString("project_title"));

            ArrayList<ClsEventSummeryList> productLists = new ArrayList<ClsEventSummeryList>();
//			JSONArray jAryData = jsonObject.optJSONArray("data");
//			Log.d("jAryData.length() :-",""+jAryData.length());
//			if (jAryData != null && jData.length() > 0) {
//				for (int index = 0; index < jAryData.length(); index++) {
//					JSONObject objData = jAryData.getJSONObject(index);
            ClsEventSummeryList clsEventSummeryList = new ClsEventSummeryList();
            clsEventSummeryList.setProjectTitle(jData
                    .optString("project_title"));
            Log.d("project_title", "" + jData.optString("project_title"));
            clsEventSummeryList.setProjectDescription(jData
                    .optString("description"));
            clsEventSummeryList.setUserId(jData
                    .optString("user_id"));
            clsEventSummeryList.setScore(jData
                    .optString("score"));
            clsEventSummeryList.setCid(jData
                    .optString("cid"));
            clsEventSummeryList.setProjectType(jData
                    .optString("project_type"));
            clsEventSummeryList.setProjectDetails(jData
                    .optString("project_details"));
            clsEventSummeryList.setProjectState(jData
                    .optString("project_state"));
            clsEventSummeryList.setCity(jData
                    .optString("city"));
            clsEventSummeryList.setCityName(jData
                    .optString("city_name"));
            clsEventSummeryList.setState(jData
                    .optString("state"));
            clsEventSummeryList.setZipcode(jData
                    .optString("zipcode"));
            clsEventSummeryList.setFilterBudget(jData
                    .optString("filter_budget"));
            clsEventSummeryList.setFilteredBudgetid(jData
                    .optString("filtered_budgetid"));
            clsEventSummeryList.setBidDetails(jData
                    .optString("bid_details"));

            clsEventSummeryList.setBidstatus(jData
                    .optString("bidstatus"));

            clsEventSummeryList.setState(jData
                    .optString("status"));
            clsEventSummeryList.setDateStarts(jData
                    .optString("date_starts"));
            clsEventSummeryList.setImageurl(jData
                    .optString("imageurl"));
            productLists.add(clsEventSummeryList);
//				}
//			}
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
