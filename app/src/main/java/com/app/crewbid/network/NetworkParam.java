package com.app.crewbid.network;

import android.util.Log;

import com.app.crewbid.AppDelegate;
import com.app.crewbid.interfaces.KeyInterface;
import com.app.crewbid.model.ClsAddNewEvent;
import com.app.crewbid.prefs.PreferenceData;
import com.app.crewbid.utility.Utility;

import java.io.File;
import java.util.HashMap;

public class NetworkParam implements KeyInterface {

    // public static final String URL =
    // "http://192.168.10.4/team2/imtunedin/api/";
    public static final String URL = "www.crewbid.com/api/services.php?";
    public static final String METHOD_LOGIN = URL + "function=login";
    public static final String METHOD_SIGNUP = URL + "function=signup";
    public static final String METHOD_POST_CREW_ADD_NEW_EVENT = URL + "function=prepare_add_new_event_page";
    public static final String METHOD_ALL_CREW_EVENTS = URL + "function=all_crew_events";
    public static final String METHOD_MY_CREW_EVENTS = URL + "function=my_crew_events";
    public static final String METHOD_MY_CREW_EVENTS_SUMMERY = URL + "function=get_event_detail";
    public static final String METHOD_ADD_NEW_EVENT = URL + "function=add_new_event";
    public static final String METHOD_PAYMENT_REQ = URL + "function=payment_request";
    public static final String METHOD_ADD_BID_TO_EVENT = URL + "function=add_bid_to_event";
    public static final String METHOD_GET_BIDS_OF_EVENT = URL + "function=get_bids_of_event";
    public static final String METHOD_TRANSFER_PAYMENT = URL + "function=transfer_payment";
    public static final String METHOD_SET_AWARD_OF_EVENT = URL + "function=update_bids_status_on_my_event";
    public static final String METHOD_SET_CARD_DETAIL_UPDATE = URL + "function=update_account";
    public static final String METHOD_SET_NOTIFY_PAYPAL = URL + "function=notify_paypal";

    // Request : user_id=247,status=all,page=1

    public static final String METHOD_ITEM_DETAILS = URL + "";
    public static final String METHOD_BOOKMARKS = URL + "";
    public static final String METHOD_SETBOOKMARK = URL + "";
    public static final String METHOD_DELETE = URL + "";
    public static final String METHOD_SUBMIT = URL + "";
    public static final String METHOD_RECENTLY_VIEWD = URL + "";

    public HashMap<String, Object> getLoginParam(String username,
                                                 String password) {
        HashMap<String, Object> mapParam = new HashMap<String, Object>(2);
        mapParam.put("username", username);
        mapParam.put("password", password);
        return mapParam;
    }

    public HashMap<String, Object> getSignupParam(String email,
                                                  String username, String password, String phone, boolean isVendor) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("qusername", username);
        map.put("qpassword", password);
        map.put("qemail", email);
        map.put("firstname", username);
        map.put("lastname", "");
        map.put("zipcode", "");
        map.put("phone", "");


        if (isVendor) {
            map.put("companyname", "company1");
            map.put("usecompanyname", "1");
        }
        //if vender parameter

        // companyname = company
        //

        return map;
    }

    public HashMap<String, Object> getVendorSignupParam(String email, String username, String password, String phone, String debitcardnumber, String expmonth, String expyear, String cvvnumber) {
        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("qusername", username);
        map.put("qpassword", password);
        map.put("qemail", email);
        map.put("firstname", username);
        map.put("zipcode", "12345");
        map.put("phone", phone);

        map.put("companyname", "company1");
        map.put("usecompanyname", "1");

        map.put("accountName", "124");
        map.put("cardNumber", debitcardnumber);
        map.put("accountExpMonth", expmonth);
        map.put("accountExpYear", expyear);
        map.put("accountCVC", cvvnumber);

        return map;
    }

    public HashMap<String, Object> getPayment(String accesstoken, String amount) {
        HashMap<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("token", accesstoken);
        mapParam.put("amount", amount);
        return mapParam;
    }

    public HashMap<String, Object> setUpdateCardDetail(String userid, String acname, String cardnumber, String acExmonth, String acExYear, String acCVC) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("user_id", userid);
        map.put("accountName", acname);
        map.put("cardNumber", cardnumber);
        map.put("accountExpMonth", acExmonth);
        map.put("accountExpYear", acExYear);
        map.put("accountCVC", acCVC);
        map.put("transferType", "card");

        return map;
    }


    public HashMap<String, Object> getPostNewEventParam(
            ClsAddNewEvent addNewEvent, String accesstoken, String amount) {
        HashMap<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("user_id", AppDelegate.getString(PreferenceData.USERID));
        mapParam.put("score", "1");
        mapParam.put("crew_id", "");
        mapParam.put("subcmd", "preview_post_first");
        mapParam.put("project_title",
                Utility.filter(addNewEvent.getEventName()));
        mapParam.put("description_post", "");

        String categoryId = "";
        /*if (addNewEvent.getPosCategory() != -1) {
            if (addNewEvent.getProjectCategoryList().size() > addNewEvent
                    .getPosCategory()) {
                categoryId = addNewEvent.getProjectCategoryList()
                        .get(addNewEvent.getPosCategory()).getId().toString();
            }
        }*/
        mapParam.put("cid", addNewEvent.getPosCategory());

//        mapParam.put("cid", "668");

        String cityId = "";
        if (addNewEvent.getPosCity() != -1) {
            if (addNewEvent.getProjectCityList().size() > addNewEvent
                    .getPosCity()) {
                cityId = addNewEvent.getProjectCityList()
                        .get(addNewEvent.getPosCity()).getId().toString();
            }
        }
        mapParam.put("city", cityId);
        mapParam.put("ccrecepient", "");
        mapParam.put("filter_budget", addNewEvent.getFilterBudget()); // dynamic karavi
        mapParam.put("filtered_budgetid", "");
        mapParam.put("filtered_budgetid_1", "");
        mapParam.put("filtered_budgetid_2", "");
        mapParam.put("attenting ",
                Utility.filter(addNewEvent.getNumberAttending()));
        mapParam.put("skill_set", "no");
        mapParam.put("roleid", "1");

        mapParam.put("start_date", Utility.convertMillisToDate(addNewEvent
                .getmCalendar().getTimeInMillis(), KeyInterface.DD_MM_YYYY));

        mapParam.put("additional-addons", "0");

        if (Utility.filter(addNewEvent.getLogoPath()).length() != 0) {
            mapParam.put("file_0", new File(addNewEvent.getLogoPath()));
        }

        if (Utility.filter(addNewEvent.getAttachmentPath()).length() != 0) {
            mapParam.put("file_1", new File(addNewEvent.getAttachmentPath()));
        }

        mapParam.put("token", accesstoken);
        mapParam.put("amount", amount);
        mapParam.put("filter_budget", amount);

        Log.i("filter_budget", "=>" + amount);
        return mapParam;
    }


    public HashMap<String, Object> getAddNewParam(String uId) {
        HashMap<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("user_id", uId);
        return mapParam;
    }

    public HashMap<String, Object> getMyCrewEventsParam(String userId,
                                                        String status, int page) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("user_id", userId);
        map.put("status", status);
        map.put("page", "" + page);
        Log.e("tag", "Request : " + map.toString());
        return map;
    }

    public HashMap<String, Object> getMyCrewSummeryParam(int project_id, String userId
    ) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("project_id", project_id);
        map.put("user_id", userId);
//		map.put("page", "" + page);
        Utility.log("tag", "Request : " + map.toString());
        return map;
    }

    public HashMap<String, Object> getBidListParam(int project_id, String userId
    ) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("project_id", project_id);
        map.put("user_id", userId);
//		map.put("page", "" + page);
        Utility.log("tag", "Request : " + map.toString());
        return map;
    }


    public HashMap<String, Object> getAddBidtoEventParam(int project_id, String userId,
                                                         String cid, String bidamount,
                                                         String estimateDays, String featured, String proposal
    ) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("project_id", project_id);
        map.put("user_id", userId);
        map.put("cid", "" + cid);
        map.put("bidamount", "" + bidamount);
        map.put("estimate_days", "" + estimateDays);
        map.put("featured", "" + featured);
        map.put("proposal", "" + proposal);
        Utility.log("tag", "Request : " + map.toString());
        return map;
    }

    public HashMap<String, Object> getPaymentTransfer(String vendorid, String amount) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("vendorId", vendorid);
        map.put("amount", amount);
        Utility.log("tag", "Request : " + map.toString());
        return map;
    }

    public HashMap<String, Object> getNotifyPaypal(String userid, String project_id, String ammount, String txn_id) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("user_id", userid);
        map.put("project_id", project_id);
        map.put("ammount", ammount);
        map.put("txn_id", txn_id);
        return map;
    }


    public HashMap<String, Object> getAwardEventParam(String userId,
                                                      String bidid, String bidstatus

    ) {
        HashMap<String, Object> map = new HashMap<String, Object>();
//        map.put("project_id", project_id);
        map.put("user_id", userId);
        map.put("bidid", "" + bidid);
        map.put("bidstatus", "" + bidstatus);
//        map.put("estimate_days", "" + estimateDays);
//        map.put("featured", "" + featured);
//        map.put("proposal", "" + proposal);
        Utility.log("tag", "Request : " + map.toString());
        return map;
    }


}
