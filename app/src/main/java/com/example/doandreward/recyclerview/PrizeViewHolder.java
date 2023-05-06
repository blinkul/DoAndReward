package com.example.doandreward.recyclerview;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandreward.R;
import com.example.doandreward.recyclerview.helper.BaseSwipeHelper;

public class PrizeViewHolder extends RecyclerView.ViewHolder
                             implements BaseSwipeHelper.DraggableViewHolder {

    private final TextView prizeDescription, prizeCost;
    private final LinearLayout dragNotification;

    public PrizeViewHolder(@NonNull View view) {
        super(view);

        this.prizeDescription = view.findViewById(R.id.text_layout_prize_description);
        this.prizeCost = view.findViewById(R.id.text_layout_prize_cost);
        this.dragNotification = view.findViewById(R.id.drag_notification);
    }

    public TextView getPrizeDescription() { return this.prizeDescription; }
    public TextView getPrizeCost() { return this.prizeCost; }

    @Override
    public LinearLayout getDragNotification() { return this.dragNotification; }

}
