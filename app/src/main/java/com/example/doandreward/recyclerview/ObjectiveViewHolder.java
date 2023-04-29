package com.example.doandreward.recyclerview;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.example.doandreward.R;
import com.example.doandreward.recyclerview.helper.BaseSwipeHelper;

import org.jetbrains.annotations.NotNull;

public class ObjectiveViewHolder extends RecyclerView.ViewHolder
                                 implements BaseSwipeHelper.DraggableViewHolder {

    private final TextView objectiveDescription,objectivePrizePoints;
    private final LinearLayout dragNotification;

    public ObjectiveViewHolder(@NotNull final View view) {
        super(view);

        this.objectiveDescription = view.findViewById(R.id.objective_description);
        this.objectivePrizePoints = view.findViewById(R.id.objective_prize);
        this.dragNotification = view.findViewById(R.id.drag_notification);
    }

    public TextView getObjectiveDescription() {
        return this.objectiveDescription;
    }
    public TextView getObjectivePrizePoints() {
        return this.objectivePrizePoints;
    }

    @Override
    public LinearLayout getDragNotification() { return this.dragNotification; }
}
