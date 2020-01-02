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

    @Query("SELECT * FROM task WHERE calendar = :timeInMillis")
    List<Task> getFromThisDay(long timeInMillis);
    @Insert
    void insert(Task task);

    @Delete
    void delete(Task task);

    @Update
    void update(Task task);

}