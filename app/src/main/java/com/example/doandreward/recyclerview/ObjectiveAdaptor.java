package com.example.doandreward.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandreward.R;
import com.example.doandreward.database.AppDatabase;
import com.example.doandreward.database.AppExecutors;
import com.example.doandreward.entities.Objective;
import com.example.doandreward.entities.PointsHistory;
import com.example.doandreward.recyclerview.helper.BaseSwipeHelper;
import com.example.doandreward.recyclerview.helper.SwipeUI;

import java.sql.Date;
import java.util.Collections;
import java.util.List;

public class ObjectiveAdaptor extends RecyclerView.Adapter<ObjectiveViewHolder>
                              implements BaseSwipeHelper.RecyclerViewRowTouchHelperContract {

    private Context context;
    private List<Objective> objectiveList;

    private final TextView tvTotalPoints;
    private AppDatabase database;

    public ObjectiveAdaptor(Context context, TextView totalPoints) {
        this.tvTotalPoints = totalPoints;
        this.database = AppDatabase.getInstance();
        this.context = context;
    }

    @NonNull
    @Override
    public ObjectiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.objective_row_item, parent, false);
        return new ObjectiveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObjectiveViewHolder viewHolder, int position) {
        Objective objective = objectiveList.get(position);
        viewHolder.getObjectiveDescription().setText(objective.getDescription());
        viewHolder.getObjectivePrizePoints().setText(String.valueOf(objective.getRewardPoints()));
    }

    @Override
    public int getItemCount() {
        if (objectiveList == null) {
            return 0;
        }
        return objectiveList.size();
    }

    @Override
    public void onRowMoved(int from, int to) {
        if (from < to) {
            for (int i = from; i < to; i++) {
                Collections.swap(objectiveList, i, i+1);
            }
        } else {
            for(int i=from; i>to; i--) {
                Collections.swap(objectiveList,i,i-1);
            }
        }
        notifyItemMoved(from, to);
    }

    @Override
    public void onRowSelected(BaseSwipeHelper.DraggableViewHolder viewHolder) {
        LinearLayout layout = viewHolder.getDragNotification();
        layout.setAlpha(0f);
        layout.setVisibility(View.VISIBLE);
        layout.animate()
                .alpha(1f)
                .setDuration(200)
                .setListener(null);
    }

    @Override
    public void onRowClear(BaseSwipeHelper.DraggableViewHolder dragableViewHolder) {
        LinearLayout layout = dragableViewHolder.getDragNotification();
        layout.setVisibility(View.INVISIBLE);
    }

    public void setObjectives(List<Objective> objectives) {
        this.objectiveList = objectives;
        notifyDataSetChanged();
    }

    /**
     * Removes the objective completely from the database
     */
    @Override
    public boolean onDelete(int position) {
        Objective selectedObjective = objectiveList.get(position);
        objectiveList.remove(selectedObjective);
        executeObjectiveDelete(selectedObjective);

        this.notifyItemRemoved(position);
        this.notifyItemRangeChanged(0, getItemCount());
        //ALSO REMOVE HISTORY RELATED
        return true;
    }

    /**
     * Will set the objective with status complete = true.
     * Alos, a prize history record will be saved into PointsHistory table
     */
    @Override
    public boolean onComplete(int position) {
        Objective selectedObjective = objectiveList.get(position);
        selectedObjective.setComplete(true);
        objectiveList.remove(selectedObjective);
        PointsHistoryAdaptor.addRecordToPointsHistory(selectedObjective.getRewardPoints(),
                selectedObjective.getId(), null, tvTotalPoints, database);
        executeObjectiveUpdate(selectedObjective);

        this.notifyItemRemoved(position);
        this.notifyItemRangeChanged(0, getItemCount());
        return true;
    }

    @Override
    public SwipeUI getSwipeRight() {
        return new SwipeUI(
            "Complete",
                Color.WHITE,
                R.drawable.baseline_check_circle_24,
                Color.WHITE,
                Color.GREEN
        );
    }

    @Override
    public SwipeUI getSwipeLeft() {
        return new SwipeUI(
                "Delete",
                Color.WHITE,
                R.drawable.baseline_delete_24,
                Color.WHITE,
                Color.RED
        );
    }

    private void executeObjectiveUpdate(Objective objective) {
        Runnable runnable = () -> {
            database.objectiveDao().updateObjective(objective);
        };
        AppExecutors.getInstance().getSingleThreadRunnable().execute(runnable);
    }

    private void executeObjectiveDelete(Objective objective) {
        Runnable runnable = () -> {
            database.objectiveDao().deleteObjective(objective);
        };
        AppExecutors.getInstance().getSingleThreadRunnable().execute(runnable);
    }

}
