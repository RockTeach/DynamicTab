package com.rock.rock.dynamictab.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rock.rock.recyclerviewtorecyclerview.R;
import com.rock.rock.dynamictab.callback.ItemTouchHelperAdapterCallback;

import java.util.Collections;
import java.util.List;


public class TeachAdapter extends RecyclerView.Adapter<TeachAdapter.ViewHolder> implements View.OnClickListener,ItemTouchHelperAdapterCallback{

    private List<String> data;

    private LayoutInflater inflater;
    private RecyclerView recyclerView;
    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public TeachAdapter(Context context, List<String> data){
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = inflater.inflate(R.layout.item, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.btn.setText(data.get(position));
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onClick(View v) {
        int position = recyclerView.getChildAdapterPosition(v);
        listener.onItemClick(recyclerView,v,position);
    }

    public void onItemRemoved(int position){
        data.remove(position);
        notifyItemRemoved(position);
    }

    public void onItemInserted(String item){
        data.add(item);
        notifyItemInserted(data.size());
    }

    public String getItem(int position){
        return data.get(position);
    }

    @Override
    public boolean onItemMoved(int fromPosition, int toPosition) {
        /**
         *   交换数据，Collection中有专门的方法用来交互数据
         */
        Collections.swap(data,fromPosition,toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return false;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView btn;

        ViewHolder(View itemView) {
            super(itemView);
            btn = (TextView) itemView.findViewById(R.id.teach_item);
        }
    }

    public interface OnItemClickListener{

        void onItemClick(RecyclerView recycler,View v,int position);

    }

}
