package com.example.dku_lf.ui.models;

public class MarkerModel {
    double lat;//x
    double lng;//y
    String title;
    String type;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public MarkerModel(double lat,double lng, String title,String type){
        this.lat=lat;
        this.lng=lng;
        this.title=title;
        this.type=type;
    }
}
