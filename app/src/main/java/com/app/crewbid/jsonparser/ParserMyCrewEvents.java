package com.app.crewbid.jsonparser;

import android.util.Log;

import com.app.crewbid.model.ClsProductList;
import com.app.crewbid.network.ClsNetworkResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParserMyCrewEvents extends AbstractParser {

    public ParserMyCrewEvents(ClsNetworkResponse clsResponse) {
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
            ArrayList<ClsProductList> productLists = new ArrayList<ClsProductList>();
            JSONArray jAryData = jsonObject.optJSONArray("data");
            if (jAryData != null && jAryData.length() > 0) {
                for (int index = 0; index < jAryData.length(); index++) {
                    JSONObject objData = jAryData.getJSONObject(index);
                    ClsProductList clsProductList = new ClsProductList();
                    clsProductList.setProductTitle(objData
                            .optString("project_title"));
                    clsProductList.setProductUserId(objData
                            .optString("userid"));
                    clsProductList.setProductId(objData
                            .optString("projectid"));
                    clsProductList.setProductDateTime(objData
                            .optString("date_starts"));
                    clsProductList.setStatusIsAward(objData
                            .optString("status"));
//                    clsProductList.setBidStatus(objData
//                            .optString("bidstatus"));
                    Log.d("pp", "status" + objData
                            .optString("status"));
                    clsProductList.setProductPrice(objData
                            .optString("filter_budget"));

                    clsProductList.setBids(objData.optString("bids"));

                    clsProductList.setCrewsFunded(objData.optString("crews_funded"));

                    Log.d("pp", objData.optString("crews_funded"));

                    clsProductList.setDaysLeft(objData.optString("days_left"));
//                    clsProductList.setProductBidCount(objData
//                            .optString("uniquebidcount"));

                    clsProductList.setProductBidCount(objData
                            .optString("uniquebidcount"));
                    clsProductList.setProductDescription(objData
                            .optString("description"));
                    clsProductList.setProductTrackingCount(objData
                            .optString("ship_trackingnumber"));

                    JSONArray projectimg = objData.getJSONArray("pro_images");
                    try {
                        JSONObject thumbjsn = (JSONObject) projectimg.get(0);
                        String thumbimg = thumbjsn.optString("image");
                        Log.i("thumbimg", "=>" + thumbimg);
                        clsProductList.setProductThumb(thumbimg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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
