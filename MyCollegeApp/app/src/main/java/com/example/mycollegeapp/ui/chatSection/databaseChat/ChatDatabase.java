package com.example.mycollegeapp.ui.chatSection.databaseChat;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {ChatListStudentEntity.class,ChatListFacultyEntity.class,MessageEntity.class},version = 1,exportSchema = false)
public abstract class ChatDatabase extends RoomDatabase {
    private static final String LOG_TAG=ChatDatabase.class.getSimpleName();
    private static final String DATABASE_NAME="schedule_list.db";
    private static ChatDatabase chatDatabase;
    public static synchronized ChatDatabase getInstance(Context context){
        if(chatDatabase==null){
            Log.d(LOG_TAG,"Creating new database instance");
            chatDatabase= Room.databaseBuilder(context,ChatDatabase.class,ChatDatabase.DATABASE_NAME).build();

        }
        Log.d(LOG_TAG,"Getting the database instance");
        return chatDatabase;
    }

    public abstract ChatDao chatDao();
}
