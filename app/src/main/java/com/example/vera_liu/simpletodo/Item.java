package com.example.vera_liu.simpletodo;

/**
 * Created by vera_liu on 3/15/17.
 */

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Calendar;
import java.util.List;

@Table(name = "Items")
public class Item extends Model {

    @Column(name = "Task")
    public String task;
    @Column(name = "Priority")
    private String priority;
    @Column(name = "Note")
    private String note;
    @Column(name = "Done")
    private boolean done;
    @Column(name = "Year")
    private int year;
    @Column(name = "Month")
    private int month;
    @Column(name = "Day")
    private int day;
    public Item() {
        super();
    }
    public Item(String task) {
        super();
        this.task = task;
        this.priority = "Medium";
        this.note = "";
        this.done = false;
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        Log.d("year", Integer.toString(c.get(Calendar.YEAR)));
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

    public boolean getDone() {
        return done;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
        this.save();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
        this.save();
    }

    public void setDone() {
        this.done = !this.done;
        this.save();
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
        this.save();
    }

    public int[] getDate() {
        int[] dates = {year, month, day};
        return dates;
    }

    public void setDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.save();
    }
}
