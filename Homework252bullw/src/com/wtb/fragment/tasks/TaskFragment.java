package com.wtb.fragment.tasks;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TaskFragment extends Fragment {
	
	private final String TAG = "TaskFragment";
	
	private Task mTask = null;
	public int taskId = -1;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.task_entry, container, false);
		setTask(mTask);
        return v;
    }
    
    @Override
	public void onResume() {
        View view = this.getView();
        if (view != null && mTask != null) {
        	setTask(mTask);
        }
		super.onResume();
	}

	public void setTask(Task task) {
		mTask = task;
		
		View view = this.getView();
		if (view != null) {
			Log.d(TAG, "view not null, setting text up");
	        TextView t = (TextView) view.findViewById(R.id.task_entry);
	        t.setText(task.toString());
	        taskId = task.getId();
		}
	}

}
