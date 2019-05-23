package com.bm.android.studentinfo;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import org.apache.commons.io.FileUtils;

import com.bm.android.studentinfo.db.Student;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class StudentViewModel extends AndroidViewModel {
    private StudentRepository mRepository;
    private LiveData<Student> mStudent;
    private Student mNewStudent;
    private static final String STAGED_PATH = "staged.png";

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
            //if student does not already exist, make a
        } else  {
            /*if saved, the stagedPhotoFile path does not matter in this context*/
            mNewStudent = new Student(studentId);
        }
    }

    public String getStagedPath()   {
        return STAGED_PATH;
    }

    public LiveData<Student> getStudent()    {
        return mStudent;
    }

    public Student getNewStudent()  {
        return mNewStudent;
    }

    private File makeFile(String childPath) {
        File parentDir = this.getApplication().getFilesDir();
        return new File(parentDir, childPath);
    }

    /*To be called when save button is clicked in StudentActivity for student already in the DB.
     * A new object with the same id as mStudent but with values of the EditTexts for the fields
      * is passed into updateStudent query*/
    public void updateStudent(HashMap<String, String> fieldValues, boolean picChanged)
             {
      int studentId = mStudent.getValue().getId();
      Student student = new Student(studentId);
      setFields(student, fieldValues);
     if (picChanged == true)   {
         try {
             updatePhoto(student);
         } catch (IOException ioe)  {
             ioe.printStackTrace();
         }
     }
      mRepository.updateStudent(student);
    }

    private void updatePhoto(Student student) throws IOException {
        //make a new photo file name
        String savedPhotoFileName = UUID.randomUUID().toString() + ".png";
        //copy the staged photo into the path of the savedPhoto
        try {
            FileUtils.copyFile(makeFile(getStagedPath()), makeFile(savedPhotoFileName));
        } catch (Exception ioe)   {
            ioe.printStackTrace();
        }
        //set the student's photo path to the new savedPhotoFileName.
        student.setPhotoPath(savedPhotoFileName);
    }

    /*To be called when save button is clicked in StudentActivity for a new student*/
    public void addStudent(HashMap<String, String> fieldValues, boolean picChanged) {
        Student student = new Student();
        setFields(student, fieldValues);
        if (picChanged == true)   {
            try {
                updatePhoto(student);
            } catch (IOException ioe)  {
                ioe.printStackTrace();
            }
        }

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