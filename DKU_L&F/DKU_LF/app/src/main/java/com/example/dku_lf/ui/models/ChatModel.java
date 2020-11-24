package com.example.dku_lf.ui.models;

import java.util.HashMap;
import java.util.Map;

public class ChatModel {
    String email;
    String text;
    String name;

    public Map<String,Boolean> users = new HashMap<>(); //채팅방 유저정보
    public String titles;
    public Map<String,Comment> comment = new HashMap<>(); //채팅내용
    public  static class  Comment{
        public String uid;
        public String message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
