package com.bm.android.studentinfo;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bm.android.studentinfo.db.Student;

import java.util.HashMap;

public class StudentActivity extends AppCompatActivity {
    private StudentViewModel mStudentViewModel;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmail;
    private EditText mGrade;
    private Button mSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        mFirstName = findViewById(R.id.fname);
        mLastName = findViewById(R.id.lname);
        mEmail = findViewById(R.id.email);
        mGrade = findViewById(R.id.grade);
        mSaveButton = findViewById(R.id.save_button);


        Intent data = getIntent();
        /*get StudentViewModel, will survive configuration changes.*/
            mStudentViewModel = ViewModelProviders
                    .of(this, new StudentViewModelFactory(getApplication(),
                            /*studentId will be -1 if student is new, StudentViewModel will
                            handle this*/
                            StudentListActivity.getStudentId(data)) )
                    .get(StudentViewModel.class);
    }

    @Override
    protected void onResume()   {
        super.onResume();
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test", "in onClick");
                mStudentViewModel.addStudent(getFieldValues());
                setResult(RESULT_OK);
                finish();
            }
        });
    }


    public HashMap<String, String> getFieldValues() {
        HashMap<String, String> fieldValues = new HashMap<>();
        fieldValues.put("firstName", mFirstName.getText().toString());
        fieldValues.put("lastName", mLastName.getText().toString());
        fieldValues.put("email", mEmail.getText().toString());
        fieldValues.put("grade", mGrade.getText().toString());
        return fieldValues;
    }

}
