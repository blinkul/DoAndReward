package com.example.doandreward.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandreward.R;
import com.example.doandreward.database.AppDatabase;
import com.example.doandreward.database.AppExecutors;
import com.example.doandreward.entities.HistoryWithObjectiveOrPrize;
import com.example.doandreward.entities.PointsHistory;

import java.sql.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class PointsHistoryAdaptor extends RecyclerView.Adapter<PointsHistoryViewHolder> {

    private Context context;
    private List<HistoryWithObjectiveOrPrize> pointsHistoryList;
    private AppDatabase database;
    private int currentPosSelected = -1;

    public PointsHistoryAdaptor(Context context) {
        this.context = context;
        this.database = AppDatabase.getInstance();
    }

    @NonNull
    @Override
    public PointsHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.point_history_item, parent, false);
        return new PointsHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PointsHistoryViewHolder viewHolder, int position) {
        HistoryWithObjectiveOrPrize history = pointsHistoryList.get(position);
        viewHolder.getPointsHistory().setText(computeHistoryRowTotalsMessage(history));
        viewHolder.getDescriptionHistory().setText(computeHistoryRowDescriptionMessage(history));
        viewHolder.itemView.setOnClickListener(
                v -> {
                    currentPosSelected = viewHolder.getBindingAdapterPosition();
                    notifyDataSetChanged();
                }
        );
        if (currentPosSelected == viewHolder.getBindingAdapterPosition()) {
            viewHolder.itemView.findViewById(R.id.linear_layout_for_background)
                                .setBackgroundColor(Color.CYAN);
        } else {
            viewHolder.itemView.findViewById(R.id.linear_layout_for_background)
                    .setBackgroundColor(Color.WHITE);
        }

    }

    private String computeHistoryRowTotalsMessage(HistoryWithObjectiveOrPrize history) {
        StringBuilder outputPointsText = new StringBuilder();
        final Integer prizeCost = history.getPrizeCost();
        final Integer objectivePoints = history.getObjectivePoints();

        if (prizeCost < 0) {
            outputPointsText.append(prizeCost);
        } else {
            outputPointsText.append("+");
            outputPointsText.append(objectivePoints);
        }
        outputPointsText.append(" (new total ");
        outputPointsText.append(history.getTotalPoints());
        outputPointsText.append(")");

        return outputPointsText.toString();
    }

    private String computeHistoryRowDescriptionMessage(HistoryWithObjectiveOrPrize history) {
        if (history.getObjectiveDescription() != null) {
            return history.getObjectiveDescription();
        } else {
            return history.getPrizeDescription();
        }
    }

    @Override
    public int getItemCount() {
        if (pointsHistoryList == null) {
            return 0;
        }
        return pointsHistoryList.size();
    }

    public void setHistoryPoints(List<HistoryWithObjectiveOrPrize> history) {
        this.pointsHistoryList = history;

        HistoryWithObjectiveOrPrize rootObjective = new HistoryWithObjectiveOrPrize();
        rootObjective.setTotalPoints(0);
        rootObjective.setObjectivePoints(0);
        rootObjective.setObjectiveDescription("root");
        this.pointsHistoryList.add(rootObjective);

        notifyDataSetChanged();
    }

    public void removeHistoryNewerThanSelected() {
        HistoryWithObjectiveOrPrize currentHistorySelected = pointsHistoryList.get(currentPosSelected);
        Callable<Integer> callable =
                () -> database.pointsHistoryDao().removeAllRecordsWithIdGreaterThan(currentHistorySelected.getId());
        Future<Integer> future = AppExecutors.getInstance().getSingleThreadCallable().submit(callable);
        int countDeletedRecords;
        try {
            countDeletedRecords = future.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        pointsHistoryList = pointsHistoryList.stream()
                .filter(x -> x.getTotalPoints() <= currentHistorySelected.getTotalPoints())
                .collect(Collectors.toList());

        notifyItemRangeRemoved(0, countDeletedRecords);
    }

    public static void addRecordToPointsHistory(int differenceInPoints, Integer objectiveId, Integer prizeId, TextView view, AppDatabase database) {
        if (objectiveId != null && prizeId != null) {
            throw new IllegalArgumentException("History record should be added either for objective or prize, not both at the same time!");
        }

        Runnable runnable = () -> {
            PointsHistory previousRecord = database.pointsHistoryDao().getLatestRecordByDate();
            PointsHistory newRecord = new PointsHistory();
            newRecord.setDate(new Date(System.currentTimeMillis()));
            if (previousRecord != null) {
                int previousPoints = previousRecord.getUpdatedPoints();
                newRecord.setPreviousPoints(previousPoints);
                int sign = 1;
                if (prizeId != null) {
                    sign = -1;
                }
                newRecord.setUpdatedPoints(previousPoints + differenceInPoints * sign);
            } else {
                newRecord.setUpdatedPoints(differenceInPoints);
            }
            newRecord.setObjectiveId(objectiveId);
            newRecord.setPrizeId(prizeId);

            database.pointsHistoryDao().addRecord(newRecord);

            Runnable uiRunnable = () -> view.setText(
                    String.valueOf(newRecord.getUpdatedPoints()));
            AppExecutors.getInstance().executeRunnableForUi(uiRunnable);
        };
        AppExecutors.getInstance().getSingleThreadRunnable().execute(runnable);
    }
}
