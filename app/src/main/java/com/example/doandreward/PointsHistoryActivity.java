package com.example.doandreward;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.doandreward.database.AppDatabase;
import com.example.doandreward.database.AppExecutors;
import com.example.doandreward.entities.HistoryWithObjectiveOrPrize;
import com.example.doandreward.recyclerview.PointsHistoryAdaptor;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class PointsHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PointsHistoryAdaptor adaptor;
    private AppDatabase database = AppDatabase.getInstance();
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_history);

        recyclerView = findViewById(R.id.points_history_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adaptor = new PointsHistoryAdaptor(PointsHistoryActivity.this);
        recyclerView.setAdapter(adaptor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adaptor.setHistoryPoints(retrievePointsHistory());
    }

    private List<HistoryWithObjectiveOrPrize> retrievePointsHistory() {
        List<HistoryWithObjectiveOrPrize> pointsHistoryList;
        Callable<List<HistoryWithObjectiveOrPrize>> callable =
                () -> database.historyWithObjectiveOrPrizeDao().getPoints();

        Future<List<HistoryWithObjectiveOrPrize>> future = AppExecutors.getInstance().getSingleThreadCallable().submit(callable);
        try {
            pointsHistoryList = future.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return pointsHistoryList;
    }

    public void onConfirmSelectedHistory(View v) {
        adaptor.removeHistoryNewerThanSelected();
    }

}