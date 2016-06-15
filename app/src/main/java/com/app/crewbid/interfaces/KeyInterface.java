package com.app.crewbid.interfaces;

public interface KeyInterface {
    public static final boolean IS_DEBUG = false;

    public static final int FRAG_SPLASH = 1;
    public static final int FRAG_LOGIN = 2;
    public static final int FRAG_SIGNUP = 3;
    public static final int FRAG_POST_CREWVENT_FORM = 4;
    public static final int FRAG_POST_CREWVENT_UPLOAD = 5;
    public static final int FRAG_POST_CREWVENT_PAYMENT = 6;
    public static final int FRAG_HOME = 7;
    public static final int FRAG_TOP_TEN = 8;
    public static final int FRAG_BROWSE_VENDORS = 9;
    public static final int FRAG_NOTIFICATION = 10;
    public static final int FRAG_SETTINGS = 11;
    public static final int FRAG_LOGOUT = 12;
    public static final int FRAG_DEBITCARD = 13;


    public static final String STATUS = "status";
    public static final String MESSAGE = "message";

    public static final String DD_MM_YYYY = "dd-MM-yyyy";
    public static final String[] FILE_TYPE_IMAGE_FILTER = new String[]{"jpg",
            "jpeg", "png", "bmp", "gif", "tiff", "tif"};
    public static final String[] FILE_TYPE_OTHER_FILTER = new String[]{"doc",
            "docx", "pdf", "jpg", "jpeg", "png", "bmp", "gif", "tiff", "tif"};
}
