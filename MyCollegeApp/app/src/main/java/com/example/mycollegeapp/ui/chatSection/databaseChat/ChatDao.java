package com.example.mycollegeapp.ui.chatSection.databaseChat;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChatDao {
    @Query("select * from chatListStudent")
    LiveData<List<ChatListStudentEntity>> loadChatListStudent();

    @Query("select * from chatListFaculty")
    LiveData<List<ChatListFacultyEntity>> loadChatListFaculty();

    @Query("select * from messages")
    LiveData<List<MessageEntity>> loadMessages();

    @Insert
    void insertChat(ChatListStudentEntity chatListStudentsEntity);
}
