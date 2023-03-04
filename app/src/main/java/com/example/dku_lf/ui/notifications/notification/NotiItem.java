package com.example.dku_lf.ui.notifications.notification;

public class NotiItem {

    private String title;
    private String content;
    private String documentID;
    private String notificationID;

    public NotiItem(String title, String content, String documentID, String notificationID) {
        this.title = title;
        this.content = content;
        this.documentID = documentID;
        this.notificationID = notificationID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(String notificationID) {
        this.notificationID = notificationID;
    }

    @Override
    public String toString() {
        return "NotiItem{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", documentID='" + documentID + '\'' +
                ", notificationID='" + notificationID + '\'' +
                '}';
    }
}
