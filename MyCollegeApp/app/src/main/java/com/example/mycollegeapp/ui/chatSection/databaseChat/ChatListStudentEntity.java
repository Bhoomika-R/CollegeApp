package com.example.mycollegeapp.ui.chatSection.databaseChat;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "chatListStudent")
public class ChatListStudentEntity {
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "userId")
    @NonNull
    private String userId;
    public ChatListStudentEntity(String userId){
        this.userId=userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
