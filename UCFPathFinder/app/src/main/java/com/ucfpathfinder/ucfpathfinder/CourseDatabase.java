package com.ucfpathfinder.ucfpathfinder;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Course.class}, version = 1)
public abstract class CourseDatabase extends RoomDatabase {
    public abstract CoursesDAO getCourseDAO();
}