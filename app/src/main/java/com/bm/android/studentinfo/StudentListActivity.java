package com.bm.android.studentinfo;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bm.android.studentinfo.db.Student;

import java.util.List;

public class StudentListActivity extends AppCompatActivity {
    private StudentListViewModel mStudentListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        /*configure student recyclerView (set adapter and layout manager)*/
        RecyclerView recyclerView = findViewById(R.id.student_recyclerview);
        final StudentListAdapter adapter = new StudentListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*get ViewModel - ViewModel will persist over configuration changes*/
        mStudentListViewModel = ViewModelProviders.of(this).get(StudentListViewModel.class);
        /*set an observer on the ViewModel LiveData in order to alert the adapter
        when the LiveData has changed.
         */
        mStudentListViewModel.getAllStudents().observe(this, new Observer<List<Student>>() {
            @Override
            public void onChanged(@Nullable List<Student> students) {
                /*update student list in the adapter using DiffUtil*/
                adapter.setStudents(students);
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
