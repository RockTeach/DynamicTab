package com.rock.rock.dynamictab.callback;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class ItemTouchCallback extends ItemTouchHelper.Callback {

    private ItemTouchHelperAdapterCallback callback;

    public ItemTouchCallback(ItemTouchHelperAdapterCallback callback) {
        this.callback = callback;
    }

    /**
     *  判断动作 优先调用
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        /**
         *  返回值是用来标记移动还是滑动
         */
        int move = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(move,ItemTouchHelper.UP);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        /**
         *      当发现移动的时候,要进行刷新
         *
         *      ① 刷新数据
         *
         *      ② 刷新适配器
         */


        callback.onItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    /**
     *  是否允许长按拖拽
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

}
