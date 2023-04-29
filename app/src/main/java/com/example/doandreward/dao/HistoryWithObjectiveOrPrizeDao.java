package com.example.doandreward.dao;

import androidx.room.Dao;
import androidx.room.Query;
import com.example.doandreward.entities.HistoryWithObjectiveOrPrize;
import java.util.List;

@Dao
public interface HistoryWithObjectiveOrPrizeDao {

    @Query(" SELECT h.id AS id, " +
           " o.description AS objectiveDescription, " +
           " p.description AS prizeDescription, " +
           " h.updatedPoints AS totalPoints, " +
           " CASE WHEN h.objectiveId IS NOT NULL THEN h.updatedPoints - h.previousPoints END AS objectivePoints, " +
           " CASE WHEN h.prizeId IS NOT NULL THEN h.updatedPoints - h.previousPoints END AS prizeCost " +
           " FROM PointsHistory h LEFT JOIN objective o ON h.objectiveId = o.id " +
           " LEFT JOIN prize p ON h.prizeId = p.id " +
           " ORDER BY h.date DESC")
    List<HistoryWithObjectiveOrPrize> getPoints();

}
