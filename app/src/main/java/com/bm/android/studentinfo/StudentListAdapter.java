package com.bm.android.studentinfo;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.android.studentinfo.db.Student;

import java.util.ArrayList;
import java.util.List;


public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.StudentViewHolder> {

    /*creates a ViewHolder for student_item.xml*/
    class StudentViewHolder extends RecyclerView.ViewHolder {
        private TextView nameView;
        private TextView gradeView;
        private ImageView pictureView;

        private StudentViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.student_name);
            gradeView = itemView.findViewById(R.id.student_grade);
            pictureView = itemView.findViewById(R.id.student_pic);
        }
    }

    private final LayoutInflater mInflater;
    /*Stored list of students in Recyclerview - will be updated when LiveData is updated*/
    private List<Student> mStudents;
    private Context mContext;

    /*set inflater for adapter to use and store context of StudentListActivity*/
    public StudentListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mStudents = new ArrayList<>();
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

        /*Add binding for ImageView using Glide*/
        holder.nameView.setText(fullName);
        holder.gradeView.setText(grade);
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
            mStudents.clear();
            mStudents.addAll(students);
            /*dispatch changes between the lists to the adapter*/
            diffResult.dispatchUpdatesTo(this);
    }
}
