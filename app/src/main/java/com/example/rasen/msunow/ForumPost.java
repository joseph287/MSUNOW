package com.example.rasen.msunow;

/**
 * Created by Rixoro on 4/5/2017.
 */

public class ForumPost {
    private String body;
    private String author;
    private String time;
    private String photoURL;
    private int karma;
    private String title;
    private String room;

    public ForumPost(){

    }

    public ForumPost(String body, String author, String time, String photoURL, int karma, String title, String room) {
        this.body = body;
        this.author = author;
        this.time = time;
        this.photoURL = photoURL;
        this.karma = karma;
        this.title = title;
        this.room = room;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public int getKarma() {
        return karma;
    }

    public void setKarma(int karma) {
        this.karma = karma;
    }
}
