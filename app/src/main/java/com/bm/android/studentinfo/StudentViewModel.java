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
    private LiveData<Student> mStudent;

    public StudentViewModel(Application app, String studentId) {
        super(app);
        mRepository = new StudentRepository(app);
    }

    /*used in onCreate in StudentActivity to fill EditTexts with
    appropriate data
     */
    public Student getStudent() {return mStudent.getValue();}

    /*To be called when save button is clicked in StudentActivity*/
    public void updateStudent(HashMap<String, String> fieldValues)  {
        Student updatedStudent = new Student(getStudent().getId(),
                fieldValues.get("firstName"),
                fieldValues.get("lastName"),
                fieldValues.get("email"),
                fieldValues.get("grade"));
        mRepository.updateStudent(updatedStudent);
    }
}