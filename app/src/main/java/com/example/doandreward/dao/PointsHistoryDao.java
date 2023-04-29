package com.example.doandreward.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.doandreward.entities.PointsHistory;

@Dao
public interface PointsHistoryDao {

    @Insert
    void addRecord(PointsHistory record);

    @Query("SELECT * FROM PointsHistory ORDER BY date DESC LIMIT 1")
    PointsHistory getLatestRecordByDate();

    @Query("DELETE FROM PointsHistory WHERE id > :id")
    int removeAllRecordsWithIdGreaterThan(int id);
}
