package com.bm.android.studentinfo.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


/***********************************************************************
 * Retrieves the database singleton or creates a new one if it does
 * not exist.
 */
@Database(entities = {Student.class}, version = 1)
public abstract class StudentDatabase extends RoomDatabase {
    public abstract StudentDao studentDao();
    private static StudentDatabase db;

    public static StudentDatabase getDatabase(final Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context.getApplicationContext(),
                    StudentDatabase.class, "student_database")
                    .build();
        }
        return db;
    }
}