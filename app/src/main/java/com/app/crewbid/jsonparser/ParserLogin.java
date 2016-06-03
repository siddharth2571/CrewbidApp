package com.app.crewbid.jsonparser;

import com.app.crewbid.model.ClsLogin;
import com.app.crewbid.network.ClsNetworkResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class ParserLogin extends AbstractParser {

    public ParserLogin(ClsNetworkResponse clsResponse) {
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

            JSONObject objData = jsonObject.optJSONObject("data");
            ClsLogin clsLogin = new ClsLogin();
            clsLogin.setUserId(objData.optString("user_id"));
            clsLogin.setUsecompanyname(objData.optString("usecompanyname"));
            clsLogin.setUsername(objData.optString("username"));
            clsLogin.setIsadmin(objData.optString("isadmin"));
            clsLogin.setSalt(objData.optString("salt"));
            clsLogin.setEmail(objData.optString("email"));
            clsLogin.setFirstName(objData.optString("first_name"));
            clsLogin.setLastName(objData.optString("last_name"));
            clsLogin.setAddress(objData.optString("address"));
            clsLogin.setAddress2(objData.optString("address2"));
            clsLogin.setCity(objData.optString("city"));
            clsLogin.setState(objData.optString("state"));
            clsLogin.setZipCode(objData.optString("zip_code"));
            clsLogin.setCountryCode(objData.optString("countrycode"));
            clsLogin.setAreaCode(objData.optString("areacode"));
            clsLogin.setPhone(objData.optString("phone"));
            clsLogin.setExtention(objData.optString("extention"));
            clsLogin.setIsvender(objData.optString("is_vendor"));
            clsLogin.setHasAccount(objData.optString("hasAccount"));

            clsResponse.setObject(clsLogin);
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
