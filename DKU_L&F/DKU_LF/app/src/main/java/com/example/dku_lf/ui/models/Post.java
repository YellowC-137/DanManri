package com.example.dku_lf.ui.models;

public class Post {

    private String documentId;
    private String title;
    private String contents;

    /* 서버를 이용한 시간 받기
    @ServerTimestamp
    private Date date;
     */

    public Post() {
    }

    public Post(String documentId, String title, String contents) {
        this.documentId = documentId;
        this.title = title;
        this.contents = contents;
        //this.data = data;
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

    /* 시간 부분
    public Date getDate() {
        return data;
    }

    public void setData(Date date) {
        this.data = data;
    }
    */

    @Override
    public String toString() {
        return "Post{" +
                "documentId='" + documentId + '\'' +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                //", date=" +date =
                '}';
    }
}
