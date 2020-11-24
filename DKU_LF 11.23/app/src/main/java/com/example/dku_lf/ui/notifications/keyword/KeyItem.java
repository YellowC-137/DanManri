package com.example.dku_lf.ui.notifications.keyword;

public class KeyItem {
    private String title;

    public KeyItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "KeyItem{" +
                "title='" + title + '\'' +
                '}';
    }
}
