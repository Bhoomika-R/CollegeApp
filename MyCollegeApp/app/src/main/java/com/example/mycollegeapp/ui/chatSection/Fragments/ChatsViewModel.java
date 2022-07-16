package com.example.mycollegeapp.ui.chatSection.Fragments;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mycollegeapp.ui.chatSection.databaseChat.ChatDatabase;
import com.example.mycollegeapp.ui.chatSection.databaseChat.ChatListFacultyEntity;
import com.example.mycollegeapp.ui.chatSection.databaseChat.ChatListStudentEntity;

import java.util.List;

public class ChatsViewModel extends AndroidViewModel {
    private LiveData<List<ChatListStudentEntity>> chatListStudent;
    private LiveData<List<ChatListFacultyEntity>> chatListFaculty;
    public ChatsViewModel(@NonNull Application application) {
        super(application);
        ChatDatabase mdb=ChatDatabase.getInstance(getApplication());
        chatListStudent=mdb.chatDao().loadChatListStudent();
        chatListFaculty=mdb.chatDao().loadChatListFaculty();
    }
    public LiveData<List<ChatListStudentEntity>> getChatListStudent(){return chatListStudent;}

    public LiveData<List<ChatListFacultyEntity>> getChatListFaculty(){return chatListFaculty;}

}
