package com.example.sit305ass2.db;

import android.content.Context;

import androidx.compose.runtime.AbstractApplier;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.sit305ass2.EventModel;

@Database(entities = {EventModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase
{
    public abstract EventModelDao eventModelDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDbInstance(Context context)
    {
        if( INSTANCE == null)
        {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "DB NAME").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

}
