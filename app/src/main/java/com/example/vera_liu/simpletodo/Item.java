package com.example.vera_liu.simpletodo;

/**
 * Created by vera_liu on 3/15/17.
 */

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "Items", id = "_id")
public class Item extends Model {

    @Column(name = "Task")
    public String task;
    @Column(name = "Priority")
    private String priority;
    @Column(name = "Note")
    private String note;
    @Column(name = "Done")
    private boolean done;
    public Item() {
        super();
    }
    public Item(String task) {
        super();
        this.task = task;
        this.priority = "medium";
        this.note = "";
        this.done = false;
    }

    public List<Item> getAll() {
        return new Select()
                .from(Item.class)
                .execute();
    }

    public List<Item> getTodo() {
        return new Select()
                .from(Item.class)
                .where("Done = ?", false)
                .execute();
    }

    public List<Item> getDone() {
        return new Select()
                .from(Item.class)
                .where("Done = ?", true)
                .execute();
    }

}
