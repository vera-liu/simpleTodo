package com.example.vera_liu.simpletodo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.EditText;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements EditItemFragment.EditNoteListener {
    ArrayList<Item> todoItems;
    ItemAdapter todoAdapter;
    RecyclerView todoRvItems;

    public ItemTouchHelper.SimpleCallback initItemTouchCallback(final ItemAdapter adapter, final ArrayList<Item> items) {
        return new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                Item itemToRemove = items.get(position);
                items.remove(position);
                adapter.notifyItemRemoved(position);
                itemToRemove.delete();
            }
        };
    }

    protected ItemAdapter.OnItemClickListener getCheckboxListener(final ArrayList<Item> sourceList, final ItemAdapter sourceAdapter) {
        return (new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Item itemText = sourceList.get(position);
                itemText.setDone();
                sourceAdapter.notifyItemChanged(position);
            }
        });
    }
    protected void showEditDialog(int position) {
        Item item = todoItems.get(position);
        FragmentManager fm = getSupportFragmentManager();
        EditItemFragment editFragment = EditItemFragment.newInstance(item, position);
        editFragment.show(fm,"fragment_edit_item");
    }
    protected ItemAdapter.OnItemClickListener getTextListener() {
        return (new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                showEditDialog(position);
            }
        });
    }
    protected void setTouchHelpers() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(initItemTouchCallback((ItemAdapter) todoRvItems.getAdapter(), todoItems));
        itemTouchHelper.attachToRecyclerView(todoRvItems);

        todoAdapter.setOnItemClickListener(getCheckboxListener(todoItems, todoAdapter), getTextListener());
    }
    protected void setAdapters() {
        todoAdapter = new ItemAdapter(this, todoItems);
        todoRvItems.setAdapter(todoAdapter);
        todoRvItems.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        todoRvItems = (RecyclerView) findViewById(R.id.lvItems);
        todoItems = (ArrayList) new Select().from(Item.class).execute();
        setAdapters();
        setTouchHelpers();
    }

    public void onAddItem(View v) {
        EditText etNewItem  = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        Item newItem = new Item(itemText);
        newItem.save();
        todoItems.add(0, newItem);
        todoAdapter.notifyItemInserted(0);
        todoRvItems.scrollToPosition(0);
        etNewItem.setText("");
    }
    public void onDumpCompleted(View v) {
        for (Iterator<Item> iterator = todoItems.iterator(); iterator.hasNext();) {
            Item item = iterator.next();
            if (item.getDone()) {
                // Remove the current element from the iterator and the list.
                item.delete();
                iterator.remove();
            }
        }
        todoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveEdit(String task, String note, String priority, int year, int month, int day, int position) {
        Item editItem = todoItems.get(position);
        editItem.setTask(task);
        editItem.setNote(note);
        editItem.setPriority(priority);
        editItem.setDate(year, month, day);
        todoAdapter.notifyItemChanged(position);
    }

}
