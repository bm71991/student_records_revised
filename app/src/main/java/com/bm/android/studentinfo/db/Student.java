package com.bm.android.studentinfo.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/********************************************
 * Student table for Room Database
 */

@Entity(tableName="student_table")
public class Student {
    @NonNull
    private String firstName;

    @NonNull
    private  String lastName;

    @NonNull
    private String email;

    @NonNull
    private String grade;

    private String photoPath;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) {this.firstName = firstName;}

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) {this.lastName = lastName;}

    public String getEmail() { return email; }
    public void setEmail(String email) {this.email = email;}

    public String getGrade() { return grade; }
    public void setGrade(String grade) {this.grade = grade;}

    public String getPhotoPath()    {
        if (photoPath == null)  {
            return "";
        }
        return photoPath;
    }

    public void setPhotoPath(String path) {
        photoPath = path;
    }

    public int getId() { return id; }

    public Student()    {
    }

    /*For use with updating a Student in room db (make new Student,
    enter in id of Student to update manually and set updated fields) */
    @Ignore
    public Student(int id) {
        this.id = id;
    }
}
