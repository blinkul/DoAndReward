package com.example.doandreward;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.doandreward.database.AppDatabase;
import com.example.doandreward.database.AppExecutors;
import com.example.doandreward.entities.Objective;
import com.example.doandreward.entities.PointsHistory;
import com.example.doandreward.recyclerview.ObjectiveAdaptor;
import com.example.doandreward.recyclerview.helper.BaseSwipeHelper;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/*

TODO:
[2] Nu pot cumpara daca nu am suficiente puncte
[??] BUG: Cand ma joc cu history-ul se cam strica cand dau revert. Raman cele cu 9
[6] Refactorizare
[??] Teste > bune de facut mai ales daca il pun in Git

--- UI ---
[1] - MENU - back button
[1] - MENU - Title
[1] - MENU - Prize Activity / Main Activity
[3] - Remove night mode theme
[5] - Create a theme
[4] - Improve Add Objective / Add prize activities

[] Snackbar with Undo ?
*/

public class MainActivity extends AppCompatActivity {

    private TextView tvTotalPoints;
    private ObjectiveAdaptor adaptor;
    private RecyclerView recyclerView;
    private AppDatabase database;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.tvTotalPoints = findViewById(R.id.txt_points);
        this.database = AppDatabase.getInstance();
        this.recyclerView = findViewById(R.id.list_objectives);
        this.recyclerView.setHasFixedSize(true);
        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.adaptor = new ObjectiveAdaptor(MainActivity.this, this.tvTotalPoints);
        this.recyclerView.setAdapter(this.adaptor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adaptor.setObjectives(retrieveObjectives());
        retrievePointsHistory();
        ItemTouchHelper swipeHelper = new ItemTouchHelper(
                new BaseSwipeHelper(MainActivity.this,
                        recyclerView,
                        0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
        );

        swipeHelper.attachToRecyclerView(recyclerView);
    }

    public void onClickAddObjective(View v) {
        Intent addObjectiveIntent = new Intent(this, AddObjectiveActivity.class);
        startActivity(addObjectiveIntent);
    }

    public void onClickShowPointsHistory(View v) {
        Intent showPointsHistory = new Intent(this, PointsHistoryActivity.class);
        startActivity(showPointsHistory);
    }

    public void onClickGoToPrize(View v) {
        Intent showPrizes = new Intent(this, PrizesActivity.class);
        startActivity(showPrizes);
    }

    private List<Objective> retrieveObjectives() {
        List<Objective> objectives;
        Callable<List<Objective>> callable =
                () -> database.objectiveDao().getAllNotComplete();

        Future<List<Objective>> future = AppExecutors.getInstance().getSingleThreadCallable().submit(callable);
        try {
            objectives = future.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return objectives;
    }

    //TODO: This should be in a separate class (duplicate from PrizeActivity)
    private void retrievePointsHistory() {
        Runnable runnable = () -> {
            final PointsHistory pointsHistory = database.pointsHistoryDao().getLatestRecordByDate();
            if (pointsHistory == null) {
                runOnUiThread(() -> tvTotalPoints.setText("0"));
            } else {
                runOnUiThread(() -> tvTotalPoints.setText(
                        String.valueOf(pointsHistory.getUpdatedPoints()))
                );
            }
        };

        AppExecutors.getInstance().getSingleThreadRunnable().execute(runnable);
    }


}