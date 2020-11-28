package com.example.dku_lf.ui.models;

import java.util.HashMap;
import java.util.Map;

public class LocationModel {

    public Map<String, locatonInfo> LocMap = new HashMap<>(); //채팅내용
    public  static class  locatonInfo{
        public String postUid;
        public Double latitude;
        public Double longitude;
        public  String posttype ;
        public String postname;
    }
}
