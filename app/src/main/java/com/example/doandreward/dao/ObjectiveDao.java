package com.example.doandreward.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.doandreward.entities.Objective;

import java.util.List;

@Dao
public interface ObjectiveDao {

    @Query("SELECT * FROM Objective WHERE complete = 0")
    List<Objective> getAllNotComplete();

    @Insert
    void addNewObjective(Objective objective);

    @Update
    void updateObjective(Objective objective);

    @Delete
    void deleteObjective(Objective objective);

}
