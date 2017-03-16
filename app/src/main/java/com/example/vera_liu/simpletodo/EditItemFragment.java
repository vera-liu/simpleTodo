package com.example.vera_liu.simpletodo;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;


public class EditItemFragment extends DialogFragment implements TextView.OnEditorActionListener {
    private EditText editNote;
    private TextView taskView;
    private int position;
    public EditItemFragment() {

    }
    public interface EditNoteListener {
        void onFinishEditNote(String input, int position);
    }
    public static EditItemFragment newInstance(String task, String note, int position) {
        EditItemFragment fragment = new EditItemFragment();
        Bundle args = new Bundle();
        args.putString("task", task);
        args.putString("note", note);
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        taskView = (TextView) view.findViewById(R.id.task);
        taskView.setText("Task");
        editNote = (EditText) view.findViewById(R.id.edit_note);
        editNote.setText(getArguments().getString("task", "Untitled task"));
        editNote.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        editNote.setOnEditorActionListener(this);

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId || EditorInfo.IME_ACTION_UNSPECIFIED == actionId) {
            EditNoteListener listener = (EditNoteListener) getActivity();
            listener.onFinishEditNote(editNote.getText().toString(), getArguments().getInt("position", position));
            dismiss();

            return true;
        }
        return false;
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
