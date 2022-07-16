package com.example.mycollegeapp.ui.chatSection.Model;

public class Chat {

    private String sender;
    private String receiver;
    private String message;
    private String photoUrl;
    private String dateAndTime;
    private int isSeen;

    public Chat( String sender, String receiver ,String message,String photoUrl,String dateAndTime, int isSeen){
        this.sender = sender;
        this.receiver= receiver;
        this.message = message;
        this.photoUrl=photoUrl;
        this.dateAndTime = dateAndTime;
        this.isSeen = isSeen;
    }

    public Chat(){

    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getIsSeen() {
        return isSeen;
    }

    public void setSeen(int seen) {
        isSeen = seen;
    }
}
