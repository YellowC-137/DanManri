package com.example.dku_lf.ui.models;

public class Post {

    private String Id;;
    private String title;
    private String contents;

    public Post() {
    }

    public Post(String id, String title, String contents) {
        Id = id;
        this.title = title;
        this.contents = contents;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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

    @Override
    public String toString() {
        return "Post{" +
                "Id='" + Id + '\'' +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                '}';
    }
}
