package com.bm.android.studentinfo;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.bm.android.studentinfo.db.Student;

import java.util.HashMap;

public class StudentViewModel extends AndroidViewModel {
    private StudentRepository mRepository;
    private LiveData<Student> mStudent;

    /*will be called inside StudentViewModelFactory's create() method*/
    public StudentViewModel(Application app, int studentId) {
        super(app);
        mRepository = new StudentRepository(app);
        /*If student already exists, query the database to get the record for this student.
        On orientation change, StudentViewModel will not be destroyed and this query will not
        execute again.
         */
        if (!StudentActivity.isNewStudent(studentId)) {
            mStudent = mRepository.getStudent(studentId);
        }
    }

    public LiveData<Student> getStudent()    {
        return mStudent;
    }

    /*To be called when save button is clicked in StudentActivity*/
    public void updateStudent(HashMap<String, String> fieldValues)  {
       /*change fields of student object to values in StudentActivity EditTexts*/
        /*update student in Room db*/
//        mRepository.updateStudent(updatedStudent);
    }

    /*To be called when save button is clicked in StudentActivity and is new student*/
    public void addStudent(HashMap<String, String> fieldValues) {
        Student student = new Student();
        student.setFirstName(fieldValues.get("firstName"));
        student.setLastName(fieldValues.get("lastName"));
        student.setGrade(fieldValues.get("grade"));
        student.setEmail(fieldValues.get("email"));
        /*adds new student to the room db*/
        mRepository.addStudent(student);
    }
}