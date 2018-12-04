package com.ucfpathfinder.ucfpathfinder;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "Course")
public class Course {
    @PrimaryKey
    @NonNull
    private int courseID;
    private String className;
    private String classCode;
    private String year;
    private String term;
    private String startTime;
    private String endTime;
    private String day;
    private String building;

    public Course(String className, String classCode, String year, String term, String startTime, String endTime, String day, String building)
    {
        setClassName(className);
        setClassCode(classCode);
        setYear(year);
        setTerm(term);
        setStartTime(startTime);
        setEndTime(endTime);
        setDay(day);
        setBuilding(building);
    }

    @NonNull
    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(@NonNull int courseID) {
        this.courseID = courseID;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    @Override
    public String toString() {
        return "Course{" +
                "className='" + className + '\'' +
                ", class code='" + classCode + '\'' +
                ", year='" + year + '\'' +
                ", term='" + term + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", day='" + day + '\'' +
                ", building='" + building + '\'' +
                '}';
    }
}
