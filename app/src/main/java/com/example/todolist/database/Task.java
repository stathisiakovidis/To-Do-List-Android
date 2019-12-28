package com.example.todolist.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;



//Our database table... kind of
@Entity
public class Task {

    @PrimaryKey (autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "body")
    private String body;

    @ColumnInfo(name = "dayOfMonth", defaultValue = "13")
    private int day;

    @ColumnInfo(name = "month", defaultValue = "12")
    private int month;

    @ColumnInfo(name = "year", defaultValue = "2019")
    private int year;

    public Task(){}

    public Task(String title, String body, int dayOfMonth, int month, int year){
        this.title = title;
        this.body = body;
        this.day = dayOfMonth;
        this.month = month;
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public int getMonth() {
        return month;
    }
}
