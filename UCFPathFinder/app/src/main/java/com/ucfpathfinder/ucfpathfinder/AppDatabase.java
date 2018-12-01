package com.ucfpathfinder.ucfpathfinder;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Building.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BuildingsDAO getBuildingsDAO();
}