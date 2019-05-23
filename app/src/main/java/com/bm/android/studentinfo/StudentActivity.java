package com.bm.android.studentinfo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bm.android.studentinfo.db.Student;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.io.FileUtils;

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
    private static final String KEY_PIC_CHANGED = "picChanged";
    private static final String PHOTO_AUTHORITY = "com.bm.android.student_records.fileprovider";
    private static final int REQUEST_PHOTO = 0;
    private boolean picChanged;
    private File photoDisplayFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        picChanged = false;
        if (savedInstanceState != null) {
            picChanged = savedInstanceState.getBoolean(KEY_PIC_CHANGED);
        }
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
                //2. edge-case: the query returned extremely fast
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

            //if a new student:
        } else {
            /*if a picture has been taken but not saved yet, set photoDisplayFile to the staged
            photo, or else photoDisplayFile will be null and a gray background will appear where
            the photo is supposed to be.*/
            if (picChanged) {
                photoDisplayFile = makeFile(getStagedPhotoPath());
            }
            MyAppGlideModule.loadPhoto(this, photoDisplayFile, mStudentImageView,
                    new ColorDrawable(Color.GRAY));
        }
    }

    private void updateUI(Student student)  {
        mFirstName.setText(student.getFirstName());
        mLastName.setText(student.getLastName());
        mEmail.setText(student.getEmail());
        mGrade.setText(student.getGrade());
        if (picChanged == false) {
            photoDisplayFile =  makeFile(student.getPhotoPath());
        } else {
            photoDisplayFile =  makeFile(getStagedPhotoPath());
        }

        MyAppGlideModule.loadPhoto(this, photoDisplayFile, mStudentImageView,
                new ColorDrawable(Color.GRAY));
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_PIC_CHANGED, picChanged);
        super.onSaveInstanceState(savedInstanceState);
    }

    private String getStagedPhotoPath() {
        return mStudentViewModel.getStagedPath();
    }

    private void setPhotoListener()    {
        //"final" makes object reference unchangeable, but object itself is mutable.
        final Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final File stagedFile = makeFile(getStagedPhotoPath());

        mStudentImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            /* get a content URI for the staged photo file that a camera activity can write to */
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getApplicationContext(),
                        PHOTO_AUTHORITY, stagedFile);

                /* EXTRA_OUTPUT used to indicate a content resolver Uri to be used to store the
                requested image or video. */
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                List<ResolveInfo> camActivities = getApplicationContext().getPackageManager()
                        .queryIntentActivities(pictureIntent, PackageManager.MATCH_DEFAULT_ONLY);

                for (int i = 0; i < camActivities.size(); i++)  {
                    /*grant permission to write to uri for each package which can take a picture.*/
                    getApplicationContext()
                            .grantUriPermission(camActivities.get(i).activityInfo.packageName,
                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(pictureIntent, REQUEST_PHOTO);
            }
        });
    }

    private File makeFile(String childPath) {
        File parentDir = this.getFilesDir();
        return new File(parentDir, childPath);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)  {
        if (resultCode != RESULT_OK)    {
            return;
        }

        if (requestCode == REQUEST_PHOTO)   {
            File stagedPhotoFile = makeFile(getStagedPhotoPath());
            Uri uri = FileProvider.getUriForFile(this,
                    PHOTO_AUTHORITY,
                    stagedPhotoFile);
            //Revoke write permission for photo URI
            this.revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            picChanged = true;

            MyAppGlideModule.loadPhoto(this, stagedPhotoFile,
                    mStudentImageView, new ColorDrawable(Color.GRAY));
        }
    }

    @Override
    protected void onResume()   {
        super.onResume();

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNewStudent(StudentActivity.this.mStudentId))  {
                    mStudentViewModel.addStudent(getFieldValues(), picChanged);
                }   else    {
                    mStudentViewModel.updateStudent(getFieldValues(), picChanged);
                }
                setResult(RESULT_OK);
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

        setPhotoListener();
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
