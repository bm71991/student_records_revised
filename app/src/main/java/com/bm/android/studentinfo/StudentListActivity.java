package com.bm.android.studentinfo;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bm.android.studentinfo.db.Student;

import java.util.List;

public class StudentListActivity extends AppCompatActivity {
    private StudentListViewModel mStudentListViewModel;
    private TextView mEmptyStudentView;
    private RecyclerView mRecyclerView;
    private static final int ADD_STUDENT_REQUEST_CODE = 0;
    public static final String STUDENT_ID_EXTRA = "com.bm.android.studentinfo.student_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        mEmptyStudentView = findViewById(R.id.empty_view);
        mRecyclerView = findViewById(R.id.student_recyclerview);

        /*get ViewModel - ViewModel will persist over configuration changes*/
        mStudentListViewModel = ViewModelProviders.of(this).get(StudentListViewModel.class);

        /*configure student recyclerView (set adapter and layout manager)*/
        final StudentListAdapter adapter = new StudentListAdapter(this,
                mStudentListViewModel);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*add swiping functionality for deletion of items in the recyclerView.*/
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(adapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);

        /*set an observer on the ViewModel LiveData in order to alert the adapter
        when the LiveData has changed.
         */
        mStudentListViewModel.getAllStudents().observe(this, new Observer<List<Student>>() {
            @Override
            public void onChanged(@Nullable List<Student> students) {
                /*update student list in the adapter using DiffUtil*/
                adapter.setStudents(students);
                /*check whether to toggle between empty view or recyclerview*/
                checkViewState(adapter);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)   {
        getMenuInflater().inflate(R.menu.student_list, menu);
        return true;
    }

    //onOptionsItemSelected
    @Override
    public boolean onOptionsItemSelected(MenuItem item)    {
        switch (item.getItemId())   {
            case (R.id.add_student):
                Intent intent = new Intent(this, StudentActivity.class);
                startActivityForResult(intent, ADD_STUDENT_REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)  {

    }

    public static int getStudentId(Intent data) {
        return data.getIntExtra(STUDENT_ID_EXTRA, -1);
    }

    private void checkViewState(StudentListAdapter adapter)   {
        if (adapter.getItemCount() == 0)   {
            mEmptyStudentView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else  {
            mEmptyStudentView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

}
