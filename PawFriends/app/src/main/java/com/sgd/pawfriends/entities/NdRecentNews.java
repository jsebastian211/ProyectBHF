package com.sgd.pawfriends.entities;

/**
 * Created by Daza on 11/06/2017.
 */

public class NdRecentNews {

    public static final int TITLE = 1;
    public static final int URL = 2;
    public static final int CREATED_DATE = 3;
    public static final int COLOR = 4;
    public static final int TYPE = 5;


    private String title;
    private String url;
    private long createdDate;
    private String color;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getCreated_date() {
        return createdDate;
    }

    public void setCreated_date(long createdDate) {
        this.createdDate = createdDate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public static String getName(int field) {
        switch (field) {
            case TITLE:
                return "title";
            case URL:
                return "url";
            case CREATED_DATE:
                return "createdDate";
            case COLOR:
                return "color";
            default:
                return null;
        }
    }

    public NdRecentNews() {
    }

    public NdRecentNews(String title, String url, long createdDate, String color) {
        this.title = title;
        this.url = url;
        this.createdDate = createdDate;
        this.color = color;
    }
}
