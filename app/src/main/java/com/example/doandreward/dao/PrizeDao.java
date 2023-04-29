package com.example.doandreward.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.doandreward.entities.Prize;

import java.util.List;

@Dao
public interface PrizeDao {

    @Query("SELECT * FROM Prize WHERE used = 0 ORDER BY description ASC")
    List<Prize> getAllActive();

    @Delete
    void deletePrize(Prize prize);

    @Insert
    void addPrize(Prize prize);

    @Update
    void updatePrize(Prize prize);

}
