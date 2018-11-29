package com.ucfpathfinder.ucfpathfinder;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "building")
public class BuildingsDB {
    private String buildingAbbreviation;
    private String buildingName;
    private String plusCode;
    @PrimaryKey
    @NonNull
    private int buildingID;

    public String getBuildingAbbreviation() {
        return buildingAbbreviation;
    }

    public void setBuildingAbbreviation(String buildingAbbreviation) {
        this.buildingAbbreviation = buildingAbbreviation;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getPlusCode() {
        return plusCode;
    }

    public void setPlusCode(String plusCode) {
        this.plusCode = plusCode;
    }

    @NonNull
    public int getBuildingID() {
        return buildingID;
    }

    public void setBuildingID(@NonNull int buildingID) {
        this.buildingID = buildingID;
    }
}

