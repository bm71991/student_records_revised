package com.bm.android.studentinfo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bm.android.studentinfo.db.Student;

import java.util.HashMap;

public class StudentActivity extends AppCompatActivity {
    private StudentViewModel mStudentViewModel;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmail;
    private EditText mGrade;
    private Button mSaveButton;
    private Button mCancelButton;
    private Intent mData;
    private int mStudentId;
    private ImageView mStudentImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        mFirstName = findViewById(R.id.fname);
        mLastName = findViewById(R.id.lname);
        mEmail = findViewById(R.id.email);
        mGrade = findViewById(R.id.grade);
        mSaveButton = findViewById(R.id.save_button);
        mCancelButton = findViewById(R.id.cancel_button);
        mStudentImageView = findViewById(R.id.student_image_view);

        mData = getIntent();
        mStudentId = StudentListActivity.getStudentId(mData);

        /*get StudentViewModel, will survive configuration changes.*/
        mStudentViewModel = ViewModelProviders
                .of(this, new StudentViewModelFactory(getApplication(),
                        /*studentId will be -1 if student is new, StudentViewModel will
                        handle this*/
                        mStudentId) )
                .get(StudentViewModel.class);

        /*If this StudentActivity is displaying a student who already has a record
        in the DB:
        */
        if (!StudentActivity.isNewStudent(mStudentId))    {
            LiveData<Student> currentStudent = mStudentViewModel.getStudent();
            /*
            currentStudent.getValue() will be null when the Activity was just instantiated
            and the query result has not finished yet.
            currentStudent.getValue() will not be null when:
                1. onCreate is being called due to orientation change, the ViewModel is
                not destroyed and still has the Student object stored in mStudent.
                2. edge-case: the query returned extremely fast
             */
            if (currentStudent.getValue() == null) {
                currentStudent.observe(this, new Observer<Student>() {
                    @Override
                    public void onChanged(@Nullable Student student) {
                        updateUI(student);
                    }
                });
            } else {
                updateUI(currentStudent.getValue());
            }
        }
        //For both new and old students:
        MyAppGlideModule.loadPhoto(this, null, mStudentImageView,
                new ColorDrawable(Color.GRAY));
    }

    @Override
    protected void onResume()   {
        super.onResume();

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNewStudent(StudentActivity.this.mStudentId))  {
                    mStudentViewModel.addStudent(getFieldValues());
                    setResult(RESULT_OK);
                    /* update the Student object in DB */
                }   else    {
                    mStudentViewModel.updateStudent(getFieldValues());
                    setResult(RESULT_OK);

                }
                finish();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void updateUI(Student student)  {
            mFirstName.setText(student.getFirstName());
            mLastName.setText(student.getLastName());
            mEmail.setText(student.getEmail());
            mGrade.setText(student.getGrade());
    }

    public HashMap<String, String> getFieldValues() {
        HashMap<String, String> fieldValues = new HashMap<>();
        fieldValues.put("firstName", mFirstName.getText().toString());
        fieldValues.put("lastName", mLastName.getText().toString());
        fieldValues.put("email", mEmail.getText().toString());
        fieldValues.put("grade", mGrade.getText().toString());
        return fieldValues;
    }

    /*If id sent to StudentActivity is -1, then the activity is for a new student
    to be added.
     */
    public static boolean isNewStudent(int id) {
        return id == -1;
    }
}
