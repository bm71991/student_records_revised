package com.bm.android.studentinfo.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/******************************
 * DAO supporting CRUD operations on Student Room Database
 */
@Dao
public interface StudentDao {

    @Query("SELECT * FROM student_table ORDER BY lastName ASC")
    LiveData<List<Student>> getAllStudents();

    @Query("SELECT * FROM student_table WHERE id = :studentId")
    LiveData<Student> getStudent(String studentId);

    @Update
    void updateStudent(Student student);

    @Delete
    void deleteStudent(Student student);

    @Insert
    void addStudent(Student student);
}
