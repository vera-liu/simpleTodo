package com.example.vera_liu.simpletodo;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by vera_liu on 3/14/17.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private ArrayList<Item> items;
    private Context context;
    private OnItemClickListener checkboxListener, textListener;
    private boolean completed;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener checkboxListener, OnItemClickListener textListener) {
        this.checkboxListener = checkboxListener;
        this.textListener = textListener;
    }

    public ItemAdapter(Context adapterContext, ArrayList<Item> adapterItems, boolean completed) {
        context = adapterContext;
        items = adapterItems;
        this.completed = completed;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemAdapter.ViewHolder viewHolder, int position) {
        final int pos = position;
        final ItemAdapter.ViewHolder vHolder = viewHolder;
//        String itemText = items.get(position).getTask();
        TextView itemName = viewHolder.itemName;
        CheckBox itemCheckBox = viewHolder.itemCheckBox;
        if (this.completed) {
            itemName.setPaintFlags(itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            itemCheckBox.setChecked(true);
        } else {
            itemCheckBox.setChecked(false);
        }
//        itemName.setText(itemText);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName;
        public CheckBox itemCheckBox;
        public ViewHolder(View itemView) {
            super(itemView);
            final View iView = itemView;
            itemName = (TextView) itemView.findViewById(R.id.itemName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (textListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            textListener.onItemClick(iView, position);
                        }
                    }
                }
            });
            itemCheckBox = (CheckBox) itemView.findViewById(R.id.itemCheckbox);
            itemCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkboxListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            checkboxListener.onItemClick(iView, position);
                        }
                    }
                }
            });
        }
    }
}
