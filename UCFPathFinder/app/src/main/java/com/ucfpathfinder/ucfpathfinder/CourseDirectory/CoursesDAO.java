package com.ucfpathfinder.ucfpathfinder.CourseDirectory;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface CoursesDAO {
    @Insert
    public void insert(Course... course);

    @Update
    public void update(Course... course);

    @Delete
    public void delete(Course course);

    @Query("DELETE FROM Course")
    public void nukeTable();

    @Query("SELECT * FROM Course")
    public List<Course> getCourses();
}
