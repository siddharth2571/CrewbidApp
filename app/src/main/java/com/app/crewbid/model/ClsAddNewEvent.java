package com.app.crewbid.model;

import java.util.ArrayList;
import java.util.Calendar;

public class ClsAddNewEvent {

    // support variable
    private Calendar mCalendar;
    private String eventName, numberAttending, budget, filterBudget;
    private String logoPath, attachmentPath;

    // response data
    private int posCrew = -1;
    private ArrayList<ClsCity> projectCityList;
    private int posCity = -1;
    private ArrayList<ClsCategory> projectCategoryList;
    private String posCategory = "";
    private String paypalMinimum;

    public void setFilterBudget(String filterBudget) {
        this.filterBudget = filterBudget;
    }

    public String getFilterBudget() {
        return filterBudget;
    }

    public ArrayList<ClsCity> getProjectCityList() {
        return projectCityList;
    }

    public void setProjectCityList(ArrayList<ClsCity> projectCityList) {
        this.projectCityList = projectCityList;
    }

    public ArrayList<ClsCategory> getProjectCategoryList() {
        return projectCategoryList;
    }

    public void setProjectCategoryList(
            ArrayList<ClsCategory> projectCategoryList) {
        this.projectCategoryList = projectCategoryList;
    }

    public String getPaypalMinimum() {
        return paypalMinimum;
    }

    public void setPaypalMinimum(String paypalMinimum) {
        this.paypalMinimum = paypalMinimum;
    }

    public Calendar getmCalendar() {
        return mCalendar;
    }

    public void setmCalendar(Calendar mCalendar) {
        this.mCalendar = mCalendar;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getNumberAttending() {
        return numberAttending;
    }

    public void setNumberAttending(String numberAttending) {
        this.numberAttending = numberAttending;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public int getPosCity() {
        return posCity;
    }

    public void setPosCity(int posCity) {
        this.posCity = posCity;
    }

    public String getPosCategory() {
        return posCategory;
    }

    public void setPosCategory(String posCategory) {
        this.posCategory = posCategory;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public int getPosCrew() {
        return posCrew;
    }

    public void setPosCrew(int posCrew) {
        this.posCrew = posCrew;
    }

    public static class ClsCity {
        private String id, name;

        public ClsCity(String id, String name) {
            super();
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class ClsCategory {
        private String id, name;

        public ClsCategory(String id, String name) {
            super();
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
