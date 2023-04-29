package com.example.doandreward.recyclerview.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class BaseSwipeHelper extends ItemTouchHelper.SimpleCallback {

    private RecyclerViewRowTouchHelperContract adaptor;
    private Context context;
    private RecyclerView recyclerView;
    private RecyclerViewRowTouchHelperContract touchHelperContract;

    public BaseSwipeHelper(Context context, RecyclerView recyclerView, int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
        this.adaptor = (RecyclerViewRowTouchHelperContract) recyclerView.getAdapter();
        this.context = context;
        this.recyclerView = recyclerView;
        this.touchHelperContract = (RecyclerViewRowTouchHelperContract) recyclerView.getAdapter();
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragFlag, swipeFlag);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        this.touchHelperContract.onRowMoved(viewHolder.getAbsoluteAdapterPosition(), target.getAbsoluteAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAbsoluteAdapterPosition();

        switch (direction) {
            case ItemTouchHelper.LEFT:
                adaptor.onDelete(position);
                break;
            case ItemTouchHelper.RIGHT:
                adaptor.onComplete(position);
                break;
        }
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof DraggableViewHolder) {
                touchHelperContract.onRowSelected((DraggableViewHolder)viewHolder);
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (viewHolder instanceof DraggableViewHolder) {
            touchHelperContract.onRowClear((DraggableViewHolder) viewHolder);
        }
    }

    // Defining the UI on swipe and how much to swipe
    @Override
    public void onChildDraw(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        SwipeUI swipeLeft = adaptor.getSwipeLeft();
        SwipeUI swipeRight = adaptor.getSwipeRight();
        new RecyclerViewSwipeDecorator.Builder(canvas, recyclerView, viewHolder, dX, dY, actionState,isCurrentlyActive)
                .addSwipeLeftLabel(swipeLeft.getLabel())
                .setSwipeLeftLabelColor(swipeLeft.getLabelColor())
                .addSwipeLeftActionIcon(swipeLeft.getIcon())
                .setSwipeLeftActionIconTint(swipeLeft.getIconTint())
                .addSwipeLeftBackgroundColor(swipeLeft.getBackgroundColor())

                .addSwipeRightLabel(swipeRight.getLabel())
                .setSwipeRightLabelColor(swipeRight.getLabelColor())
                .addSwipeRightActionIcon(swipeRight.getIcon())
                .setSwipeRightActionIconTint(swipeRight.getIconTint())
                .addSwipeRightBackgroundColor(swipeRight.getBackgroundColor())

                .create()
                .decorate();

        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    public interface RecyclerViewRowTouchHelperContract {
        void onRowMoved(int from,int to);
        void onRowSelected(DraggableViewHolder viewHolder);
        void onRowClear(DraggableViewHolder viewHolder);
        boolean onDelete(int position);
        boolean onComplete(int position);
        SwipeUI getSwipeRight();
        SwipeUI getSwipeLeft();
    }

    public interface DraggableViewHolder {
        LinearLayout getDragNotification();
    }

}





