package com.app.crewbid.model;

public class ClsProductList {

    private String productId;
    private String productUserId;
    private String productName;
    private String productDescription;
    private String productThumb;
    private String productPrice;
    private String productSpecialPrice;
    private String productSku;
    private String productTrackingCount;
    private String statusIsAward;
    private String bids;
    private String crewsFunded;
    private String daysLeft;

    public String getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(String daysLeft) {
        this.daysLeft = daysLeft;
    }

    public String getCrewsFunded() {
        return crewsFunded;
    }

    public void setCrewsFunded(String crewsFunded) {
        this.crewsFunded = crewsFunded;
    }

    public String getBids() {
        return bids;
    }

    public void setBids(String bids) {
        this.bids = bids;
    }

    public String getStatusIsAward() {
        return statusIsAward;
    }

    public void setStatusIsAward(String statusIsAward) {
        this.statusIsAward = statusIsAward;
    }

    public String getProductUserId() {
        return productUserId;
    }

    public void setProductUserId(String productUserId) {
        this.productUserId = productUserId;
    }

    public String getProductDateTime() {
        return productSku;
    }

    public void setProductDateTime(String productSku) {
        this.productSku = productSku;
    }

    public String getProductTrackingCount() {
        return productTrackingCount;
    }

    public void setProductTrackingCount(String productHtmlurl) {
        this.productTrackingCount = productHtmlurl;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductTitle() {
        return productName;
    }

    public void setProductTitle(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductThumb() {
        return productThumb;
    }

    public void setProductThumb(String productThumb) {
        this.productThumb = productThumb;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductBidCount() {
        return productSpecialPrice;
    }

    public void setProductBidCount(String productSpecialPrice) {
        this.productSpecialPrice = productSpecialPrice;
    }

}
