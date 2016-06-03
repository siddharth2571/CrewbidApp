package com.app.crewbid.model;

public class ClsBidOfEvent {


    private String projectUserId;


    private String qty;
    private String estimate_days;
    private String date_added;
    private String date_updated;
    private String date_awarded;
    private String bidStatus;
    private String bidstate;
    private String bidamounttype;
    private String bidcustom;
    private String fvf;
    private String state;
    private String isproxybid;
    private String isshortlisted;
    private String winnermarkedaspaid;
    private String winnermarkedaspaiddate;
    private String winnermarkedaspaidmethod;
    private String buyerpaymethod;
    private String buyershipcost;
    private String buyershipperid;
    private String sellermarkedasshipped;
    private String sellermarkedasshippeddate;
    private String featured;
    private String buyer_rating;
    private String vendor_accept;
    private String tagsamount;
    private String shippingmethod;
    private String trackingnumber;
    private String location_eng;
    private String cc_short_name;

    private String user_country_image;
    private String what_i_earn;
    private String date_added_formated;
    private String skill_title;
    private String earning;
    private String awarded;

    private String userId;
    private String projectId;
    private String phone;

    private String bidId;
    private String first_name;
    private String last_name;
    private String bidamount;
    private String proposal;

//	bidstate


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public void setBidStatus(String bidStatus) {
        this.bidStatus = bidStatus;
    }

    public String getBidStatus() {
        return bidStatus;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setProposal(String proposal) {
        this.proposal = proposal;
    }

    public String getProposal() {
        return proposal;
    }

    public String getBidamount() {
        return bidamount;
    }

    public void setBidamount(String bidamount) {
        this.bidamount = bidamount;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getBidId() {
        return bidId;
    }

    public void setBidId(String bidId) {
        this.bidId = bidId;
    }
}
