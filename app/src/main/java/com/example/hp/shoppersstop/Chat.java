package com.example.hp.shoppersstop;

public class Chat {


    private boolean seen;
    private long timeStamp;
    private String lastMessage;


    Chat() {
    }

    public Chat(boolean seen, long timeStamp, String lastMessage) {
        this.seen = seen;
        this.timeStamp = timeStamp;
        this.lastMessage = lastMessage;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
