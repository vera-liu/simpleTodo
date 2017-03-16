package com.example.vera_liu.simpletodo;

/**
 * Created by vera_liu on 3/15/17.
 */

public class Item {
    private String task;
    private String priority;
    private String note;
    public String getTask() {
        return task;
    }
    public String getPriority() {
        return priority;
    }
    public String getNote() {
        return note;
    }
    public Item(String task) {
        this.task = task;
        this.priority = "medium";
        this.note = "";
    }
    public void setPriority(String priority) {
        this.priority = priority;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public void setTask(String task) {
        this.task = task;
    }
    public void setItem(String task, String priority, String note) {
        this.task = task;
        this.priority = priority;
        this.note = note;
    }

}
