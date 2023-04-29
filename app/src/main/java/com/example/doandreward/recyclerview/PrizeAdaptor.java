package com.example.doandreward.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandreward.R;
import com.example.doandreward.database.AppDatabase;
import com.example.doandreward.database.AppExecutors;
import com.example.doandreward.entities.Objective;
import com.example.doandreward.entities.Prize;
import com.example.doandreward.recyclerview.helper.BaseSwipeHelper;
import com.example.doandreward.recyclerview.helper.SwipeUI;

import java.util.Collections;
import java.util.List;

public class PrizeAdaptor extends RecyclerView.Adapter<PrizeViewHolder>
                          implements BaseSwipeHelper.RecyclerViewRowTouchHelperContract {

    private Context context;
    private List<Prize> prizeList;
    private final TextView tvTotalPoints;
    private AppDatabase database;

    public PrizeAdaptor(Context context, TextView totalPoints) {
        this.tvTotalPoints = totalPoints;
        this.database = AppDatabase.getInstance();
        this.context = context;
    }

    @NonNull
    @Override
    public PrizeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.prize_item, parent, false);
        return new PrizeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrizeViewHolder holder, int position) {
        Prize prize = prizeList.get(position);
        holder.getPrizeDescription().setText(prize.getDescription());
        holder.getPrizeCost().setText(String.valueOf(prize.getCost()));
    }

    @Override
    public int getItemCount() {
        if (prizeList == null) {
            return 0;
        }
        return prizeList.size();
    }


    //TODO: This code is duplicate with ObjectiveAdapter. Create helper class for row swap
    @Override
    public void onRowMoved(int from, int to) {
        if (from < to) {
            for (int i = from; i < to; i++) {
                Collections.swap(prizeList, i, i+1);
            }
        } else {
            for(int i=from; i>to; i--) {
                Collections.swap(prizeList,i,i-1);
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
    public void onRowClear(BaseSwipeHelper.DraggableViewHolder viewHolder) {
        LinearLayout layout = viewHolder.getDragNotification();
        layout.setVisibility(View.INVISIBLE);
    }

    public void setPrizes(List<Prize> prizes) {
        this.prizeList = prizes;
        notifyDataSetChanged();
    }

    public boolean onDelete(int position) {
        Prize selectedPrize = prizeList.get(position);
        prizeList.remove(selectedPrize);
        executePrizeDelete(selectedPrize);

        this.notifyItemRemoved(position);
        this.notifyItemRangeChanged(0, getItemCount());
        return true;
    }

    public boolean onComplete(int position) {
        Prize selectedPrize = prizeList.get(position);
        selectedPrize.setUsed(true);
        prizeList.remove(selectedPrize);
        PointsHistoryAdaptor.addRecordToPointsHistory(selectedPrize.getCost(),
                null, selectedPrize.getId(), tvTotalPoints, database);
        executePrizeUpdate(selectedPrize);

        this.notifyItemRemoved(position);
        this.notifyItemRangeChanged(0, getItemCount());
        return true;
    }

    @Override
    public SwipeUI getSwipeRight() {
        return new SwipeUI(
                "Shop",
                Color.WHITE,
                R.drawable.baseline_add_shopping_cart_24,
                Color.WHITE,
                Color.MAGENTA
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

    private void executePrizeDelete(Prize prize) {
        Runnable runnable = () -> {
            database.prizeDao().deletePrize(prize);
        };
        AppExecutors.getInstance().getSingleThreadRunnable().execute(runnable);
    }

    private void executePrizeUpdate(Prize prize) {
        Runnable runnable = () -> {
            database.prizeDao().updatePrize(prize);
        };
        AppExecutors.getInstance().getSingleThreadRunnable().execute(runnable);
    }
}
