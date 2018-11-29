package com.ucfpathfinder.ucfpathfinder;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface BuildingsDAO {
    @Insert
    public void insert(BuildingsDB... building);

    @Update
    public void update(BuildingsDB... building);

    @Delete
    public void delete(BuildingsDB building);

    @Query("SELECT * FROM building")
    public list<BuildingsDB> getBuildings();
}
