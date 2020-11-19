package com.example.dku_lf.ui.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Post {

    private String documentId;
    private String title;
    private String contents;
    @ServerTimestamp
    private Date date;
    private String user;

    public Post() {
    }

    public Post(String documentId, String title,String user, String contents/*,시간 추가시 사용 Date date*/) {
        this.documentId = documentId;
        this.user = user;
        this.title = title;
        this.contents = contents;
        this.date = date;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Post{" +
                "documentId='" + documentId + '\'' + ", title='" + title +'\''+ " user :"+ user + '\'' + ", contents='" + contents + '\'' + ", date=" + date + '}';
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
