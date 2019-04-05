package com.bm.android.studentinfo;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.bm.android.studentinfo.db.Student;
import com.bm.android.studentinfo.db.StudentDao;
import com.bm.android.studentinfo.db.StudentDatabase;

import java.util.List;

public class StudentRepository {
    private StudentDao mStudentDao;
    private LiveData<List<Student>> mAllStudents;

    StudentRepository(Application app)  {
        StudentDatabase db = StudentDatabase.getDatabase(app);
        /*be able to use StudentDao operations*/
        mStudentDao = db.studentDao();
        mAllStudents = mStudentDao.getAllStudents();
    }

    /***********************************************************
     * Wrap DAO operations for ViewModels to use.
     * Queries returning LiveData are already
     * executed asynchronously.
     */
    public LiveData<List<Student>> getAllStudents()   {
        return mAllStudents;
    }

    public LiveData<Student> getStudent(String id)  {
        return mStudentDao.getStudent(id);
    }

    public void updateStudent(Student student)  {
        new updateAsyncTask(mStudentDao).execute(student);
    }

    public void addStudent(Student student)  {
        new addAsyncTask(mStudentDao).execute(student);
    }

    public void deleteStudent(Student student)  {
        new deleteAsyncTask(mStudentDao).execute(student);
    }


    /*******************************************
     * wrappers for DAO operations to execute
     * as a background task (not on UI thread)
     */
    private static class updateAsyncTask extends AsyncTask<Student, Void, Void> {
        private StudentDao mAsyncTaskDao;

        updateAsyncTask(StudentDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Student... params) {
            mAsyncTaskDao.updateStudent(params[0]);
            return null;
        }
    }

    private static class addAsyncTask extends AsyncTask<Student, Void, Void> {
        private StudentDao mAsyncTaskDao;

        addAsyncTask(StudentDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Student... params) {
            mAsyncTaskDao.addStudent(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Student, Void, Void> {
        private StudentDao mAsyncTaskDao;

        deleteAsyncTask(StudentDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Student... params) {
            mAsyncTaskDao.deleteStudent(params[0]);
            return null;
        }
    }

}
