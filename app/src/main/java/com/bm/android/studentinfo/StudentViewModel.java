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

    /*To be called when save button is clicked in StudentActivity for student already in the DB.
     * A new object with the same id as mStudent but with values of the EditTexts for the fields
      * is passed into updateStudent query*/
    public void updateStudent(HashMap<String, String> fieldValues)  {
      int studentId = mStudent.getValue().getId();
      Student student = new Student(studentId);
      setFields(student, fieldValues);
      mRepository.updateStudent(student);
    }

    /*To be called when save button is clicked in StudentActivity for a new student*/
    public void addStudent(HashMap<String, String> fieldValues) {
        Student student = new Student();
        setFields(student, fieldValues);
        /*adds new student to the room db*/
        mRepository.addStudent(student);
    }

    private void setFields(Student student, HashMap<String, String> fieldValues)  {
        student.setFirstName(fieldValues.get("firstName"));
        student.setLastName(fieldValues.get("lastName"));
        student.setGrade(fieldValues.get("grade"));
        student.setEmail(fieldValues.get("email"));
    }
}