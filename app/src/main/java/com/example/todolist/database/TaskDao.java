package com.example.todolist.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {


    @Query("SELECT * FROM task WHERE type != 'DRAFT'")
    List<Task> getAllDone();

    @Query("SELECT * FROM task WHERE type == 'DRAFT'")
    List<Task> getDraft();

    @Query("SELECT * FROM task WHERE calendar = :timeInMillis AND type == 'DATE'")
    List<Task> getFromThisDay(long timeInMillis);

    @Query("SELECT * FROM task WHERE id = :id")
    Task getSpecific(int id);

    @Query("SELECT * FROM task WHERE type == 'DATE'")
    List<Task> getDateNotes();

    @Insert
    void insert(Task task);

    @Delete
    void delete(Task task);

    @Update
    void update(Task task);

}