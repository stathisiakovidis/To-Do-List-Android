package com.example.todolist.database;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;

//Our database table... kind of
@Entity
public class Task {

    @PrimaryKey (autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "body")
    private String body;

    @ColumnInfo(name = "type", defaultValue = "NODATE")
    private String type;

    @ColumnInfo(name = "locationLat")
    private double lat;

    @ColumnInfo(name = "locationLng")
    private double lng;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Nullable
    @ColumnInfo(name = "calendar")
    private Calendar calendar;

    public Task(){
        type = Type.NODATE.toString();
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public enum Type{
        DRAFT, DATE, NODATE
    }
}
