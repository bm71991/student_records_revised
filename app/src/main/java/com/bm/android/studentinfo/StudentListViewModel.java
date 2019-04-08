package com.bm.android.studentinfo;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.bm.android.studentinfo.db.Student;

import java.util.List;

public class StudentListViewModel extends AndroidViewModel {
    private StudentRepository mRepository;
    private LiveData<List<Student>> allStudents;

    public StudentListViewModel(Application app) {
        super(app);
        mRepository = new StudentRepository(app);
        allStudents = mRepository.getAllStudents();
    }

    public LiveData<List<Student>> getAllStudents() {return allStudents;}

    public void deleteStudent(Student student)   {mRepository.deleteStudent(student);}
}
