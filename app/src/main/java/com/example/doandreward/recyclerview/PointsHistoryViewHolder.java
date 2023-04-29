package com.example.doandreward.recyclerview;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandreward.R;

public class PointsHistoryViewHolder extends RecyclerView.ViewHolder {

    private final TextView pointsHistory, descriptionHistory;

    public PointsHistoryViewHolder(@NonNull View view) {
        super(view);

        this.pointsHistory = view.findViewById(R.id.tv_points);
        this.descriptionHistory = view.findViewById(R.id.tv_description);
    }

    public TextView getPointsHistory() { return this.pointsHistory; }
    public TextView getDescriptionHistory() { return this.descriptionHistory; }

}
