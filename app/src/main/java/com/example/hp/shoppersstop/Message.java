package com.example.hp.shoppersstop;

import com.google.firebase.database.ServerValue;

public class Message {

    private String message;
    private String from;
    private boolean seen;
    private String type;
    private long time;


    public Message(String message, String from, boolean seen, String type, long time) {
        this.message = message;
        this.from = from;
        this.seen = seen;
        this.type = type;
        this.time = time;
    }


    Message(){

    }


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
