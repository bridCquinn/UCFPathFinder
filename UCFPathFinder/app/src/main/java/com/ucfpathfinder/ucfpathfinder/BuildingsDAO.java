package com.ucfpathfinder.ucfpathfinder;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface BuildingsDAO {
    @Insert
    public void insert(Building... building);

    @Update
    public void update(Building... building);

    @Delete
    public void delete(Building building);

    @Query("SELECT * FROM Building")
    public List<Building> getBuildings();
}
