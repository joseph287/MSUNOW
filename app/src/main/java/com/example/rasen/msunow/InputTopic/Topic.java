package com.example.rasen.msunow.InputTopic;

/**
 * Created by willl on 4/16/2017.
 */

public class Topic {
    private String title;
    private String body;
    private String room;
    private String author;
    private String time;
    private String photoURL;
    private int karma;
    private boolean subscribe;

    public Topic(){
    }

    public Topic(String title, String body, String room, String author, String time, String photoURL, int karma, boolean subscribe){
        this.title=title;
        this.body= body;
        this.room= room;
        this.author=author;
        this.time=time;
        this.photoURL=photoURL;
        this.karma=karma;
        this.subscribe = subscribe;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getRoom() {
        return room;
    }

    public String getAuthor() {
        return author;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public int getKarma() {
        return karma;
    }

    public boolean getSubscribe() { return subscribe;}

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public void setKarma(int karma) {
        this.karma = karma;
    }

    public void setSubscribe(boolean subscribe) {this.subscribe = subscribe;}

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
