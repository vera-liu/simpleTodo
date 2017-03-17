package com.example.vera_liu.simpletodo;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;


public class EditItemFragment extends DialogFragment implements Button.OnClickListener {
    private EditText editTask, editNote;
    private TextView taskView;
    private Button saveButton;
    private Spinner prioritySpinner;
    private DatePicker datePicker;
    private static String priority;
    private int position;
    private static int year, month, day;
    private String[] priorities = {"High", "Medium", "Low"};
    public EditItemFragment() {

    }
    public interface EditNoteListener {
        void onSaveEdit(String task, String note, String priority, int year, int month, int day, int position);
    }
    public static EditItemFragment newInstance(Item item, int position) {
        EditItemFragment fragment = new EditItemFragment();
        Bundle args = new Bundle();
        args.putString("task", item.getTask());
        args.putString("note", item.getNote());
        priority = item.getPriority();
        int[] date = item.getDate();
        year = date[0];
        month = date[1];
        day = date[2];
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        taskView = (TextView) view.findViewById(R.id.task);
        taskView.setText("Task");
        editTask = (EditText) view.findViewById(R.id.edit_task);
        editNote = (EditText) view.findViewById(R.id.edit_note);
        editTask.setText(getArguments().getString("task", "Untitled task"));
        editTask.requestFocus();
        editNote.setText(getArguments().getString("note", ""));
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        saveButton = (Button) view.findViewById(R.id.saveBtn);
        saveButton.setOnClickListener(this);

        prioritySpinner = (Spinner) view.findViewById(R.id.prioritySp);
        prioritySpinner.setSelection(Arrays.asList(priorities).indexOf(priority));
        prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                priority  = parent.getItemAtPosition(position).toString();
                Log.d("set priority", priority);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int y, int m, int d) {
                year = y;
                month = m;
                day = d;
            }
        });
    }

    @Override
    public void onClick(View v) {
        EditNoteListener listener = (EditNoteListener) getActivity();
        Log.d("send priority", priority);
        listener.onSaveEdit(
                editTask.getText().toString(),
                editNote.getText().toString(),
                priority,
                year,
                month,
                day,
                getArguments().getInt("position", position));
        dismiss();
    }

    public void onResume() {
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        super.onResume();
    }

}
