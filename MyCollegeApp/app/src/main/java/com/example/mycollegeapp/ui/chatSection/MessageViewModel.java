package com.example.mycollegeapp.ui.chatSection;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mycollegeapp.ui.chatSection.databaseChat.ChatDatabase;
import com.example.mycollegeapp.ui.chatSection.databaseChat.MessageEntity;

import java.util.List;

public class MessageViewModel  extends AndroidViewModel {
    private LiveData<List<MessageEntity>> messages;
    public MessageViewModel(@NonNull Application application) {
        super(application);
        ChatDatabase mdb=ChatDatabase.getInstance(getApplication());
        messages=mdb.chatDao().loadMessages();
    }

    public LiveData<List<MessageEntity>> loadMessages(){return messages;}
}
