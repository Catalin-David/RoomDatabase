package com.example.roomdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StudentDao {

    @Insert
    void insert(Student student);

    @Delete
    void delete(Student student);

    @Update
    void update(Student student);

    @Query("SELECT * from students")
    List<Student> getAllStudents();

    @Query("SELECT * from students WHERE _id IN (:ids)")
    Student getStudentById(int[] ids);

    @Query("DELETE FROM students WHERE name = :name")
    void deleteByName (String name);

    @Query("SELECT * from students")
    LiveData<List<Student>> liveStudents();
}
