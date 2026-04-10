package com.example.sit305ass2;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.time.Month;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

@Entity
public class EventModel {

    @PrimaryKey(autoGenerate = true)
    public int eid;
    @ColumnInfo(name = "title")
    String title;
    @ColumnInfo(name = "category")
    String category;
    @ColumnInfo(name = "location")
    String location;
    @ColumnInfo(name = "year")
    int year;
    @ColumnInfo(name = "month")
    int month;
    @ColumnInfo(name = "day")
    int day;
    @ColumnInfo(name = "hour")
    int hour;
    @ColumnInfo(name = "minute")
    int minute;



    @RequiresApi(api = Build.VERSION_CODES.O)
    public EventModel(String title, String category, String location,
                      int year, int month, int day, int hour, int minute) {
        this.title = title;
        this.category = category;
        this.location = location;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDate()
    {
         // Returns "JANUARY"
        return year + " " + Month.of(month).name() + " " + day;
    }

    public String getTime()
    {
        String m = String.valueOf(minute);
        if (minute < 10)
        {
            m = "0" + minute;
        }

        int h = hour % 12;
        if (h == 0) h = 12;
        String AM_PM = (hour > 12) ? "pm" : "am";
        return h + ":" + m + "" + AM_PM;
    }
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

}
