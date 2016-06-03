package com.app.crewbid.jsonparser;

import com.app.crewbid.model.ClsBidOfEvent;
import com.app.crewbid.network.ClsNetworkResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParserBidList extends AbstractParser {

    public ParserBidList(ClsNetworkResponse clsResponse) {
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
            ArrayList<ClsBidOfEvent> productLists = new ArrayList<ClsBidOfEvent>();
            JSONArray jAryData = jsonObject.optJSONArray("all_bids");
            if (jAryData != null && jAryData.length() > 0) {
                for (int index = 0; index < jAryData.length(); index++) {
                    JSONObject objData = jAryData.getJSONObject(index);
                    ClsBidOfEvent clsProductList = new ClsBidOfEvent();
                    clsProductList.setFirst_name(objData
                            .optString("first_name"));
                    clsProductList.setLast_name(objData
                            .optString("last_name"));
                    clsProductList.setBidId(objData
                            .optString("bid_id"));
                    clsProductList.setBidamount(objData
                            .optString("bidamount"));
                    clsProductList.setProjectId(objData
                            .optString("project_id"));
                    clsProductList.setProposal(objData
                            .optString("proposal"));
                    clsProductList.setUserId(objData
                            .optString("user_id"));
                    clsProductList.setBidStatus(objData
                            .optString("bidstatus"));
                    clsProductList.setPhone(objData
                            .optString("phone"));

//					clsProductList.setProductTitle(objData
//							.optString("project_title"));
//					clsProductList.setProductUserId(objData
//							.optString("userid"));
//					clsProductList.setProductId(objData
//							.optString("projectid"));
//					clsProductList.setProductDateTime(objData
//							.optString("date_time"));
//					clsProductList.setProductPrice(objData
//							.optString("crewtotal"));
//					clsProductList.setProductBidCount(objData
//							.optString("uniquebidcount"));
//					clsProductList.setProductBidCount(objData
//							.optString("uniquebidcount"));
//					clsProductList.setProductDescription(objData
//							.optString("description"));
//					clsProductList.setProductTrackingCount(objData
//							.optString("ship_trackingnumber"));

                    productLists.add(clsProductList);
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
