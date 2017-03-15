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
    ArrayList<String> items;
    ItemAdapter itemsAdapter;
    RecyclerView lvItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (RecyclerView) findViewById(R.id.lvItems);
        readItems();
        items.add("First Item");
        items.add("Second Item");
        itemsAdapter = new ItemAdapter(this, items);

        lvItems.setAdapter(itemsAdapter);
        lvItems.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                ItemAdapter adapter = (ItemAdapter) lvItems.getAdapter();
                items.remove(position);
                adapter.notifyItemRemoved(position);
                writeItems();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(lvItems);

    }
    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }
    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onAddItem(View v) {
        EditText etNewItem  = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        items.add(0, itemText);
        itemsAdapter.notifyItemInserted(0);
        lvItems.scrollToPosition(0);
        etNewItem.setText("");
        writeItems();
    }
}
