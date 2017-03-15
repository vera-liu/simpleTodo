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
    private ArrayList<String> items;
    private Context context;

    public ItemAdapter(Context adapterContext, ArrayList<String> adapterItems) {
        context = adapterContext;
        items = adapterItems;
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
        viewHolder.itemCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemText = items.get(pos);
                TextView itemName = vHolder.itemName;
                if ((itemName.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0) {
                    itemName.setPaintFlags(itemName.getPaintFlags() & ~(Paint.STRIKE_THRU_TEXT_FLAG));
                } else {
                    itemName.setPaintFlags(itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }
        });
        String itemText = items.get(position);
        TextView itemName = viewHolder.itemName;
        itemName.setText(itemText);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName;
        public CheckBox itemCheckBox;
        public ViewHolder(View itemView) {
            super(itemView);
            itemName = (TextView) itemView.findViewById(R.id.itemName);
            itemCheckBox = (CheckBox) itemView.findViewById(R.id.itemCheckbox);
        }
    }
}
