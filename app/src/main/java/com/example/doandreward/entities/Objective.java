package com.example.doandreward.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

//TODO: @NotNull issues
@Entity(tableName = "objective")
public class Objective {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name="points", defaultValue = "0")
    private int rewardPoints;

    @ColumnInfo(name="description")
    @NotNull
    private String description;

    @ColumnInfo(name="complete", defaultValue = "0")
    private boolean complete;

    public Objective(@NotNull String description, int rewardPoints) {
        this.description = description;
        this.rewardPoints = rewardPoints;
    }

    public int getId() {
        return id;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }
    @NotNull public String getDescription() {
        return description;
    }
    public boolean isComplete() { return complete; }

    public void setId(int id) {
        this.id = id;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    public void setDescription(@NotNull String description) {
        this.description = description;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
