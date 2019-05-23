package com.bm.android.studentinfo;

import android.support.v7.util.DiffUtil;
import com.bm.android.studentinfo.db.Student;
import java.util.List;

/*Callback to be passed into calculateDiff method. The DiffUtil.DiffResult will then be dispatched
to the adapter to make the appropriate changes to the mStudents list.
 */
public class StudentDiffCallback extends DiffUtil.Callback {


    List<Student> oldStudents;
    List<Student> newStudents;

    public StudentDiffCallback(List<Student> oldStudents, List<Student> newStudents) {
        this.oldStudents = oldStudents;
        this.newStudents = newStudents;
    }

    @Override
    public int getOldListSize() {
        return oldStudents.size();
    }

    @Override
    public int getNewListSize() {
        return newStudents.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        /*compare whether ids are the same*/
        return oldStudents.get(oldItemPosition).getId() ==
                newStudents.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Student oldStudent = oldStudents.get(oldItemPosition);
        Student newStudent = newStudents.get(newItemPosition);

        return oldStudent.getFirstName().equals(newStudent.getFirstName()) &&
                oldStudent.getLastName().equals(newStudent.getLastName()) &&
                oldStudent.getEmail().equals(newStudent.getEmail()) &&
                oldStudent.getGrade() == newStudent.getGrade() &&
                oldStudent.getPhotoPath().equals(newStudent.getPhotoPath());
    }
}
