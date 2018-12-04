package com.ucfpathfinder.ucfpathfinder.CourseDirectory;

import android.content.Context;

public interface AddDeleteRunnable extends Runnable{
    public void setCourse(Course course, Context context);
}
