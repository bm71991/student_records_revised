package com.bm.android.studentinfo;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

/*factory to allow for passing the student id into StudentViewModel*/
public class StudentViewModelFactory implements ViewModelProvider.Factory   {
    private Application mApplication;
    private int mId;

    public StudentViewModelFactory(Application app, int id)  {
        mApplication = app;
        mId = id;
    }

    /*Creates and returns a StudentViewModel with the specified parameters*/
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new StudentViewModel(mApplication, mId);
    }
}
