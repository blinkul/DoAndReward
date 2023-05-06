package com.example.doandreward;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.TextView;

import com.example.doandreward.database.AppDatabase;
import com.example.doandreward.database.AppExecutors;
import com.example.doandreward.entities.PointsHistory;
import com.example.doandreward.entities.Prize;
import com.example.doandreward.recyclerview.PrizeAdaptor;
import com.example.doandreward.recyclerview.helper.BaseSwipeHelper;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class PrizesActivity extends AppCompatActivity {

    private TextView tvTotalPoints;
    private PrizeAdaptor adaptor;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prizes);

        this.tvTotalPoints = findViewById(R.id.total_points_to_spend_on_prizes);
        this.database = AppDatabase.getInstance();
        this.recyclerView = findViewById(R.id.recycler_view_prizes);
        this.recyclerView.setHasFixedSize(true);
        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.adaptor = new PrizeAdaptor(PrizesActivity.this, tvTotalPoints);
        this.recyclerView.setAdapter(this.adaptor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adaptor.setPrizes(retrievePrizes());
        int total = retrievePointsHistory();
        adaptor.setTotalPoints(total);
        ItemTouchHelper swipeHelper = new ItemTouchHelper(
                new BaseSwipeHelper(PrizesActivity.this,
                        recyclerView,
                        0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
        );
        swipeHelper.attachToRecyclerView(recyclerView);
    }


    //TODO: This code is duplicate with MainActivity
    public void onClickShowPointsHistory(View v) {
        Intent showPointsHistory = new Intent(this, PointsHistoryActivity.class);
        startActivity(showPointsHistory);
    }

    public void onClickAddPrize(View v) {
        Intent addPrizeIntent = new Intent(this, AddPrizeActivity.class);
        startActivity(addPrizeIntent);
    }

    private List<Prize> retrievePrizes() {
        List<Prize> prizes;
        Callable<List<Prize>> callable =
                () -> database.prizeDao().getAllActive();
        Future<List<Prize>> future = AppExecutors.getInstance().getSingleThreadCallable().submit(callable);
        try {
            prizes = future.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return prizes;
    }

    //TODO: This should be in a separate class (duplicate from MainActivity)
    private int retrievePointsHistory() {
        Callable callable = () -> {
            final PointsHistory pointsHistory = database.pointsHistoryDao().getLatestRecordByDate();
            if (pointsHistory == null) {
                runOnUiThread(() -> tvTotalPoints.setText("0"));
                return 0;
            } else {
                int total = pointsHistory.getUpdatedPoints();
                runOnUiThread(() -> tvTotalPoints.setText(
                        String.valueOf(total))
                );
                return total;
            }
        };

        Future<Integer> future = AppExecutors.getInstance().getSingleThreadCallable().submit(callable);
        try {
            return future.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}