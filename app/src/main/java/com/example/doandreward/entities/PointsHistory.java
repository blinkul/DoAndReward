package com.example.doandreward.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import org.jetbrains.annotations.NotNull;
import java.sql.Date;

@Entity(tableName = "PointsHistory",
        foreignKeys = {@ForeignKey(entity = Objective.class,
                                  parentColumns = "id",
                                  childColumns = "objectiveId"),
                       @ForeignKey(entity = Prize.class,
                                  parentColumns = "id",
                                  childColumns = "prizeId")}
        )
public class PointsHistory {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "date")
    @NotNull
    private Date date;

    @ColumnInfo(name = "previousPoints", defaultValue = "0")
    private int previousPoints;

    @ColumnInfo(name = "updatedPoints", defaultValue = "0")
    private int updatedPoints;

    @ColumnInfo(name = "objectiveId")
    private Integer objectiveId;

    @ColumnInfo(name = "prizeId")
    private Integer prizeId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPreviousPoints() {
        return previousPoints;
    }

    public void setPreviousPoints(int previousPoints) {
        this.previousPoints = previousPoints;
    }

    public int getUpdatedPoints() {
        return updatedPoints;
    }

    public void setUpdatedPoints(int updatedPoints) {
        this.updatedPoints = updatedPoints;
    }

    public Integer getObjectiveId() {
        return objectiveId;
    }

    public void setObjectiveId(Integer objectiveId) {
        this.objectiveId = objectiveId;
    }

    public Integer getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(Integer prizeId) {
        this.prizeId = prizeId;
    }
}
