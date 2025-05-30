package com.example.swith.Friend;

public class Message {
    public String senderId;
    public String message;
    public long timestamp;

    public Message() {}

    public Message(String senderId, String message, long timestamp) {
        this.senderId = senderId;
        this.message = message;
        this.timestamp = timestamp;
    }
}
