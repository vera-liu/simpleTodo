package com.example.vera_liu.simpletodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.EditText;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> todoItems, doneItems;
    ItemAdapter todoAdapter, doneAdapter;
    RecyclerView todoRvItems, doneRvItems;
    public ItemTouchHelper.SimpleCallback initItemTouchCallback(final ItemAdapter adapter, final ArrayList<String> items) {
        return new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                items.remove(position);
                adapter.notifyItemRemoved(position);
                writeItems();
            }
        };
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        todoRvItems = (RecyclerView) findViewById(R.id.lvItems);
        doneRvItems = (RecyclerView) findViewById(R.id.donelvItems);
        readItems();
        todoItems.add("First Item");
        todoItems.add("Second Item");
        doneItems = new ArrayList<String>();
        doneItems.add("Third Item");
        todoAdapter = new ItemAdapter(this, todoItems, false);
        doneAdapter = new ItemAdapter(this, doneItems, true);

        todoRvItems.setAdapter(todoAdapter);
        doneRvItems.setAdapter(doneAdapter);
        todoRvItems.setLayoutManager(new LinearLayoutManager(this));
        doneRvItems.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(initItemTouchCallback((ItemAdapter) todoRvItems.getAdapter(), todoItems));
        itemTouchHelper.attachToRecyclerView(todoRvItems);
        ItemTouchHelper doneItemTouchHelper = new ItemTouchHelper(initItemTouchCallback((ItemAdapter) doneRvItems.getAdapter(), doneItems));
        doneItemTouchHelper.attachToRecyclerView(doneRvItems);
        todoAdapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String itemText = todoItems.get(position);
                todoItems.remove(position);
                doneItems.add(0, itemText);
                todoAdapter.notifyItemRemoved(position);
                doneAdapter.notifyItemInserted(0);
            }
        });
        doneAdapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String itemText = doneItems.get(position);
                doneItems.remove(position);
                todoItems.add(0, itemText);
                doneAdapter.notifyItemRemoved(position);
                todoAdapter.notifyItemInserted(0);
            }
        });

    }
    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            todoItems = new ArrayList<String>();
        }
    }
    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, todoItems);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onAddItem(View v) {
        EditText etNewItem  = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        todoItems.add(0, itemText);
        todoAdapter.notifyItemInserted(0);
        todoRvItems.scrollToPosition(0);
        etNewItem.setText("");
        writeItems();
    }
    public void onDumpCompleted(View v) {
        doneItems.clear();
        doneAdapter.notifyDataSetChanged();
    }
}
