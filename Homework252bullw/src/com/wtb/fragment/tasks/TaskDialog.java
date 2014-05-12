package com.wtb.fragment.tasks;


import android.app.DialogFragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import com.wtb.fragment.tasks.TaskDatabaseHelper;

public class TaskDialog extends DialogFragment implements OnEditorActionListener {
	   private EditText mEditText;

	    public TaskDialog() {
	        // Empty constructor required for DialogFragment
	    }

	    public interface EditTaskDialogListener {
	        void onFinishEditDialog(String inputText);
	    }

	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        View view = inflater.inflate(R.layout.fragment_edit_task, container);
	        mEditText = (EditText) view.findViewById(R.id.txt_your_name);
	        getDialog().setTitle("Add new task");
	        
	        mEditText.requestFocus();
	        getDialog().getWindow().setSoftInputMode(
	        		LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	        mEditText.setOnEditorActionListener(this);

	        return view;

	    }
	    
	    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
	        if (EditorInfo.IME_ACTION_DONE == actionId) {
	            // Return input text to activity
	        	
	        	TaskDatabaseHelper tbh = new TaskDatabaseHelper(getActivity());
	        	Task task = new Task();
	        	//TODO: validate string
	        	mEditText = (EditText) v.findViewById(R.id.txt_your_name);
	        	task.setTaskDetails(mEditText.getText().toString());
	        	tbh.addTask(task);
	            EditTaskDialogListener activity = (EditTaskDialogListener) getActivity();
	            activity.onFinishEditDialog(mEditText.getText().toString());
	            this.dismiss();
	            return true;
	        }
	        return false;
	    }
	    
	    public void onFinishEditDialog(String inputText) {
	        Toast.makeText(getActivity(), "Hi, " + inputText, Toast.LENGTH_SHORT).show();
	    }

}
