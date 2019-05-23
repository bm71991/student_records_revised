package com.bm.android.studentinfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.android.studentinfo.db.Student;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.bm.android.studentinfo.StudentListActivity.STUDENT_ID_EXTRA;


public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.StudentViewHolder> {

    /*creates a ViewHolder for student_item.xml*/
    class StudentViewHolder extends RecyclerView.ViewHolder {
        private TextView nameView;
        private TextView gradeView;
        private ImageView studentPic ;
        private int id;

        private StudentViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.student_name);
            gradeView = itemView.findViewById(R.id.student_grade);
            studentPic= itemView.findViewById(R.id.student_pic);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getStudentIntent(id, v.getContext());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    private final LayoutInflater mInflater;
    /*Stored list of students in Recyclerview - will be updated when LiveData is updated*/
    private List<Student> mStudents;
    private Context mContext;
    private StudentListViewModel mViewModel;

    /*set inflater for adapter to use and store context of StudentListActivity*/
    public StudentListAdapter(Context context, StudentListViewModel viewModel) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mStudents = new ArrayList<>();
        mViewModel = viewModel;
    };

    @Override
    public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType)   {
        /*Set the itemView for the particular viewHolder to hold reference to,
        * which in this case is the inflated student_item.xml*/
        View itemView = mInflater.inflate(R.layout.student_item, parent, false);
        return new StudentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StudentViewHolder holder, int position)    {
        if (mStudents != null)  {
            /*bind data in the Student object at that position with the fields in
            the corresponding ViewHolder.
             */
            bind(holder, position);
        }
    }

    private void bind(StudentViewHolder holder, int position)   {
        Student currentStudent = mStudents.get(position);
        String fullName = getFullName(currentStudent);
        String grade = currentStudent.getGrade();
        File photoFile = new File(mContext.getFilesDir(), currentStudent.getPhotoPath());

        /*Add binding for ImageView using Glide*/
        holder.nameView.setText(fullName);
        holder.gradeView.setText(grade);
        holder.id = currentStudent.getId();

        MyAppGlideModule.loadPhoto((Activity) mContext, photoFile,
                (ImageView) holder.itemView.findViewById(R.id.student_pic),
                new ColorDrawable(Color.GRAY));
    }

    @Override
    public int getItemCount() {
        if (mStudents != null)  {
            return mStudents.size();
        }
        else return 0;
    }

    private String getFullName(Student currentStudent)    {
        return mContext.getResources()
                .getString(R.string.full_name,
                        currentStudent.getFirstName(),
                        currentStudent.getLastName());
    }

    /*To be called by observer in StudentListActivity
    when notified that LiveData list changed (onChanged)*/
    void setStudents(List<Student> students)    {
            final StudentDiffCallback diffCallback = new StudentDiffCallback(mStudents, students);
            /* find the difference between mStudents and the LiveData */
            final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

            /*update students list */
            mStudents = students;
            /*dispatch changes between the lists to the adapter*/
            diffResult.dispatchUpdatesTo(this);

    }

    private Intent getStudentIntent(int id, Context context)    {
        Intent intent = new Intent(context, StudentActivity.class);
        intent.putExtra(STUDENT_ID_EXTRA, id);
        return intent;
    }

    /*Called when swiping to delete an item for the recyclerView in StudentListActivity*/
    public void onItemDismiss(int position)    {
        Student currentStudent = mStudents.get(position);
        mViewModel.deleteStudent(currentStudent);
    }
}
