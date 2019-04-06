package com.bm.android.studentinfo;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;

import com.bm.android.studentinfo.db.Student;

import java.util.HashMap;
import java.util.List;

public class StudentViewModel extends AndroidViewModel {
    private StudentRepository mRepository;
    private Student mStudent;

    /*will be called inside StudentViewModelFactory's create() method*/
    public StudentViewModel(Application app, int studentId) {
        super(app);
        mRepository = new StudentRepository(app);
        /*if student does not exist yet:*/
        if (studentId == -1)    {
            mStudent = new Student();
        } else  {
            mStudent = mRepository.getStudent(studentId).getValue();
        }
    }

    /*used in onCreate in StudentActivity to fill EditTexts with
    appropriate data
     */
    public Student getStudent() {return mStudent;}

    /*To be called when save button is clicked in StudentActivity*/
    public void updateStudent(HashMap<String, String> fieldValues)  {
       /*change fields of student object to values in StudentActivity EditTexts*/
        /*update student in Room db*/
//        mRepository.updateStudent(updatedStudent);
    }

    /*To be called when save button is clicked in StudentActivity and is new student*/
    public void addStudent(HashMap<String, String> fieldValues) {
        mStudent.setFirstName(fieldValues.get("firstName"));
        mStudent.setLastName(fieldValues.get("lastName"));
        mStudent.setGrade(fieldValues.get("grade"));
        mStudent.setEmail(fieldValues.get("email"));
        mRepository.addStudent(mStudent);
    }
}