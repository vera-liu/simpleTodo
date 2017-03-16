package com.example.vera_liu.simpletodo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.EditText;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.query.Select;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements EditItemFragment.EditNoteListener {
    ArrayList<Item> todoItems, doneItems;
    ItemAdapter todoAdapter, doneAdapter;
    RecyclerView todoRvItems, doneRvItems;
    public void updateFiles() {
//        writeItems("todo.txt", todoItems);
//        writeItems("done.txt", doneItems);
    }
    public ItemTouchHelper.SimpleCallback initItemTouchCallback(final ItemAdapter adapter, final ArrayList<Item> items) {
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
                updateFiles();
            }
        };
    }
    protected ItemAdapter.OnItemClickListener getCheckboxListener(final ArrayList<Item> sourceList, final ArrayList<Item> targetList, final ItemAdapter sourceAdapter, final ItemAdapter targetAdapter ) {
        return (new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Item itemText = sourceList.get(position);
                sourceList.remove(position);
                targetList.add(0, itemText);
                sourceAdapter.notifyItemRemoved(position);
                targetAdapter.notifyItemInserted(0);
                updateFiles();
            }
        });
    }
    protected void showEditDialog(int position) {
        Item item = todoItems.get(position);
        FragmentManager fm = getSupportFragmentManager();
        //EditItemFragment editFragment = EditItemFragment.newInstance(item.getTask(), item.getNote(), position);
        //editFragment.show(fm,"fragment_edit_item");
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
        ItemTouchHelper doneItemTouchHelper = new ItemTouchHelper(initItemTouchCallback((ItemAdapter) doneRvItems.getAdapter(), doneItems));
        doneItemTouchHelper.attachToRecyclerView(doneRvItems);

        todoAdapter.setOnItemClickListener(getCheckboxListener(todoItems, doneItems, todoAdapter, doneAdapter), getTextListener());
        doneAdapter.setOnItemClickListener(getCheckboxListener(doneItems, todoItems, doneAdapter, todoAdapter), null);
    }
    protected void setAdapters() {
        todoAdapter = new ItemAdapter(this, todoItems, false);
        doneAdapter = new ItemAdapter(this, doneItems, true);

        todoRvItems.setAdapter(todoAdapter);
        doneRvItems.setAdapter(doneAdapter);
        todoRvItems.setLayoutManager(new LinearLayoutManager(this));
        doneRvItems.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Configuration dbConfiguration = new Configuration.Builder(this).setDatabaseName("todo.db").create();
        ActiveAndroid.initialize(dbConfiguration);
        todoRvItems = (RecyclerView) findViewById(R.id.lvItems);
        doneRvItems = (RecyclerView) findViewById(R.id.donelvItems);
        todoItems = new ArrayList<Item>();
        doneItems = new ArrayList<Item>();

        Item newItem = new Item("First");
        newItem.save();
        todoItems = (ArrayList) new Select().from(Item.class).execute();
        doneItems = (ArrayList) new Select().from(Item.class).execute();
        setAdapters();
        setTouchHelpers();
    }
    private void readItems(String filename, ArrayList<Item> items) {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, filename);
        try {
            ArrayList<String> itemTasks = new ArrayList<String>(FileUtils.readLines(todoFile));
            for (String task : itemTasks) {
                items.add(new Item(task));
            }
        } catch (IOException e) {
            items = new ArrayList<Item>();
        }
    }
    private void writeItems(String filename, ArrayList<Item> items) {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, filename);
        try {
            ArrayList<String> todoTasks = new ArrayList<String>();
            for(Item item : items) {
//                todoTasks.add(item.getTask());
            }
            FileUtils.writeLines(todoFile, todoTasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onAddItem(View v) {
        EditText etNewItem  = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        todoItems.add(0, new Item(itemText));
        todoAdapter.notifyItemInserted(0);
        todoRvItems.scrollToPosition(0);
        etNewItem.setText("");
        updateFiles();
    }
    public void onDumpCompleted(View v) {
        doneItems.clear();
        doneAdapter.notifyDataSetChanged();
        updateFiles();
    }
    @Override
    public void onFinishEditNote(String input, int position) {
        Item editItem = todoItems.get(position);
        //editItem.setTask(input);
        todoAdapter.notifyItemChanged(position);
        updateFiles();
    }
}
