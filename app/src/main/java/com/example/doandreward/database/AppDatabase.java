package com.example.doandreward.database;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.doandreward.AppContext;
import com.example.doandreward.dao.HistoryWithObjectiveOrPrizeDao;
import com.example.doandreward.dao.ObjectiveDao;
import com.example.doandreward.dao.PointsHistoryDao;
import com.example.doandreward.dao.PrizeDao;
import com.example.doandreward.database.converters.Converters;
import com.example.doandreward.entities.Objective;
import com.example.doandreward.entities.PointsHistory;
import com.example.doandreward.entities.Prize;

@Database(entities = {Objective.class, PointsHistory.class, Prize.class},
          version = 5,
          autoMigrations = @AutoMigration(from = 4, to=5),
          exportSchema = true
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "do_win_database";
    private static final AppDatabase INSTANCE = Room.databaseBuilder(
            AppContext.getAppContext(), AppDatabase.class, DB_NAME
            )
//            .fallbackToDestructiveMigration()
            .build();

    public static AppDatabase getInstance() {
        return INSTANCE;
    }

    public abstract ObjectiveDao objectiveDao();

    public abstract PointsHistoryDao pointsHistoryDao();

    public abstract PrizeDao prizeDao();

    public abstract HistoryWithObjectiveOrPrizeDao historyWithObjectiveOrPrizeDao();



}
