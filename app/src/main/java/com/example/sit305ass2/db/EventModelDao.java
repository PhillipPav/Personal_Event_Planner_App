package com.example.sit305ass2.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.sit305ass2.EventModel;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface EventModelDao
{
    @Query("SELECT * FROM eventmodel ORDER BY year, month, day, hour, minute ASC")
    List<EventModel> getAllEvents();

    @Query("DELETE FROM eventmodel WHERE eid = :eid")
    void deleteByUserId(int eid);
    @Insert
    void insertEvent(EventModel... eventModels);

    @Delete
    void delete(EventModel eventModel);


}
