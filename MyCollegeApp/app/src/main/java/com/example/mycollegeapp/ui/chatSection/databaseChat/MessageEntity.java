package com.example.mycollegeapp.ui.chatSection.databaseChat;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "messages")
public class MessageEntity {
    @PrimaryKey
    @NonNull
    private String messageId;
    @ColumnInfo(name="sender")
    private String sender;
    @ColumnInfo(name="receiver")
    private String receiver;
    @ColumnInfo(name="message")
    private String message;
    @ColumnInfo(name="photoUrl")
    private String photoUrl;
    @ColumnInfo(name="dateAndTime")
    private String dateAndTime;
    @ColumnInfo(name="isSeen")
    private int isSeen;

    public MessageEntity(String messageId, String sender, String receiver, String message, String photoUrl, String dateAndTime, int isSeen) {
        this.messageId = messageId;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.photoUrl = photoUrl;
        this.dateAndTime = dateAndTime;
        this.isSeen = isSeen;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public int getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(int isSeen) {
        this.isSeen = isSeen;
    }
}
